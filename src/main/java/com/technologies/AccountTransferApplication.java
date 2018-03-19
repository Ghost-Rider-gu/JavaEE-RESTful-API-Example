package com.technologies;

import com.technologies.config.DbConfig;
import com.technologies.resource.AccountResource;
import com.technologies.resource.UserResource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Application entry point.
 */
@Slf4j
public class AccountTransferApplication {

    public static void main(String[] args) throws Exception {
        log.info("Start application ...");

        DbConfig.getConnection();
        startServer();
    }

    /**
     * Config server
     *
     * @throws Exception
     */
    private static void startServer() throws Exception {
        Server server = new Server(8088);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                UserResource.class.getCanonicalName() + "," + AccountResource.class.getCanonicalName());
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

}
