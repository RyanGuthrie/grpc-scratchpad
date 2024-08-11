package net.ryanguthrie.grpc.server;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 3001;

        ManagementService greater = new ManagementService();

        Server server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(greater)
                .build()
                .start();

        greater.shutdownCallback(() -> {
            System.out.println("Calling shutdown");
            server.shutdown();
            System.out.println("Called shutdown");
        });

        System.out.println("Listening on port " + port);
        server.awaitTermination();
        System.out.println("Server shutting down");
    }
}
