package br.com.brunoeas.notificadormessenger.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessagingDTO implements Serializable {

    private SenderDTO sender;

    private MessageDTO message;

    private Long timestamp;

}
