package se.tink.analytics.producer;

import java.net.URI;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

public class ClientSync {


    public static void main(String[] args) throws Exception {

        KinesisClient kinesisClient = KinesisClient.builder().region(Region.EU_WEST_1)
                .endpointOverride(new URI("http://localhost:4567")).build();

        SdkBytes bytes = SdkBytes.fromByteArray("teststring".getBytes());
        String partitionKey = "my-partition";
        PutRecordRequest recordRequest =
                PutRecordRequest.builder()
                        .streamName("events")
                        .partitionKey(partitionKey)
                        .data(bytes)
                        .build();

        kinesisClient.putRecord(recordRequest);

    }
}
