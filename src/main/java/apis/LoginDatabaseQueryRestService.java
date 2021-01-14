package apis;

import apis.pojo.UserData;
import database.LoggedInUsers;
import database.QueryExecutor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("users")
public class LoginDatabaseQueryRestService extends RestService{

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response loginUser(UserData request) {
        try {

            boolean userExists = QueryExecutor.runQuery(
                    "select username, pass from user_logins where username ='"
                            + request.getUsername() + "'"
                            + " and pass='"
                            + request.getPassword() + "'").has("obj0");

           if (userExists) {

               String uuid = UUID.randomUUID().toString();

               LoggedInUsers.getLoggedInUsers().put(request.getUsername(), uuid);

               return okJSON_(Response.Status.ACCEPTED, uuid);

           } else {

               return okJSON(Response.Status.UNAUTHORIZED);
           }

        } catch (Exception e) {
            e.printStackTrace();
            return okJSON(Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response registerUser(UserData request) {
        try {

            boolean userRegistered = QueryExecutor.executeUpdateQuery(
                    "insert into user_logins(username, pass) values "
                    + "('" + request.getUsername() + "','" + request.getPassword() + "')") == 1;

            if (userRegistered) {
                return okJSON(Response.Status.ACCEPTED);
            } else {
                return okJSON(Response.Status.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return okJSON(Response.Status.UNAUTHORIZED);
            }
        }
}

