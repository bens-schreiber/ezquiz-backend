package classes;

import classes.apis.UsersDatabaseQueryRestService;
import classes.apis.QuestionsDatabaseQueryRestService;
import classes.apis.QuizDatabaseQueryRestService;
import classes.etc.Constants;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

/**
 Register your rest services here! ( in registerRestServices() )
 */

public class RestServer {
    public static void main(String[] args) throws Exception {

        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);

        // TODO: Tweak this to only allow UI as an origin
        filterHolder.setInitParameter("allowedOrigins", "*");
        filterHolder.setInitParameter("allowedMethods", "GET, POST, PUT");

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(Constants.API_PATH);
        context.addFilter(filterHolder, "/*", null);

        Server jettyServer = new Server(Constants.JETTY_PORT_NUMBER);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        registerRestServices(jerseyServlet);

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

    private static void registerRestServices(ServletHolder jerseyServlet) {
        /*
                jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                        UserRestService.class.getCanonicalName() + ","
                            + PostRestService.class.getCanonicalName() + ","
                            + RelatednessMatrixRestService.class.getCanonicalName());
         */
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                QuestionsDatabaseQueryRestService.class.getCanonicalName() + ","
                + UsersDatabaseQueryRestService.class.getCanonicalName() + ","
                + QuizDatabaseQueryRestService.class.getCanonicalName()
        );
    }
}