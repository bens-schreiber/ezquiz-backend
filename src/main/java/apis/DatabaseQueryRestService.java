package apis;

import database.QueryExecutor;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("database")
public class DatabaseQueryRestService extends RestService {
    @GET
    @Path("test")
    public Response test() {
        try {
            return okJSON(Response.Status.ACCEPTED, QueryExecutor.runQuery("select * from question").toString());
        }
        catch (Exception e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("msg", "failure whale");
            return okJSON(Response.Status.ACCEPTED, errorJson.toString());
        }
    }
}
