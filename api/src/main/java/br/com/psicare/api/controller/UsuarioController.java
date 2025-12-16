package br.com.psicare.api.controller;

import br.com.psicare.api.dto.DadosCadastroUsuario;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        // Encripta a senha antes de salvar
        var senhaHash = passwordEncoder.encode(dados.senha());

        // Gera iniciais simples (Pega as 2 primeiras letras)
        var iniciais = dados.nome().substring(0, Math.min(2, dados.nome().length())).toUpperCase();

        var usuario = new Usuario(dados.nome(), dados.email(), senhaHash, dados.telefone(), iniciais);
        repository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(usuario); // TODO: Retornar DTO de resposta, não a Entidade! (Corrigiremos no refinamento)
    }
}