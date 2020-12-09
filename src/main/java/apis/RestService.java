package apis;

import javax.ws.rs.core.Response;
import java.util.UUID;

public class RestService {

    public Response okJSON_(Response.Status status, String token) {
        return Response.status(status).entity("{}").header("Token", token).build();
    }

    public Response okJSON(Response.Status status) {
        return Response.status(status).entity("{}").build();
    }

    public Response okJSON(Response.Status status, String jsonBody) {
        return Response.status(status).entity(jsonBody).build();
    }
}