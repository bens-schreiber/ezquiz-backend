package apis;

import apis.pojo.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.QueryExecutor;
import etc.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Random;

//Quiz related POST/GET methods.
@Produces(MediaType.APPLICATION_JSON)
@Path("quiz")
public class QuizDatabaseQueryRestService extends RestService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    //Insert question into database
    public Response addQuiz(String json, @Context HttpHeaders headers) {

        if (validate(headers)) {

            try {

                //Turn the string sent by the software into a JSON object
                JSONObject consumedJSON = new JSONObject(json);

                //Generate a quiz key
                //todo: handle collisions?
                int quizKey = new Random().nextInt(10000);

                //Grab the quiz JSON from the consumedJSON
                JSONObject quiz = (JSONObject) consumedJSON.get("quiz");
                JSONObject preferences = (JSONObject) consumedJSON.get("preferences");

                //Execute the SQL Query
                //Use valid boolean to error handle sql.
                boolean valid;
                valid = QueryExecutor.executeUpdateQuery("insert into quizzes values("
                        + quizKey + ","
                        + "'" + quiz.get("quizname") + "',"
                        + "'" + quiz.get("quizowner") + "',"
                        + preferences.get("calculator") + ","
                        + preferences.get("answers") + ","
                        + preferences.get("notepad") + ","
                        + preferences.get("drawingpad") + ")"
                ) == 1;

                //If query executed properly
                if (valid) {

                    //Grab the question JSON from the consumedJSON
                    JSONArray jsonQuestions = new JSONArray(consumedJSON.get("questions").toString());

                    //Using ObjectMapper, turn the jsonQuestions JSONArray into a list of Question pojo's.
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                    List<Question> questions = objectMapper.readValue(jsonQuestions.toString(), new TypeReference<>() {});

                    //Create a random seed for the question ID and increase linearly for every id
                    int question_id = new Random().nextInt(100000);
                    for (Question question : questions) {
                        valid = QueryExecutor.executeUpdateQuery("insert into question values ("
                                + question_id++ + ", "
                                + quizKey + ", "
                                + "'" + question.getQuestion() + "', "
                                + "'" + question.getAnswer() + "', "
                                + "'" + question.getType() + "', "
                                + "'" + question.getSubject() + "', "
                                + "'" + question.getOptions() + "', "
                                + "'" + question.getDirections() + "')"
                        ) == 1;
                    }
                    if (valid) return okJSON(Response.Status.ACCEPTED);
                }

                //Return no content on sql failure
                return okJSON(Response.Status.NO_CONTENT);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return okJSON(Response.Status.UNAUTHORIZED);

    }

    @GET
    @Path("{quizKey}")
    //Grab all questions from the Quiz Key, without displaying answers
    public Response getQuiz(@PathParam("quizKey") String quizKey, @Context HttpHeaders headers) {

        if (validate(headers)) {
            try {

                //Assemble response
                JSONObject response = new JSONObject();

                //Get all questions
                response.put("questions", new JSONObject(QueryExecutor.runQuery(
                        Constants.NO_ANSWER_QUERY + " where quizkey=" + quizKey
                ).toString()));

                response.put("preferences", new JSONObject(QueryExecutor.runQuery(
                        "select answers, notepad, calculator, drawingpad from quizzes where quizkey=" + quizKey
                ).toString()));

                return okJSON(Response.Status.ACCEPTED, response.toString());

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }
        }

        return okJSON(Response.Status.UNAUTHORIZED);
    }

    @DELETE
    @Path("{quizkey}")
    //Delete a quiz along with all quiz related user data.
    public Response deleteCreatedQuiz(@PathParam("quizkey") int quizkey, @Context HttpHeaders headers) {
        if(validate(headers)) {
            try {

                boolean valid = QueryExecutor.executeUpdateQuery("delete from quizzes where quizkey=" + quizkey)
                        > 0;
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
    @Path("key/{id}")
    //Determine if a given key exists.
    public Response getKeyFromDatabase(@PathParam("id") String id, @Context HttpHeaders headers) {
        if (validate(headers)) {
            try {

                boolean keyExits = QueryExecutor.runQuery(
                        "select quizkey from quizzes where quizkey=" + id).length() > 0;

                if (keyExits) return okJSON(Response.Status.ACCEPTED);

                return okJSON(Response.Status.NO_CONTENT);

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }

        }
        return okJSON(Response.Status.UNAUTHORIZED);
    }

}
