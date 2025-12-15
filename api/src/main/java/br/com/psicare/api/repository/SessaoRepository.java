package br.com.psicare.api.repository;

import br.com.psicare.api.model.Sessao;
import br.com.psicare.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SessaoRepository extends JpaRepository<Sessao, UUID> {

    // CORREÇÃO: Busca pelo campo 'data' (criado na entidade acima)
    List<Sessao> findAllByPacienteIdOrderByDataDesc(UUID pacienteId);

    // Query para o Dashboard
    @Query("""
        select count(s) from Sessao s
        where s.paciente.usuario = :usuario
        and s.data between :inicio and :fim
    """)
    long contarSessoesNoPeriodo(@Param("usuario") Usuario usuario, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}