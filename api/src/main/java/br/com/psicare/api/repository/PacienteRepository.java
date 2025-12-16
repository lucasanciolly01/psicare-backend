package br.com.psicare.api.repository;

import br.com.psicare.api.model.Paciente;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.enums.StatusPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    // Lista pacientes ativos (excluindo os deletados logicamente), com paginação
    Page<Paciente> findAllByUsuarioAndStatusIsNot(Usuario usuario, StatusPaciente status, Pageable pageable);

    // Busca um paciente específico garantindo que pertence ao usuário logado
    Paciente findByIdAndUsuario(UUID id, Usuario usuario);

    // Busca todos os pacientes de um usuário
    Page<Paciente> findAllByUsuario(Usuario usuario, Pageable pageable);

    // === MÉTODOS DO DASHBOARD ===

    // Conta total de pacientes do psicólogo
    long countByUsuario(Usuario usuario);

    // Conta pacientes por status (ex: quantos estão ATIVO)
    long countByUsuarioAndStatus(Usuario usuario, StatusPaciente status);
}