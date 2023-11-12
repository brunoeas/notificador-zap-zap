package br.com.brunoeas.notificadormessenger.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EntryDTO implements Serializable {

    private List<MessagingDTO> messaging = new ArrayList<>();

    private String id;

    private Long time;

}
