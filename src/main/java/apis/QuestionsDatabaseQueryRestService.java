package apis;

import database.QueryExecutor;

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
    @Path("answer/{ids}")
    //Grab all question answers from ids
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

    @GET
    @Path("answers/{quizKey}")
    //Grab all question answers from quiz key
    public Response getQuestionAnswerKey(@PathParam("quizKey") String quizKey, @Context HttpHeaders headers) {

        if (validate(headers)) {

            try {

                return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery(
                        "select * from question where quizkey=" + quizKey
                ).toString());

            } catch (Exception e) {
                return okJSON(Response.Status.NO_CONTENT);
            }
        }

        return okJSON(Response.Status.UNAUTHORIZED);
    }

}
