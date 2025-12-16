package br.com.psicare.api.dto;

import java.util.List;

public record DadosDashboard(
        long totalPacientes,
        long pacientesAtivos,
        long pacientesInativos,
        long sessoesMesAtual,
        List<DadosDetalhamentoSessao> agendamentosHoje
) {}