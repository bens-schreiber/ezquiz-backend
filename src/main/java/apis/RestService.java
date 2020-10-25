package apis;

import javax.ws.rs.core.Response;

public class RestService {
    public Response okJSON(Response.Status status, String jsonBody) {
        return Response.status(status).entity(jsonBody).build();
    }
}