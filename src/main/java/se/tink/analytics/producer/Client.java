package se.tink.analytics.producer;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.Protocol;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClientBuilder;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

public class Client {


    public static void main(String[] args) throws Exception {

        KinesisAsyncClientBuilder kinesisClientBuilder = KinesisAsyncClient.builder().region(Region.EU_WEST_1);
        kinesisClientBuilder
                .endpointOverride(new URI("http://localhost:4567"))
                .httpClientBuilder(NettyNioAsyncHttpClient.builder().protocol(Protocol.HTTP1_1));

        KinesisAsyncClient kinesisClient = kinesisClientBuilder.build();

        SdkBytes bytes = SdkBytes.fromByteArray("teststring".getBytes());
        String partitionKey = "my-partition";
        PutRecordRequest recordRequest =
                PutRecordRequest.builder()
                        .streamName("events")
                        .partitionKey(partitionKey)
                        .data(bytes)
                        .build();
        CompletableFuture<PutRecordResponse> responseFuture = kinesisClient.putRecord(recordRequest);
        PutRecordResponse response = responseFuture.get(1000, TimeUnit.MILLISECONDS);
        response.shardId();

    }
}
