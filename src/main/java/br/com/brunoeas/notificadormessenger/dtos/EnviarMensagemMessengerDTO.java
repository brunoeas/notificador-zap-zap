package br.com.brunoeas.notificadormessenger.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class EnviarMensagemMessengerDTO implements Serializable {

    private MessageDTO message;

    private RecipientDTO recipient;

}
