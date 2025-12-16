package br.com.psicare.api.dto;

import br.com.psicare.api.enums.TipoSessao;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

public record DadosCadastroAgendamento(
        @NotNull UUID pacienteId,
        @NotNull @Future LocalDateTime dataHora,
        @NotNull TipoSessao tipoSessao,
        @NotNull BigDecimal valorCobrado
) {}