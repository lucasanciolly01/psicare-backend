package br.com.psicare.api.dto;

import br.com.psicare.api.enums.FrequenciaSessao;
import br.com.psicare.api.enums.StatusPaciente;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record DadosAtualizacaoPaciente(
        @NotNull
        UUID id,

        String nome,
        String telefone,
        String email,

        LocalDate dataNascimento, // <--- ADICIONADO: Faltava este campo aqui!

        String queixaPrincipal,
        String historicoFamiliar,
        String observacoesIniciais,
        String anotacoes,

        StatusPaciente status,
        FrequenciaSessao frequenciaSessao,
        String avatarColor
) {
}