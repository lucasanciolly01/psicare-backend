package br.com.psicare.api.controller;

import br.com.psicare.api.dto.*;
import br.com.psicare.api.enums.StatusPaciente; // Import adicionado
import br.com.psicare.api.model.Paciente;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.repository.PacienteRepository;
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
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dados,
                                    UriComponentsBuilder uriBuilder,
                                    @AuthenticationPrincipal Usuario usuarioLogado) {
        var paciente = new Paciente(dados, usuarioLogado);
        repository.save(paciente);

        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoPaciente>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        // CORREÇÃO: Busca apenas pacientes do usuário logado QUE NÃO ESTEJAM INATIVOS
        var page = repository.findAllByUsuarioAndStatusIsNot(usuarioLogado, StatusPaciente.INATIVO, paginacao)
                .map(DadosDetalhamentoPaciente::new);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable UUID id) {
        // TODO: Validar se o paciente pertence ao usuário logado (Segurança extra)
        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable UUID id) {
        // CORREÇÃO: Soft Delete - Recupera e inativa em vez de deletar
        var paciente = repository.getReferenceById(id);
        paciente.inativar();

        return ResponseEntity.noContent().build();
    }
}