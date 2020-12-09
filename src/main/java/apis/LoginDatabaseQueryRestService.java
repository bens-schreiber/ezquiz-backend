package apis;

import apis.pojo.UserData;
import database.QueryExecutor;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class LoginDatabaseQueryRestService extends RestService{

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response loginUser(UserData request) {
        try {

            boolean userExists = QueryExecutor.runQuery(
                    "select username, pass from info where username ='"
                            + request.getUsername() + "'"
                            + " and pass='"
                            + request.getPassword() + "'").has("obj0");

            //todo: add token
           if (userExists) {
               return okJSON(Response.Status.ACCEPTED);
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

            QueryExecutor.executeUpdateQuery(
                    "insert into info(username, pass) values "
                    + "('" + request.getUsername() + "','" + request.getPassword() + "')");

            boolean userExists = QueryExecutor.runQuery(
                    "select username, pass from info where username ='"
                            + request.getUsername() + "'"
                            + " and pass='"
                            + request.getPassword() + "'").has("obj0");

            if (userExists) {
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

