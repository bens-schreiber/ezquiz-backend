package apis;

import apis.pojo.Question;
import apis.pojo.TestKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.LoggedInUsers;
import database.QueryExecutor;
import etc.Constants;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Random;

@Produces(MediaType.APPLICATION_JSON)
@Path("database")
public class QuestionsDatabaseQueryRestService extends RestService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("questions")
    public Response addQuiz(String json, @Context HttpHeaders headers) {

        if (validate(headers)) {

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                List<Question> questions = objectMapper.readValue(json, new TypeReference<>() {
                });

                int id = 0;
                for (Question question : questions) {
                    QueryExecutor.executeUpdateQuery("insert into question(id, quizowner, quizname, question, answer, type, subject, options, directions) values ("
                            + id + ", "
                            + "'" + question.getQuizowner() + "', "
                            + "'" + question.getQuizname() + "', "
                            + "'" + question.getQuestion() + "', "
                            + "'" + question.getAnswer() + "', "
                            + "'" + question.getType() + "', "
                            + "'" + question.getSubject() + "', "
                            + "'" + question.getOptions() + "', "
                            + "'" + question.getDirections() +"')"
                    ); id++;
                }

                int rand = new Random().nextInt(10000);
                Question question  = questions.get(0);

                QueryExecutor.executeUpdateQuery("insert into quiz_keys values (" + rand + ","
                        + "'" + question.getQuizname() + "',"
                        + "'" + question.getQuizowner() + "')"
                );

                return okJSON(Response.Status.ACCEPTED);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return okJSON(Response.Status.UNAUTHORIZED);

    }

    @GET
    @Path("key/{id}")
    public Response getKeyFromDatabase(@PathParam("id") String id, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select quizowner, quizname from quiz_keys where quiz_keys.quizkey=" + id).toString());

            } catch (Exception e) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }

        }
        return okJSON(Response.Status.FORBIDDEN);
    }

    @GET
    @Path("questions/{quizOwner}/{quizName}")
    public Response getQuestions(@PathParam("quizOwner") String quizOwner,
                                       @PathParam("quizName") String quizName,
                                       @Context HttpHeaders headers) {

        if (validate(headers)) {
            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        Constants.NO_ANSWER_QUERY + " where question.quizOwner='" + quizOwner + "'" + " and question.quizName='" + quizName + "'")
                        .toString());

            } catch (Exception e) {

                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }
        }

        return okJSON(Response.Status.FORBIDDEN);
    }

    @GET
    @Path("questions/{quizOwner}/{quizName}/{ids}")
    public Response getQuestionsFromSeveralIds(@PathParam("ids") String ids,
                                               @PathParam("quizOwner") String quizOwner,
                                               @PathParam("quizName") String quizName,
                                               @Context HttpHeaders headers) {

        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        Constants.NO_ANSWER_QUERY + " where question.quizOwner='" + quizOwner + "'" + "' and question.quizName='" + quizName + "'" +
                                " and question_num in (" + ids + ")")
                        .toString());

            } catch (Exception e) {

                JSONObject errorJson = new JSONObject();
                errorJson.put("msg", "failure whale");
                return okJSON(Response.Status.ACCEPTED, errorJson.toString());
            }
        }

        return okJSON(Response.Status.FORBIDDEN);
    }

    @GET
    @Path("questions/answer/{quizOwner}/{quizName}/{ids}")
    public Response getQuestionAnswerByID(@PathParam("ids") String ids, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select answer, id from question where question.id in (" + ids + ")").toString());
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
    @Path("code")
    public Response addRetakeCode(TestKey testKey, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {

                int rand = new Random().nextInt(10000);

                boolean keyAdded = QueryExecutor.executeUpdateQuery(
                        "insert into retake_codes(code, bitmap) values('"
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
    @Path("code/{id}")
    public Response getCodeFromDatabase(@PathParam("id") String id, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select bitmap from retake_codes where retake_codes.code=" + id).toString());
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
