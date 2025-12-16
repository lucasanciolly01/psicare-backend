package br.com.psicare.api.controller;

import br.com.psicare.api.dto.DadosDashboard;
import br.com.psicare.api.dto.DadosDetalhamentoSessao;
import br.com.psicare.api.enums.StatusPaciente;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.repository.AgendamentoRepository;
import br.com.psicare.api.repository.PacienteRepository;
import br.com.psicare.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<DadosDashboard> carregarDashboard(Authentication authentication) {
        // 1. Recupera o ID do token e busca a entidade Usuario completa
        UUID psicologoId = UUID.fromString(authentication.getName());
        Usuario psicologo = usuarioRepository.findById(psicologoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Psicólogo não encontrado"));

        LocalDate hoje = LocalDate.now();

        // 2. Métricas de Pacientes (Usando a entidade Usuario recuperada)
        long totalPacientes = pacienteRepository.countByUsuario(psicologo);
        long pacientesAtivos = pacienteRepository.countByUsuarioAndStatus(psicologo, StatusPaciente.ATIVO);
        long pacientesInativos = pacienteRepository.countByUsuarioAndStatus(psicologo, StatusPaciente.INATIVO);

        // 3. Sessões do Mês Atual
        // Define o início do mês (Dia 1 às 00:00)
        LocalDateTime inicioMes = hoje.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        // Define o fim do mês (Último dia às 23:59:59.999)
        LocalDateTime fimMes = hoje.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);

        long sessoesMesAtual = agendamentoRepository.countSessoesNoPeriodo(psicologoId, inicioMes, fimMes);

        // 4. Lista de Agendamentos de Hoje
        LocalDateTime inicioHoje = hoje.atStartOfDay();
        LocalDateTime fimHoje = hoje.atTime(LocalTime.MAX);

        var agendamentosHojeEntities = agendamentoRepository.findAllByPsicologoIdAndDataHoraBetweenOrderByDataHoraAsc(
                psicologoId, inicioHoje, fimHoje
        );

        // Converte a lista de Entidades para a lista de DTOs
        var listaAgendamentosHoje = agendamentosHojeEntities.stream()
                .map(DadosDetalhamentoSessao::new)
                .toList();

        // 5. Retorna o objeto consolidado
        return ResponseEntity.ok(new DadosDashboard(
                totalPacientes,
                pacientesAtivos,
                pacientesInativos,
                sessoesMesAtual,
                listaAgendamentosHoje
        ));
    }
}