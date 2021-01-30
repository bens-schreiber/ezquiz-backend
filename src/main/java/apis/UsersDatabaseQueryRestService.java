package apis;

import apis.pojo.UserData;
import database.UserStatus;
import database.QueryExecutor;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;
import java.util.UUID;

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

                String uuid = UUID.randomUUID().toString();

                UserStatus.getLoggedInUsers().add(uuid);

                boolean admin = Boolean.parseBoolean(QueryExecutor.runQuery("select admin from user_logins where username='" + request.getUsername() + "'")
                        .getJSONObject("obj0")
                        .getString("admin"));

                return okJSON_(Response.Status.ACCEPTED, uuid, admin);

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

            boolean userRegistered = QueryExecutor.executeUpdateQuery(
                    "insert into user_logins(username, password, admin) values "
                            + "('" + request.getUsername() + "','" + request.getPassword() + "'," + 0 + ")") == 1;

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

                //Random value for primary key.
                int rand = new Random().nextInt(10000);

                boolean scoreAdded = QueryExecutor.executeUpdateQuery(
                        "insert into user_quiz_scores values (" +
                                rand + ",'" +
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
                        "select distinct a.id, a.score, b.quizname, b.quizowner, b.quizkey from user_quiz_scores a, question b " +
                                "where a.quizkey=b.quizkey and a.username='" +
                                username + "'"
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

                //Random value for primary key.
                int rand = new Random().nextInt(10000);

                boolean accountAdded = QueryExecutor.executeUpdateQuery(
                        "insert into user_saved_quizkeys values (" +
                                rand + ",'" +
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
    //Get all keys with the correlating username
    public Response getSavedKeys(@PathParam("username") String username,  @Context HttpHeaders headers) {
        if(validate(headers)) {
            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        "select distinct quizname, quizowner, quizkey from question where question.quizkey in " +
                        "(select quizkey from user_saved_quizkeys where user_saved_quizkeys.username='" +
                        username + "')"
                ).toString());

            } catch (Exception e) {
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
                        "select distinct quizkey, quizname from question where question.quizowner='" + username + "'"
                ).toString());

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }

        }
        return okJSON(Response.Status.FORBIDDEN);
    }


}

