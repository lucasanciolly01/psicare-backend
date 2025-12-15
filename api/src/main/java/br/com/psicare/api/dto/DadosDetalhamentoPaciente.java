package br.com.psicare.api.dto;

import br.com.psicare.api.enums.FrequenciaSessao;
import br.com.psicare.api.enums.StatusPaciente;
import br.com.psicare.api.model.Paciente;
import java.time.LocalDate;
import java.util.UUID;

public record DadosDetalhamentoPaciente(
        UUID id,
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento,
        String queixaPrincipal,
        String historicoFamiliar,
        String observacoesIniciais,

        String anotacoes, // <--- ADICIONADO AQUI

        StatusPaciente status,
        FrequenciaSessao frequenciaSessao,
        String avatarColor
) {
    public DadosDetalhamentoPaciente(Paciente paciente) {
        this(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getDataNascimento(),
                paciente.getQueixaPrincipal(),
                paciente.getHistoricoFamiliar(),
                paciente.getObservacoesIniciais(),

                paciente.getAnotacoes(), // <--- ADICIONADO AQUI

                paciente.getStatus(),
                paciente.getFrequenciaSessao(),
                paciente.getAvatarColor()
        );
    }
}