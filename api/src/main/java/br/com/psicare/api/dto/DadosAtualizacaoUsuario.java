package br.com.psicare.api.dto;

public record DadosAtualizacaoUsuario(
        String nome,
        String telefone,
        String senha,
        String foto // Novo campo (Base64)
) {}