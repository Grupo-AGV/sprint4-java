package com.penaestrada;

import com.penaestrada.infra.security.CorsFilter;
import com.penaestrada.infra.security.SecurityFilter;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class App extends ResourceConfig {

    public App() {
        packages("com.penaestrada");
        register(CorsFilter.class);
        register(SecurityFilter.class);
    }
}
