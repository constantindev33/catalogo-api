package comportfolio.catalogo_api.exception;

import java.time.Instant;
import java.util.Map;


public record ApiError(
        Instant momento,
        int status,
        String mensagem,
        String caminho,
        Map<String, String> campos
) {
}
