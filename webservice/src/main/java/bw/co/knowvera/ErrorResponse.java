package bw.co.knowvera;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    Integer status,
    String code,
    String message,
    List<Object> messageArguments,
    Instant timestamp
) {
    
}
