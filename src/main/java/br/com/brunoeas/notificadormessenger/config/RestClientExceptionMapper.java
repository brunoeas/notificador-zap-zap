package br.com.brunoeas.notificadormessenger.config;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import java.io.ByteArrayInputStream;

@Priority(Priorities.USER)
public class RestClientExceptionMapper implements ResponseExceptionMapper<Exception> {

    @Override
    public Exception toThrowable(final Response response) {
        try {
            response.bufferEntity();
        } catch (final Exception ignored) {
        }
        final String msg = this.getBody(response);
        return new Exception(msg);
    }

    @Override
    public boolean handles(final int status, final MultivaluedMap<String, Object> headers) {
        return status >= 400;
    }

    private String getBody(final Response response) {
        final ByteArrayInputStream is = (ByteArrayInputStream) response.getEntity();
        final byte[] bytes = new byte[is.available()];
        is.read(bytes, 0, is.available());
        return new String(bytes);
    }
}
