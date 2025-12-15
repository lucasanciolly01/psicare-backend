package br.com.psicare.api.dto;

import br.com.psicare.api.enums.StatusSessao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DadosCadastroSessao(
        @NotNull
        LocalDate data, // Agora se chama 'data' (não dataInicio)

        String tipo,    // Novo campo

        @NotNull
        StatusSessao statusSessao, // Agora se chama 'statusSessao' (não status)

        String evolucao,
        String anotacoes
) {
}