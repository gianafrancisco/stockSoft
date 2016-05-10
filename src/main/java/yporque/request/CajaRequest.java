package yporque.request;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by francisco on 30/12/15.
 */
public class CajaRequest {

    private String username;
    private Instant fecha;

    public CajaRequest(String username, Instant fecha) {
        this.username = username;
        this.fecha = fecha;
    }

    public CajaRequest() {
        this.username = "";
        this.fecha = LocalDateTime.now().toInstant(ZoneOffset.UTC);
    }

    public String getUsername() {
        return username;
    }

    public Instant getFecha() {
        return fecha;
    }
}
