package apis;

import apis.pojo.TestKey;
import database.LoggedInUsers;
import database.QueryExecutor;
import etc.Constants;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Path("database")
public class QuestionsDatabaseQueryRestService extends RestService {

    @GET
    @Path("questions")
    public Response getQuestions(@Context HttpHeaders headers) {

        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select * from question").toString());
            } catch (Exception e) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }
        }
        return okJSON(Response.Status.FORBIDDEN);
    }


    @GET
    @Path("questions/type/{type}")
    public Response getQuestionsByType(@PathParam("type") String type, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(Constants.NO_ANSWER_QUERY + " and _type.type_name='" + type + "'").toString());
            } catch (Exception e) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }
        }
        return okJSON(Response.Status.FORBIDDEN);
    }


    @GET
    @Path("questions/subject/{subject}")
    public Response getQuestionsBySubject(@PathParam("subject") String subject, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(Constants.NO_ANSWER_QUERY + " and _subject.subject_name='" + subject + "'").toString());
            } catch (Exception e) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }
        }
        return okJSON(Response.Status.FORBIDDEN);
    }


    @GET
    @Path("questions/{subject}/{type}")
    public Response getQuestionsByTypeAndSubject(@PathParam("subject") String subject, @PathParam("type") String type, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery
                        (Constants.NO_ANSWER_QUERY + " and _subject.subject_name='" + subject + "' and _type.type_name='" + type + "'").toString());

            } catch (Exception e) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }
        }
        return okJSON(Response.Status.FORBIDDEN);
    }

    @GET
    @Path("questions/{id}")
    public Response getQuestionByID(@PathParam("id") String id, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(Constants.NO_ANSWER_QUERY + " and question_num='" + id + "'").toString());
            } catch (Exception e) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }
        }
        return okJSON(Response.Status.FORBIDDEN);

    }

    @GET
    @Path("questions/answer/{id}")
    public Response getQuestionAnswerByID(@PathParam("id") String id, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select answer from question where question_num='" + id + "'").toString());
            } catch (Exception e) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }
        }
        return okJSON(Response.Status.FORBIDDEN);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("key")
    public Response addTestKey(TestKey testKey,  @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {

                int rand = new Random().nextInt(10000);

                boolean keyAdded = QueryExecutor.executeUpdateQuery(
                        "insert into test_keys(test_key, bitmap) values('"
                                + String.format("%04d", rand) + "'"
                                + ",'" + testKey.getKey() + "')") == 1;

                if (keyAdded) {
                    JSONObject a = new JSONObject();
                    a.put("key", rand);
                    return okJSON(Response.Status.ACCEPTED, a.toString());
                } else {
                    return okJSON(Response.Status.UNAUTHORIZED);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return okJSON(Response.Status.UNAUTHORIZED);
            }

        }
        return okJSON(Response.Status.FORBIDDEN);

    }

    @GET
    @Path("key/{id}")
    public Response getKeyFromDatabase(@PathParam("id") String id,  @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select bitmap from test_keys where test_keys.test_key=" + id).toString());
            } catch (Exception e) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }

        }
        return okJSON(Response.Status.FORBIDDEN);
    }

    private static boolean validate(HttpHeaders headers) {
        return LoggedInUsers.getLoggedInUsers().containsValue(String.valueOf(headers.getRequestHeader("token").get(0)));
    }
}
