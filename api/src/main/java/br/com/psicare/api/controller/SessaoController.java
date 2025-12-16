package br.com.psicare.api.controller;

import br.com.psicare.api.dto.DadosCadastroSessao;
import br.com.psicare.api.dto.DadosDetalhamentoSessao;
import br.com.psicare.api.model.Sessao;
import br.com.psicare.api.repository.PacienteRepository;
import br.com.psicare.api.repository.SessaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes/{pacienteId}/sessoes")
public class SessaoController {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@PathVariable UUID pacienteId, @RequestBody @Valid DadosCadastroSessao dados, UriComponentsBuilder uriBuilder) {
        var paciente = pacienteRepository.getReferenceById(pacienteId);

        var sessao = new Sessao(dados, paciente);
        sessaoRepository.save(sessao);

        var uri = uriBuilder.path("/sessoes/{id}").buildAndExpand(sessao.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoSessao(sessao));
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoSessao>> listar(@PathVariable UUID pacienteId) {
        var sessoes = sessaoRepository.findAllByPacienteIdOrderByDataDesc(pacienteId)
                .stream()
                .map(DadosDetalhamentoSessao::new)
                .toList();

        return ResponseEntity.ok(sessoes);
    }
}