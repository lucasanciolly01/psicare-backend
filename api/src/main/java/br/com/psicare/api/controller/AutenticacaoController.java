package br.com.psicare.api.controller;

import br.com.psicare.api.dto.DadosAutenticacao;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        // 1. Cria o token com login/senha que chegou
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        // 2. O Spring valida (se der erro, ele lança Exception automaticamente)
        var authentication = manager.authenticate(authenticationToken);

        // 3. Recupera o usuário que foi validado
        var usuario = (Usuario) authentication.getPrincipal();

        // 4. Gera o Token JWT para esse usuário
        var tokenJWT = tokenService.gerarToken(usuario);

        // 5. Retorna tudo que o Front precisa (Token + Dados Pessoais)
        return ResponseEntity.ok(new DadosLoginResponse(
                tokenJWT,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getIniciais()
        ));
    }
}

// DTO de Resposta (Criado aqui para facilitar, mas pode ir para o pacote .dto depois)
record DadosLoginResponse(
        String token,
        UUID id,
        String nome,
        String email,
        String iniciais
) {}