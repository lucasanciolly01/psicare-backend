package br.com.psicare.api.controller;

import br.com.psicare.api.dto.DadosCadastroAgendamento;
import br.com.psicare.api.dto.DadosDetalhamentoAgendamento;
import br.com.psicare.api.services.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    /**
     * POST /agendamentos: Agenda uma nova sessão.
     */
    @PostMapping
    public ResponseEntity<DadosDetalhamentoAgendamento> agendar(
            @RequestBody @Valid DadosCadastroAgendamento dados,
            Authentication authentication,
            UriComponentsBuilder uriBuilder) {

        // O ID do psicólogo logado é recuperado do objeto Authentication (JWT)
        UUID psicologoId = UUID.fromString(authentication.getName());

        var agendamento = agendamentoService.agendar(dados, psicologoId);

        var uri = uriBuilder.path("/agendamentos/{id}").buildAndExpand(agendamento.id()).toUri();
        return ResponseEntity.created(uri).body(agendamento);
    }

    /**
     * GET /agendamentos?inicio=...&fim=...: Lista os agendamentos do psicólogo logado.
     */
    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoAgendamento>> listar(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        UUID psicologoId = UUID.fromString(authentication.getName());

        var agendamentos = agendamentoService.listarAgenda(psicologoId, inicio, fim);

        return ResponseEntity.ok(agendamentos);
    }
}