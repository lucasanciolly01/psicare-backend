package br.com.psicare.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroUsuario(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String senha,

        // Telefone agora é opcional para alinhar com o Front
        @Pattern(regexp = "\\d{10,11}")
        String telefone
) {
}