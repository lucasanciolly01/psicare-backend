package br.com.psicare.api.services; // Ajustado para 'services' conforme seu caminho

// --- IMPORTS CORRIGIDOS ---
import br.com.psicare.api.dto.DadosCadastroAgendamento;
import br.com.psicare.api.dto.DadosDetalhamentoAgendamento; // <--- O IMPORT QUE FALTAVA
import br.com.psicare.api.model.Agendamento;
import br.com.psicare.api.model.Usuario;
import br.com.psicare.api.repository.AgendamentoRepository;
import br.com.psicare.api.repository.PacienteRepository;
import br.com.psicare.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Constante para a duração padrão da sessão (60 minutos)
    private static final int DURACAO_SESSAO_MINUTOS = 60;

    @Transactional
    public DadosDetalhamentoAgendamento agendar(DadosCadastroAgendamento dados, UUID psicologoId) {

        // 1. Validação de Existência (Psicólogo e Paciente)
        Usuario psicologo = usuarioRepository.findById(psicologoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Psicólogo não encontrado."));

        var paciente = pacienteRepository.findById(dados.pacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente com ID " + dados.pacienteId() + " não encontrado."));

        // 2. Validação da Regra de Negócio: Disponibilidade
        validarDisponibilidade(psicologoId, dados.dataHora());

        // 3. Criação e Persistência
        var agendamento = new Agendamento(
                psicologo,
                paciente,
                dados.dataHora(),
                dados.tipoSessao(),
                dados.valorCobrado()
        );
        agendamentoRepository.save(agendamento);

        return new DadosDetalhamentoAgendamento(agendamento);
    }

    /**
     * Cancela um agendamento (Delete lógico ou físico, aqui faremos físico para simplificar ou mudar status)
     * Para este exemplo, faremos a exclusão física conforme seu frontend espera removerAgendamento.
     */
    @Transactional
    public void cancelar(UUID id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Agendamento não encontrado");
        }
        agendamentoRepository.deleteById(id);
    }

    /**
     * Valida se o horário proposto está livre e atende às regras de negócio.
     */
    private void validarDisponibilidade(UUID psicologoId, LocalDateTime dataHora) {

        // Regra 1: Sessão deve ser agendada no futuro
        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(5))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível agendar para o passado ou muito próximo.");
        }

        // Regra 2: Sessão deve ser agendada em intervalos de 30 minutos (00 ou 30)
        if (dataHora.getMinute() % 30 != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamentos devem ser feitos a cada 30 minutos (ex: 10:00, 10:30).");
        }

        // Regra 3: Sessão deve ser em dia útil (Segunda a Sexta)
        DayOfWeek day = dataHora.getDayOfWeek();
        if (day.equals(DayOfWeek.SATURDAY) || day.equals(DayOfWeek.SUNDAY)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clínica não opera nos finais de semana.");
        }

        // --- Validação de Conflito de Horário ---

        if (agendamentoRepository.existsByPsicologoIdAndDataHora(psicologoId, dataHora)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O horário selecionado já está agendado.");
        }

        LocalDateTime horarioFim = dataHora.plusMinutes(DURACAO_SESSAO_MINUTOS).minusMinutes(1);
        LocalDateTime inicioBusca = dataHora.minusMinutes(DURACAO_SESSAO_MINUTOS - 1);

        List<Agendamento> conflitos = agendamentoRepository.findAllByPsicologoIdAndDataHoraBetweenOrderByDataHoraAsc(psicologoId, inicioBusca, horarioFim);

        if (!conflitos.isEmpty()) {
            for (Agendamento conflito : conflitos) {
                LocalDateTime conflitoFim = conflito.getDataHora().plusMinutes(DURACAO_SESSAO_MINUTOS);

                if (dataHora.isBefore(conflitoFim) && conflito.getDataHora().isBefore(dataHora.plusMinutes(DURACAO_SESSAO_MINUTOS))) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "O horário selecionado (" + dataHora.truncatedTo(ChronoUnit.MINUTES) + ") conflita com uma sessão existente às " + conflito.getDataHora().truncatedTo(ChronoUnit.MINUTES) + ".");
                }
            }
        }
    }

    /**
     * Busca todos os agendamentos do psicólogo logado em um período.
     */
    public List<DadosDetalhamentoAgendamento> listarAgenda(UUID psicologoId, LocalDateTime inicio, LocalDateTime fim) {

        var agendamentos = agendamentoRepository.findAllByPsicologoIdAndDataHoraBetweenOrderByDataHoraAsc(
                psicologoId,
                inicio,
                fim
        );

        return agendamentos.stream()
                .map(DadosDetalhamentoAgendamento::new)
                .toList();
    }
}