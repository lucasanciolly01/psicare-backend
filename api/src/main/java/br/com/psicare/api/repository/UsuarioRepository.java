package br.com.psicare.api.repository;

import br.com.psicare.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Método mágico do Spring Data para buscar por email
    UserDetails findByEmail(String email);
}