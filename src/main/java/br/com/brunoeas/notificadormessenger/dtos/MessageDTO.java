package br.com.brunoeas.notificadormessenger.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class MessageDTO implements Serializable {

    private String mid;

    private String text;

}
