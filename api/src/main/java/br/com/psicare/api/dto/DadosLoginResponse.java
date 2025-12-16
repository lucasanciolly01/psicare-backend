package br.com.psicare.api.dto;

import java.util.UUID;

public record DadosLoginResponse(
        String token,
        UUID id,
        String nome,
        String email,
        String iniciais,
        String telefone // Adicionado campo telefone
) {}