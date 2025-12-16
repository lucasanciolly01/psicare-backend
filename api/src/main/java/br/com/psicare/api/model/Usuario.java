package br.com.psicare.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "tb_usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private String email;

    @Column(name = "senha_hash")
    private String senha;

    private String telefone;

    @Column(name = "foto_blob")
    private String fotoBlob; // Campo onde a string Base64 é salva

    private String iniciais;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtor utilitário para cadastro
    public Usuario(String nome, String email, String senhaHash, String telefone, String iniciais) {
        this.nome = nome;
        this.email = email;
        this.senha = senhaHash;
        this.telefone = telefone;
        this.iniciais = iniciais;
        this.createdAt = LocalDateTime.now();
    }

    // === Método de Atualização (Regra de Negócio) ===
    // ATUALIZADO: Agora aceita a foto
    public void atualizarInformacoes(String nome, String telefone, String novaSenhaHash, String novaFoto) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
            // Recalcula iniciais se o nome mudou
            this.iniciais = nome.substring(0, Math.min(2, nome.length())).toUpperCase();
        }

        if (telefone != null) {
            this.telefone = telefone;
        }

        if (novaSenhaHash != null) {
            this.senha = novaSenhaHash;
        }

        if (novaFoto != null) {
            this.fotoBlob = novaFoto;
        }
    }

    // === Métodos do Spring Security (UserDetails) ===

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}