package br.com.brunoeas.notificadormessenger.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RequisicaoWebHookMessengerDTO implements Serializable {

    private String object;

    private List<EntryDTO> entry = new ArrayList<>();

}
