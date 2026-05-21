package backend.infrastructure.http.dto;

import java.util.List;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        List<String> details) {
}
