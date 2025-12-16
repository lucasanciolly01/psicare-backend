package br.com.psicare.api.controller;

import br.com.psicare.api.dto.DadosAtualizacaoUsuario; // Import necessário
import br.com.psicare.api.dto.DadosCadastroUsuario;
import br.com.psicare.api.model.Usuario; // Import necessário para @AuthenticationPrincipal
import br.com.psicare.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        var dto = service.cadastrar(dados);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados, @AuthenticationPrincipal Usuario usuarioLogado) {
        var dto = service.atualizar(dados, usuarioLogado);
        return ResponseEntity.ok(dto);
    }
}