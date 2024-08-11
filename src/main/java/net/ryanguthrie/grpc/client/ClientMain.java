package net.ryanguthrie.grpc.client;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.ryanguthrie.grpc.server.GreeterGrpc;
import net.ryanguthrie.grpc.server.HelloReply;
import net.ryanguthrie.grpc.server.HelloRequest;

public class ClientMain {
    public static void main(String[] args) {
        String target = "localhost:3000";

        ManagedChannel channel1 = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();

        GreeterGrpc.GreeterBlockingStub blockingStub;

        blockingStub = GreeterGrpc.newBlockingStub(channel1);
        HelloRequest request = HelloRequest.newBuilder()
                .setName("teehee")
                .setBoo("boohoohoo")
                .setFoo("alsdkfjasdf")
                .build();

        HelloReply helloReply = blockingStub.sayHello(request);
        System.out.printf("%s", helloReply.getMessage());
    }
}
