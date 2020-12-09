package apis;

import database.QueryExecutor;
import etc.Constants;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("database")
public class QuestionsDatabaseQueryRestService extends RestService {

    @GET
    @Path("questions")
    public Response getQuestions() {
        try {
            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select * from question").toString());
        } catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }
    }


    @GET
    @Path("questions/type/{type}")
    public Response getQuestionsByType(@PathParam("type") String type) {
        try {
            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(Constants.NO_ANSWER_QUERY + " and _type.type_name='" + type + "'").toString());
        } catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }
    }


    @GET
    @Path("questions/subject/{subject}")
    public Response getQuestionsBySubject(@PathParam("subject") String subject) {
        try {
            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(Constants.NO_ANSWER_QUERY + " and _subject.subject_name='" + subject + "'").toString());
        } catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }
    }


    @GET
    @Path("questions/{subject}/{type}")
    public Response getQuestionsByTypeAndSubject(
            @PathParam("subject") String subject,
            @PathParam("type") String type) {
        try {

            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery
                    (Constants.NO_ANSWER_QUERY + " and _subject.subject_name='" + subject + "' and _type.type_name='" + type + "'").toString());

        } catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }
    }

    @GET
    @Path("questions/{id}")
    public Response getQuestionByID(@PathParam("id") String id) {
        try {
            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(Constants.NO_ANSWER_QUERY + " and question_num='" + id + "'").toString());
        } catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }

    }

    @GET
    @Path("questions/answer/{id}")
    public Response getQuestionAnswerByID(@PathParam("id") String id) {
        try {
            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select answer from question where question_num='" + id + "'").toString());
        } catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }
    }
}
