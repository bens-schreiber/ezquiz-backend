package apis;

import javax.ws.rs.core.Response;

public class RestService {

    public Response okJSON_(Response.Status status, String token, boolean admin) {
        return Response.status(status).entity(admin).header("Token", token).build();
    }

    public Response okJSON(Response.Status status) {
        return Response.status(status).entity("{}").build();
    }

    public Response okJSON(Response.Status status, String jsonBody) {

        return Response.status(status).entity(jsonBody).build();
    }
}