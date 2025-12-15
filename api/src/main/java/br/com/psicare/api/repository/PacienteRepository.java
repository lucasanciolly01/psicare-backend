package br.com.psicare.api.repository;

import br.com.psicare.api.model.Paciente;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.enums.StatusPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    // Método essencial para o Soft Delete funcionar na listagem
    Page<Paciente> findAllByUsuarioAndStatusIsNot(Usuario usuario, StatusPaciente status, Pageable pageable);

    // Métodos auxiliares de busca
    Paciente findByIdAndUsuario(UUID id, Usuario usuario);
    Page<Paciente> findAllByUsuario(Usuario usuario, Pageable pageable);

    // === NOVOS MÉTODOS PARA O DASHBOARD ===

    // Conta total de pacientes do psicólogo
    long countByUsuario(Usuario usuario);

    // Conta pacientes por status (ex: quantos estão ATIVO)
    long countByUsuarioAndStatus(Usuario usuario, StatusPaciente status);
}