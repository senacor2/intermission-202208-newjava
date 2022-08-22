package com.senacor.intermission.newjava.config;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private final Environment env;

    public DatabaseConfiguration(Environment env) {
        this.env = env;
    }

    /**
     * Open the TCP port for the H2 database, so it is available remotely.
     *
     * @return the H2 database TCP server.
     * @throws SQLException if the server failed to start.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Object h2TcpServer() throws SQLException {
        Integer port = getValidPortForH2();
        if (!isPortAvailable(port)) {
            log.warn("Couldn't expose H2 database at port {}! Port already in use", port);
            return null;
        }
        Object server = createH2Server(String.valueOf(port));
        log.info("H2 database is available on port {}", port);
        return server;
    }

    private Integer getValidPortForH2() {
        int port = Integer.parseInt(env.getProperty("server.port"));
        if (port < 10000) {
            port = 10000 + port;
        } else {
            if (port < 63536) {
                port = port + 2000;
            } else {
                port = port - 2000;
            }
        }
        return port;
    }

    private boolean isPortAvailable(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            // We were able to connect to the port, so something must be running there
            return false;
        } catch (IOException ignored) {
            // We were not able to connect, so the port is probably free
            return true;
        }
    }

    private Object createH2Server(String port) throws SQLException {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> serverClass = Class.forName("org.h2.tools.Server", true, loader);
            Method createServer = serverClass.getMethod("createTcpServer", String[].class);
            return createServer.invoke(null, (Object) new String[] {"-tcp", "-tcpAllowOthers", "-tcpPort", port});
        } catch (LinkageError | ClassNotFoundException ex) {
            throw new RuntimeException("Failed to load and initialize org.h2.tools.Server", ex);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException("Failed to get method org.h2.tools.Server.createTcpServer()", ex);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException("Failed to invoke org.h2.tools.Server.createTcpServer()", ex);
        } catch (InvocationTargetException ex) {
            Throwable targetException = ex.getTargetException();
            if (targetException instanceof SQLException) {
                throw (SQLException) targetException;
            } else {
                throw new RuntimeException("Unchecked exception in org.h2.tools.Server.createTcpServer()", targetException);
            }
        }
    }

}
