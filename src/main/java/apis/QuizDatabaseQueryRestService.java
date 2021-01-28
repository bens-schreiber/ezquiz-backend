package apis;

import apis.pojo.Question;
import apis.pojo.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.QueryExecutor;
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
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                List<Question> questions = objectMapper.readValue(json, new TypeReference<>() {
                });

                //todo: collisions could occur with this, handle them
                int id = new Random().nextInt(100000);
                int quizKey = new Random().nextInt(10000);

                boolean valid;
                for (Question question : questions) {
                    valid = QueryExecutor.executeUpdateQuery("insert into question values ("
                            + id + ", "
                            + quizKey + ", "
                            + "'" + question.getQuizowner() + "', "
                            + "'" + question.getQuizname() + "', "
                            + "'" + question.getQuestion() + "', "
                            + "'" + question.getAnswer() + "', "
                            + "'" + question.getType() + "', "
                            + "'" + question.getSubject() + "', "
                            + "'" + question.getOptions() + "', "
                            + "'" + question.getDirections() +"')"
                    ) == 1;

                    if (!valid) {

                        //Delete all values from sql.
                        QueryExecutor.executeUpdateQuery(
                                "delete from question where question.quizowner='" + question.getQuizowner() + "'" +
                                        " and question.quizname='" + question.getQuizname() + "'");
                        return okJSON(Response.Status.NO_CONTENT);
                    }

                    id++;
                }

                return okJSON(Response.Status.ACCEPTED);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return okJSON(Response.Status.UNAUTHORIZED);

    }

    @DELETE
    @Path("quizzes/{quizkey}")
    //Delete a quiz along with all quiz related user data.
    public Response deleteCreatedQuiz(@PathParam("quizkey") int quizkey, @Context HttpHeaders headers) {
        if(validate(headers)) {
            try {

                boolean valid = QueryExecutor.executeUpdateQuery("delete from question where quizkey=" + quizkey)
                        + QueryExecutor.executeUpdateQuery("delete from user_saved_quizkeys where quizkey=" + quizkey)
                        + QueryExecutor.executeUpdateQuery("delete from user_quiz_scores where quizkey=" + quizkey) > 2;

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
                        "select * from question where quizkey=" + id).length() > 0;

                if (keyExits) return okJSON(Response.Status.ACCEPTED);

                return okJSON(Response.Status.NO_CONTENT);

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }

        }
        return okJSON(Response.Status.UNAUTHORIZED);
    }

}
