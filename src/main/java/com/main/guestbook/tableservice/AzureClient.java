package com.main.guestbook.tableservice;

import com.azure.core.util.BinaryData;
import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.azure.data.tables.models.TableServiceException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.main.guestbook.model.Image;
import com.main.guestbook.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class AzureClient {

    TableServiceClient tableServiceClient;
    TableClient tableClient;
    QueueClient queueClient;
    QueueClient deleteResizeQueueClient;

    BlobServiceClient blobServiceClient;

    BlobContainerClient containerClient;
    BlobClient blobClient;




    final String partitionKey = "postsPartKey";
    final String connectionString = "DefaultEndpointsProtocol=https;AccountName=guestbookstorage99;AccountKey=bTuX+vfB4ZrAifz/bdPxuZrjJ4OCHxQT0QpaTKnusPCOKjXDsirO3BQSKv3JiVV0lTPri4zwinFi+AStIpUZDQ==;EndpointSuffix=core.windows.net";
    final String queueName = "guestbook-file-queue";
    final String deleteQueueName = "guestbook-resize-delete-queue";

    final String imageStorageContainerName = "images-storage";
    final String resizeStorageContainerName = "resized-images-storage";

    public AzureClient() {
        tableServiceClient = new TableServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        tableServiceClient.createTableIfNotExists("Post");
        tableClient = tableServiceClient.getTableClient("Post");
        queueClient = new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(queueName)
                .buildClient();
        deleteResizeQueueClient = new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(deleteQueueName)
                .buildClient();
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        containerClient = blobServiceClient
                .getBlobContainerClient(imageStorageContainerName);
    }


    public void addPost(String title, String text, MultipartFile image) {
        try {
            String rowKey = getNewRowKey();
            String nowDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            TableEntity tableEntity = new TableEntity(partitionKey, rowKey)
                    .addProperty("Title", title)
                    .addProperty("Text", text)
                    .addProperty("Date", nowDate);

            tableClient.createEntity(tableEntity);
            Image newImage = new Image(rowKey, image);
            containerClient = blobServiceClient.getBlobContainerClient(imageStorageContainerName);
            blobClient = containerClient.getBlobClient(newImage.getName());
            blobClient.upload(BinaryData.fromBytes(newImage.getFile()));
            String imageName = newImage.getName();
            String message = Base64.getEncoder().encodeToString(imageName.getBytes());
            queueClient.sendMessage(message);
        } catch (TableServiceException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public Post getPostById(String rowKey) {
        TableEntity postEntity = tableClient.getEntity(partitionKey, rowKey);

        LocalDateTime localDateTime = LocalDateTime.parse(postEntity.getProperty("Date").toString());
        Post post = new Post(postEntity.getPartitionKey(), postEntity.getRowKey(), postEntity.getProperty("Title").toString(), postEntity.getProperty("Text").toString(), localDateTime);

        Image image = new Image(rowKey);
        containerClient = blobServiceClient.getBlobContainerClient(imageStorageContainerName);
        blobClient = containerClient.getBlobClient(image.getName());
        if (blobClient.exists()) {
            image.setFile(blobClient.downloadContent().toBytes());
            post.setFile(image.getFile());
        }
        return post;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        try {
            for (TableEntity postEntity: tableClient.listEntities()
                 ) {
                LocalDateTime localDateTime = LocalDateTime.parse(postEntity.getProperty("Date").toString());
                Image image = new Image(postEntity.getRowKey());
                Post post = new Post(postEntity.getPartitionKey(), postEntity.getRowKey(), postEntity.getProperty("Title").toString(), postEntity.getProperty("Text").toString(), localDateTime);
                containerClient = blobServiceClient.getBlobContainerClient(resizeStorageContainerName);
                blobClient = containerClient.getBlobClient(image.getName());
                if (blobClient.exists()) {
                    image.setFile(blobClient.downloadContent().toBytes());
                    post.setFile(image.getFile());
                }
                posts.add(post);
            }

            Comparator<Post> comparator = Comparator.comparing(Post::getDate).reversed();
            posts.sort(comparator);
            return posts;
        } catch (TableServiceException e) {
            System.out.println(e.getResponse().getStatusCode()); // 409
            throw e;
        }
    }

    public String getNewRowKey() {
        int newKey = 0;
        try {
            for (TableEntity postEntity: tableClient.listEntities()
            ) {
                int postKey = Integer.parseInt(postEntity.getRowKey());
                if (postKey > newKey) {
                    newKey = postKey;
                }
            }
            return String.valueOf(newKey + 1);
        } catch (TableServiceException e) {
            System.out.println(e.getResponse().getStatusCode()); // 409
            throw e;
        }
    }

    public void deletePost(String rowKey) {
        try {
            Image imageToDelete = new Image(rowKey);
            containerClient = blobServiceClient.getBlobContainerClient(imageStorageContainerName);
            blobClient = containerClient.getBlobClient(imageToDelete.getName());

            if (blobClient.exists()) {
                blobClient.delete();
            }

            String message = Base64.getEncoder().encodeToString(imageToDelete.getName().getBytes());
            deleteResizeQueueClient.sendMessage(message);
            tableClient.deleteEntity(tableClient.getEntity(partitionKey, rowKey));
        } catch (TableServiceException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public String getTableName() {
        return tableClient.getTableName();
    }

}
