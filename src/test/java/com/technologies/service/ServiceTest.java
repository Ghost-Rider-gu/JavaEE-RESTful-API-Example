package com.technologies.service;

import com.technologies.config.DbConfig;
import com.technologies.resource.AccountResource;
import com.technologies.resource.UserResource;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Common part for tests.
 */
public abstract class ServiceTest {

    protected static Server server = null;
    protected static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    protected static HttpClient httpClient;


    @BeforeClass
    public static void setupTest() throws Exception {
        DbConfig.getConnection();
        runServer();
        connectionManager.setDefaultMaxPerRoute(80);
        connectionManager.setMaxTotal(100);
        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setConnectionManagerShared(true).build();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
        HttpClientUtils.closeQuietly(httpClient);
    }

    /**
     * Run server for tests
     *
     * @throws Exception
     */
    private static void runServer() throws Exception {
        if (server == null) {
            server = new Server(8088);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
            servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                    UserResource.class.getCanonicalName() + "," + AccountResource.class.getCanonicalName());
            server.start();
        }
    }

}
