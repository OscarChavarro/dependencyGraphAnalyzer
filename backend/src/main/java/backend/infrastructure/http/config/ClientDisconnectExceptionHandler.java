package backend.infrastructure.http.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClientDisconnectExceptionHandler {
    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void handleAsyncRequestNotUsable() {
        // Client disconnected before the response could be fully written.
        // This is expected in some browser/network scenarios.
    }
}
