package br.com.brunoeas.notificadormessenger;

import br.com.brunoeas.notificadormessenger.dtos.EnviarMensagemMessengerDTO;
import br.com.brunoeas.notificadormessenger.dtos.MessageToSendDTO;
import br.com.brunoeas.notificadormessenger.dtos.RecipientDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@JBossLog
@ServerEndpoint("/messenger/{senderId}")
@ApplicationScoped
public class MessengerWebSocketServer {

    @RestClient
    @Inject
    MessengerRestClient messengerRestClient;

    @ConfigProperty(name = "api.page-access-token")
    String accessToken;

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(final Session session, @PathParam("senderId") final String username) {
        this.enviaMensagemParaTodosNoChat("User " + username + " joined");
        this.sessions.put(username, session);
    }

    @OnClose
    public void onClose(final Session session, @PathParam("senderId") final String username) {
        this.sessions.remove(username);
        this.enviaMensagemParaTodosNoChat("User " + username + " left");
    }

    @OnError
    public void onError(final Session session, @PathParam("senderId") final String username, final Throwable throwable) {
        this.sessions.remove(username);
        this.enviaMensagemParaTodosNoChat("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(final String message, @PathParam("senderId") final String username) {
        this.enviaMensagemParaTodosNoChat(">> " + username + ": " + message);

        final EnviarMensagemMessengerDTO body = EnviarMensagemMessengerDTO.builder()
                .message(MessageToSendDTO.builder().text(message).build())
                .recipient(RecipientDTO.builder()
                        .id(username)
                        .build())
                .build();
        this.messengerRestClient.enviaMensagem(this.accessToken, body);
    }

    private void enviaMensagemParaTodosNoChat(final String message) {
        this.sessions.values().forEach(session -> this.enviaMensagem(message, session));
    }

    private void enviaMensagem(final String message, final Session session) {
        session.getAsyncRemote().sendObject(message, result ->  {
            if (Objects.nonNull(result.getException())) {
                log.error("Erro ao enviar mensagem", result.getException());
            } else {
                log.info("Mensagem enviada com sucesso.");
            }
        });
    }

}
