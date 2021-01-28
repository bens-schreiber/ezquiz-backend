package apis;

import database.QueryExecutor;
import etc.Constants;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//GET methods for grabbing Questions & Answers from the database.
@Produces(MediaType.APPLICATION_JSON)
@Path("questions")
public class QuestionsDatabaseQueryRestService extends RestService {

    @GET
    @Path("{quizKey}")
    //Grab all questions from the Quiz Owner with the Quiz Name, without displaying answers
    public Response getQuestions(@PathParam("quizKey") String quizKey, @Context HttpHeaders headers) {

        if (validate(headers)) {
            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        Constants.NO_ANSWER_QUERY + " where question.quizkey = " + quizKey
                ).toString());

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }
        }

        return okJSON(Response.Status.UNAUTHORIZED);
    }

    @GET
    @Path("{quizKey}/{ids}")
    //Grab questions from quiz key
    public Response getQuestionsFromSeveralIds(@PathParam("ids") String ids, @PathParam("quizKey") String quizKey, @Context HttpHeaders headers) {

        if (validate(headers)) {

            try {
                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        Constants.NO_ANSWER_QUERY + " where question.quizkey = " + quizKey
                                + " and id in (" + ids + ")"
                ).toString());

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }
        }

        return okJSON(Response.Status.UNAUTHORIZED);
    }

    @GET
    @Path("answer/{ids}")
    //Grab all question answers from the Quiz Owner with the Quiz Name and the given ids.
    public Response getQuestionAnswerByID(@PathParam("ids") String ids, @Context HttpHeaders headers) {

        if (validate(headers)) {

            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        "select answer, id from question where id in (" + ids + ")"
                ).toString());

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }
        }

        return okJSON(Response.Status.UNAUTHORIZED);
    }

}
