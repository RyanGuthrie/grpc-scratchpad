package net.ryanguthrie.grpc.server;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 3000;

        HostnameGreeter greater = new HostnameGreeter("localhost");

        Server server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(greater)
                .build()
                .start();

        System.out.println("Listening on port " + port);
        Thread.sleep(10000);
    }
}
