package br.com.psicare.api.dto;

public record DadosDashboard(
        long totalPacientes,
        long pacientesAtivos,
        long pacientesInativos,
        long sessoesMesAtual
) {
}