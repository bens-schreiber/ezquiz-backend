package apis;

import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("helloworld")
public class HelloWorldRestService extends RestService {
    @GET
    public Response getHelloWorld() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("msg", "hello world");
        return okJSON(Response.Status.ACCEPTED, jsonObject.toString());
    }
}
