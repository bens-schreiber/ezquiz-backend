package apis;

import database.UserStatus;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class RestService {

    //Respond with a token for verifyUserLogin
    public Response okJSON_(Response.Status status, String token, boolean admin) {

        return Response.status(status).entity(admin).header("Token", token).build();

    }

    //Respond with empty for NO_CONTENT status
    public Response okJSON(Response.Status status) {

        return Response.status(status).entity("{}").build();

    }

    //Respond with a status and a body with information from database
    public Response okJSON(Response.Status status, String jsonBody) {

        return Response.status(status).entity(jsonBody).build();

    }

    //Validate that the given HttpHeaders contain a token in the LoggedInUsers map.
    public boolean validate(HttpHeaders headers) {

        return UserStatus.getLoggedInUsers().contains(String.valueOf(headers.getRequestHeader("token").get(0)));

    }
}