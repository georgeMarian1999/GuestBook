package com.main.guestbook.tableservice;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ServiceClient {

    TableServiceClient tableServiceClient;
    TableClient tableClient;

    String partitionKey = "postsPartKey";

    final String connectionString = "DefaultEndpointsProtocol=https;AccountName=guestbookstorage99;AccountKey=bTuX+vfB4ZrAifz/bdPxuZrjJ4OCHxQT0QpaTKnusPCOKjXDsirO3BQSKv3JiVV0lTPri4zwinFi+AStIpUZDQ==;EndpointSuffix=core.windows.net";

    public ServiceClient() {
        tableServiceClient = new TableServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        tableServiceClient.createTableIfNotExists("Post");
        tableClient = tableServiceClient.getTableClient("Post");
    }


    public void addPost(String title, String text) {

    }

    public String getTableName() {
        return tableClient.getTableName();
    }

}
