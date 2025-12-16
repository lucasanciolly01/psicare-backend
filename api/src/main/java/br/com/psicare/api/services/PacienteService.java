package br.com.psicare.api.services;

import br.com.psicare.api.dto.DadosAtualizacaoPaciente;
import br.com.psicare.api.dto.DadosCadastroPaciente;
import br.com.psicare.api.dto.DadosDetalhamentoPaciente;
import br.com.psicare.api.enums.StatusPaciente;
import br.com.psicare.api.model.Paciente;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    public DadosDetalhamentoPaciente cadastrar(DadosCadastroPaciente dados, Usuario usuarioLogado) {
        var paciente = new Paciente(dados, usuarioLogado);
        repository.save(paciente);
        return new DadosDetalhamentoPaciente(paciente);
    }

    public Page<DadosDetalhamentoPaciente> listar(Pageable paginacao, Usuario usuarioLogado) {
        // Regra de Negócio: Não mostrar pacientes inativos
        return repository.findAllByUsuarioAndStatusIsNot(usuarioLogado, StatusPaciente.INATIVO, paginacao)
                .map(DadosDetalhamentoPaciente::new);
    }

    public DadosDetalhamentoPaciente detalhar(UUID id) {
        // O getReferenceById lança EntityNotFoundException se não existir,
        // que será capturado pelo nosso TratadorDeErros.
        var paciente = repository.getReferenceById(id);
        return new DadosDetalhamentoPaciente(paciente);
    }

    public DadosDetalhamentoPaciente atualizar(DadosAtualizacaoPaciente dados) {
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);
        // O JPA deteta mudanças automaticamente e salva ao fim da transação
        return new DadosDetalhamentoPaciente(paciente);
    }

    public void excluir(UUID id) {
        var paciente = repository.getReferenceById(id);
        paciente.inativar();
    }
}