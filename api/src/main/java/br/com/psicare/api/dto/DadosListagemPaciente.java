package br.com.psicare.api.dto;

import br.com.psicare.api.model.Paciente;
import br.com.psicare.api.enums.StatusPaciente;
import java.util.UUID;

public record DadosListagemPaciente(UUID id, String nome, String email, String telefone, StatusPaciente status) {
    public DadosListagemPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone(), paciente.getStatus());
    }
}