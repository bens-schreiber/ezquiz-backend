package apis;

import database.QueryExecutor;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("database")
public class DatabaseQueryRestService extends RestService {
    @GET
    @Path("questions")
    public Response getQuestions() {
        try {
            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select * from question, _type").toString());
        }
        catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }
    }


    @GET
    @Path("questions/type/{type}")
    public Response getQuestionsByType(@PathParam("type") String type) {
        try {
            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select * from question, _type where question.type_id=_type.id and _type.type_name='" + type + "'" ).toString());
        }
        catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }
    }
}
