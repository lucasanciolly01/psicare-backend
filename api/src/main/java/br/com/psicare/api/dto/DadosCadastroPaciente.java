package br.com.psicare.api.dto;

import br.com.psicare.api.enums.FrequenciaSessao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DadosCadastroPaciente(
        // Estes continuam OBRIGATÓRIOS
        @NotBlank
        String nome,

        @Email
        String email,

        @NotBlank
        String telefone,

        @NotNull
        LocalDate dataNascimento,

        // REMOVA O @NotBlank DESTES ABAIXO (Eles se tornam opcionais):
        String queixaPrincipal,
        String historicoFamiliar,
        String observacoesIniciais,
        String anotacoes,

        FrequenciaSessao frequenciaSessao,
        String avatarColor
) {
}