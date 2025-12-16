package br.com.psicare.api.repository;

import br.com.psicare.api.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    // Verifica conflito de horário exato
    boolean existsByPsicologoIdAndDataHora(UUID psicologoId, LocalDateTime dataHora);

    // Busca agendamentos em um intervalo (usado na Agenda e no Dashboard para buscar a lista de hoje)
    List<Agendamento> findAllByPsicologoIdAndDataHoraBetweenOrderByDataHoraAsc(UUID psicologoId, LocalDateTime inicio, LocalDateTime fim);

    // === MÉTODOS DO DASHBOARD ===

    // Conta sessões em um período específico (ex: Mês Atual), ignorando as canceladas
    @Query("""
        SELECT COUNT(a) FROM Agendamento a 
        WHERE a.psicologo.id = :psicologoId 
        AND a.dataHora BETWEEN :inicio AND :fim 
        AND a.statusSessao != 'CANCELADO'
    """)
    long countSessoesNoPeriodo(UUID psicologoId, LocalDateTime inicio, LocalDateTime fim);
}