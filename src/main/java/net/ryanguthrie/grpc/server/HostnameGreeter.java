package net.ryanguthrie.grpc.server;

import io.grpc.BindableService;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ryanguthrie.grpc.server.GreeterGrpc;
import net.ryanguthrie.grpc.server.HelloRequest;
import net.ryanguthrie.grpc.server.HelloReply;
/**
 * Greeter implementation which replies identifying itself with its hostname.
 */
public final class HostnameGreeter extends GreeterGrpc.GreeterImplBase implements BindableService, GreeterGrpc.AsyncService {
    private static final Logger logger = Logger.getLogger(HostnameGreeter.class.getName());

    private final String serverName;

    public HostnameGreeter(String serverName) {
        if (serverName == null) {
            serverName = determineHostname();
        }
        this.serverName = serverName;
    }

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder()
                .setMessage(String.format("Hello %s, from %s.  %s", req.getName(), serverName, req.getBoo()))
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    private static String determineHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (IOException ex) {
            logger.log(Level.INFO, "Failed to determine hostname. Will generate one", ex);
        }
        // Strange. Well, let's make an identifier for ourselves.
        return "generated-" + new Random().nextInt();
    }
}