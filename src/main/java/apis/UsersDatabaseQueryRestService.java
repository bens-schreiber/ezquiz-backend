package apis;

import apis.pojo.UserData;
import database.Token;
import database.UserStatus;
import database.QueryExecutor;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UsersDatabaseQueryRestService extends RestService {


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    //Verify if a username and password exists.
    public Response verifyUserLogin(UserData request) {
        try {

            boolean userExists = QueryExecutor.runQuery(
                    "select username, password from user_logins where username ='"
                            + request.getUsername() + "'"
                            + " and password='"
                            + request.getPassword() + "'").has("obj0");

            if (userExists) {

                //Generate an auth token for the user
                Token authToken = Token.randomToken();
                UserStatus.getLoggedInUsers().add(authToken);

                boolean admin = Boolean.parseBoolean(QueryExecutor.runQuery("select admin from user_logins where username='" + request.getUsername() + "'")
                        .getJSONObject("obj0")
                        .getString("admin"));

                //Return a auth token UUID.
                return okJSON_(Response.Status.ACCEPTED, authToken.getToken().toString(), admin);

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
    //Post a new user to database.
    public Response registerUser(UserData request) {
        try {

            //1 bit indicates admin as of right now
            String query = "insert into user_logins values(?, ?, 1)";
            boolean userRegistered = QueryExecutor.executeUpdateQuery(query, request.getUsername(), request.getPassword()) > 0;

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


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("score")
    //Insert score into database
    public Response postQuizScore(UserData userData, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {

                boolean scoreAdded = QueryExecutor.executeUpdateQuery(
                        "insert into user_quiz_scores values ('" +
                                userData.getUsername() + "'," +
                                userData.getQuizKey() + "," +
                                userData.getScore() + ")"
                ) == 1;

                if (scoreAdded) {

                    return okJSON(Response.Status.ACCEPTED);

                } return okJSON(Response.Status.NO_CONTENT);

            } catch (Exception e) {
                e.printStackTrace();
                return okJSON(Response.Status.NO_CONTENT);
            }
        } return okJSON(Response.Status.UNAUTHORIZED);
    }


    @GET
    @Path("score/{username}")
    //Get all scores with the correlating username
    public Response getUserScores(@PathParam("username") String username, @Context HttpHeaders headers) {
        if(validate(headers)) {
            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        "select * from user_quiz_scores, quizzes where user_quiz_scores.quizkey=quizzes.quizkey and username='" + username + "'"
                ).toString());

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }
        }
        return okJSON(Response.Status.UNAUTHORIZED);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("key")
    //Insert key into a users saved keys.
    public Response postQuizKey(UserData userData, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {

                boolean accountAdded = QueryExecutor.executeUpdateQuery(
                        "insert into user_saved_quizkeys values ('" +
                                userData.getUsername() + "'," +
                                userData.getQuizKey() + ")"
                ) == 1;

                if (accountAdded) {

                    return okJSON(Response.Status.ACCEPTED);

                } return okJSON(Response.Status.NO_CONTENT);

            } catch (Exception e) {
                e.printStackTrace();
                return okJSON(Response.Status.NO_CONTENT);
            }
        } return okJSON(Response.Status.UNAUTHORIZED);
    }


    @GET
    @Path("key/{username}")
    //Get all saved quiz keys from a user
    public Response getSavedKeys(@PathParam("username") String username,  @Context HttpHeaders headers) {
        if(validate(headers)) {
            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        "select * from user_saved_quizkeys, quizzes where user_saved_quizkeys.quizkey=quizzes.quizkey and username='" + username + "'"
                ).toString());

            } catch (Exception e) {
                e.printStackTrace();
                return okJSON(Response.Status.NO_CONTENT);
            }
        }
        return okJSON(Response.Status.UNAUTHORIZED);
    }


    @DELETE
    @Path("key/{username}/{quizkey}")
    //Delete a saved key from database.
    public Response deleteSavedKey(@PathParam("username") String username, @PathParam("quizkey") int quizkey, @Context HttpHeaders headers) {
        if(validate(headers)) {
            try {

                boolean valid = QueryExecutor.executeUpdateQuery("delete from user_saved_quizkeys where" +
                        " username='" + username + "'" + " and quizkey=" + quizkey) == 1;

                if (valid) {

                    return okJSON(Response.Status.ACCEPTED);

                } else return okJSON(Response.Status.NO_CONTENT);

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }
        }
        return okJSON(Response.Status.UNAUTHORIZED);
    }


    @GET
    @Path("quizzes/{username}")
    //Get all created quizzes of a user
    public Response getKeyFromDatabase(@PathParam("username") String username, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        "select quizkey, quizname from quizzes where quizowner='" + username + "'"
                ).toString());

            } catch (Exception e) {
                e.printStackTrace();
                return okJSON(Response.Status.NO_CONTENT);
            }

        }
        return okJSON(Response.Status.FORBIDDEN);
    }


}

