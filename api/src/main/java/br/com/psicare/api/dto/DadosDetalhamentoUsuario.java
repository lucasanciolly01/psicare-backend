package br.com.psicare.api.dto;

import br.com.psicare.api.model.Usuario;
import java.util.UUID;

public record DadosDetalhamentoUsuario(UUID id, String nome, String email, String iniciais) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getIniciais());
    }
}