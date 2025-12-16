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
    private String senha; // O Spring Security exige que o getter se chame getPassword()

    private String telefone;

    @Column(name = "foto_blob")
    private String fotoBlob;

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

    // === Métodos do Spring Security (UserDetails) ===

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, todo usuário tem permissão genérica de USER
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email; // Nosso login é via Email
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