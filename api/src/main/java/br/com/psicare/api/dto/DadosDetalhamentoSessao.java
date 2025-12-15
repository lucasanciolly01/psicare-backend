package br.com.psicare.api.dto;

import br.com.psicare.api.enums.StatusSessao;
import br.com.psicare.api.model.Sessao;

import java.time.LocalDate;
import java.util.UUID;

public record DadosDetalhamentoSessao(
        UUID id,
        LocalDate data,
        String tipo,
        StatusSessao statusSessao,
        String evolucao
) {
    public DadosDetalhamentoSessao(Sessao sessao) {
        this(
                sessao.getId(),
                sessao.getData(),         // Pega o novo campo data
                sessao.getTipo(),         // Pega o novo campo tipo
                sessao.getStatusSessao(), // Pega o novo campo statusSessao
                sessao.getEvolucao()
        );
    }
}