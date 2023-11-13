package br.com.brunoeas.notificadormessenger;

import br.com.brunoeas.notificadormessenger.dtos.EnviarMensagemMessengerDTO;
import br.com.brunoeas.notificadormessenger.dtos.MessageToSendDTO;
import br.com.brunoeas.notificadormessenger.dtos.RecipientDTO;
import br.com.brunoeas.notificadormessenger.dtos.RequisicaoWebHookMessengerDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@JBossLog
@Path("/webhook")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class WebHookMessengerResource {

    @RestClient
    @Inject
    MessengerRestClient messengerRestClient;

    @ConfigProperty(name = "api.page-access-token")
    String accessToken;

    @POST
    public Response webhookMessenger(final RequisicaoWebHookMessengerDTO requisicao) {
        log.infof("Requisição recebida no webhook do Messenger:%n%s%n", requisicao);

        final String mensagemRecebida = requisicao.getEntry().iterator().next().getMessaging().iterator().next().getMessage().getText();
        log.infof("Mensagem recebida no Messenger: \"%s\"%n", mensagemRecebida);

        final String senderID = requisicao.getEntry().iterator().next().getMessaging().iterator().next().getSender().getId();
        final String mensagemDeResposta = "Mensagem de resposta para a mensagem: \"" + mensagemRecebida + "\"";

        final EnviarMensagemMessengerDTO body = EnviarMensagemMessengerDTO.builder()
                .message(MessageToSendDTO.builder().text(mensagemDeResposta).build())
                .recipient(RecipientDTO.builder()
                        .id(senderID)
                        .build())
                .build();

        log.infof("Enviando a mensagem \"%s\" para o ID \"%s\"%n", mensagemDeResposta, senderID);
        this.messengerRestClient.enviaMensagem(this.accessToken, body);

        return Response.ok("EVENT_RECEIVED").build();
    }

    @Consumes(MediaType.TEXT_PLAIN)
    @POST
    @Path("/envia-mensagem/{senderId}")
    public void enviaMensagem(@PathParam("senderId") final String senderID, final String mensagem) {
        final EnviarMensagemMessengerDTO body = EnviarMensagemMessengerDTO.builder()
                .message(MessageToSendDTO.builder().text(mensagem).build())
                .recipient(RecipientDTO.builder()
                        .id(senderID)
                        .build())
                .build();

        log.infof("Enviando a mensagem \"%s\" para o ID \"%s\"%n", mensagem, senderID);
        this.messengerRestClient.enviaMensagem(this.accessToken, body);
    }

    @GET
    public Response healthCheck(
            @QueryParam("hub.challenge") final String challenge,
            @QueryParam("hub.mode") final String mode,
            @QueryParam("hub.verify_token") final String verifyToken) {

        log.info("HealthCheck");
        log.infof("Challenge: %s", challenge);
        log.infof("Mode: %s", mode);
        log.infof("VerifyToken: %s", verifyToken);

        return Response.ok(challenge).build();
    }
}
