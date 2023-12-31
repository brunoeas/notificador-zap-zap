package br.com.brunoeas.notificadormessenger;

import br.com.brunoeas.notificadormessenger.config.ExceptionHttpInterceptor;
import br.com.brunoeas.notificadormessenger.config.RestClientExceptionMapper;
import br.com.brunoeas.notificadormessenger.dtos.EnviarMensagemMessengerDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient(configKey = "resposta-messenger-api")
@RegisterProvider(value = RestClientExceptionMapper.class)
@RegisterProvider(value = ExceptionHttpInterceptor.class)
public interface MessengerRestClient {

    @POST
    @Path("/v2.6/me/messages")
    void enviaMensagem(@QueryParam("access_token") final String accessToken, final EnviarMensagemMessengerDTO body);

}
