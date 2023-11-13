package br.com.brunoeas.notificadormessenger.config;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.jbosslog.JBossLog;

@Provider
@Priority(Priorities.AUTHENTICATION)
@JBossLog
public class ExceptionHttpInterceptor implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(final Exception e) {
        log.error("Ocorreu um erro inesperado", e);
        return Response.status(400).entity(e.toString()).build();
    }

}
