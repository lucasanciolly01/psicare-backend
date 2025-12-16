package br.com.psicare.api.controller;

import br.com.psicare.api.dto.DadosDashboard;
import br.com.psicare.api.enums.StatusPaciente;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.repository.PacienteRepository;
import br.com.psicare.api.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @GetMapping("/resumo")
    public ResponseEntity<DadosDashboard> resumo(@AuthenticationPrincipal Usuario usuarioLogado) {

        // 1. Busca dados de pacientes
        var totalPacientes = pacienteRepository.countByUsuario(usuarioLogado);
        var ativos = pacienteRepository.countByUsuarioAndStatus(usuarioLogado, StatusPaciente.ATIVO);
        var inativos = totalPacientes - ativos; // Simplificação matemática

        // 2. Busca sessões do mês atual
        var hoje = LocalDate.now();
        var inicioMes = YearMonth.from(hoje).atDay(1);
        var fimMes = YearMonth.from(hoje).atEndOfMonth();

        var sessoesMes = sessaoRepository.contarSessoesNoPeriodo(usuarioLogado, inicioMes, fimMes);

        return ResponseEntity.ok(new DadosDashboard(
                totalPacientes,
                ativos,
                inativos,
                sessoesMes
        ));
    }
}