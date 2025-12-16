package br.com.psicare.api.dto;

import br.com.psicare.api.model.Agendamento;
import br.com.psicare.api.enums.StatusSessao;
import br.com.psicare.api.enums.TipoSessao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

public record DadosDetalhamentoAgendamento(
        UUID id,
        UUID pacienteId,
        String pacienteNome,
        LocalDateTime dataHora,
        TipoSessao tipoSessao,
        BigDecimal valorCobrado,
        StatusSessao statusSessao
) {
    public DadosDetalhamentoAgendamento(Agendamento agendamento) {
        this(
                agendamento.getId(),
                agendamento.getPaciente().getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getDataHora(),
                agendamento.getTipoSessao(),
                agendamento.getValorCobrado(),
                agendamento.getStatusSessao()
        );
    }
}