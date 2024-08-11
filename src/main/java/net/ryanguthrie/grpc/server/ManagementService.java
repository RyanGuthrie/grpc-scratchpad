package net.ryanguthrie.grpc.server;

import io.grpc.BindableService;
import io.grpc.stub.StreamObserver;
import net.ryanguthrie.grpc.proto.ServiceGrpc;
import net.ryanguthrie.grpc.proto.ShutdownRequest;
import net.ryanguthrie.grpc.proto.ShutdownResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ManagementService extends ServiceGrpc.ServiceImplBase implements BindableService, ServiceGrpc.AsyncService {
    private static final Logger logger = Logger.getLogger(ManagementService.class.getName());
    private static final doShutdown unconfiguredShutdown = () -> logger.info("Shutdown requested by no callback func provided");

    // A func is used to break the circular dependency between the service itself, and the ManagementService
    private doShutdown shutdownFunc;

    public ManagementService() {
        shutdownFunc = unconfiguredShutdown;
    }

    @Override
    public void shutdown(ShutdownRequest request, StreamObserver<net.ryanguthrie.grpc.proto.ShutdownResponse> responseObserver) {
        System.out.printf("%s requested a shutdown after %s ms\n", request.getRequestor(), request.getAfterMs());

        Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(request.getAfterMs());
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Interrupted while sleeping", e);
                responseObserver.onError(e);

                throw new RuntimeException(e);
            }

            shutdownFunc.shutdown();
        });

        ShutdownResponse response = ShutdownResponse.newBuilder().
                setMessage(String.format("Shutdown at %s", java.time.LocalDate.now()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public interface doShutdown {
        void shutdown();
    }

    public void shutdownCallback(doShutdown doShutdownFunc) {
        this.shutdownFunc = doShutdownFunc;
    }
}