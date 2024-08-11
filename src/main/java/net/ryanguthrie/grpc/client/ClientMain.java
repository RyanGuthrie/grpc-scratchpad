package net.ryanguthrie.grpc.client;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.ryanguthrie.grpc.proto.ServiceGrpc;
import net.ryanguthrie.grpc.proto.ShutdownRequest;

public class ClientMain {
    public static void main(String[] args) {
        String target = "localhost:3000";

        ManagedChannel channel1 = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();

        ServiceGrpc.ServiceBlockingStub blockingStub;

        blockingStub = ServiceGrpc.newBlockingStub(channel1);

        var shutdownReq = ShutdownRequest.newBuilder()
                .setRequestor("Ryan Guthrie")
                .setAfterMs(3000)
                .build();

        var shutdownRes = blockingStub.shutdown(shutdownReq);

        System.out.printf("%s", shutdownRes.getMessage());
    }
}
