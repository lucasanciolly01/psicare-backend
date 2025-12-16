package br.com.psicare.api.controller;

import br.com.psicare.api.dto.DadosAtualizacaoPaciente;
import br.com.psicare.api.dto.DadosCadastroPaciente;
import br.com.psicare.api.dto.DadosDetalhamentoPaciente;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.services.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dados,
                                    UriComponentsBuilder uriBuilder,
                                    @AuthenticationPrincipal Usuario usuarioLogado) {

        var dto = service.cadastrar(dados, usuarioLogado);
        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(dto.id()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoPaciente>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        var page = service.listar(paginacao, usuarioLogado);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable UUID id) {
        var dto = service.detalhar(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
        var dto = service.atualizar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}