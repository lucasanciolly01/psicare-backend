package br.com.psicare.api.model;

import br.com.psicare.api.enums.StatusSessao;
import br.com.psicare.api.enums.TipoSessao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal; // IMPORTANTE para valores monetários

@Table(name = "tb_agendamentos")
@Entity(name = "Agendamento")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relacionamento com o Psicólogo (Usuário)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicologo_id", nullable = false)
    private Usuario psicologo;

    // Relacionamento com o Paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sessao", nullable = false)
    private TipoSessao tipoSessao;

    // BigDecimal com precisão para mapear para NUMERIC(10, 2) no banco
    @Column(name = "valor_cobrado", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorCobrado;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_sessao", nullable = false)
    private StatusSessao statusSessao;

    @Column(name = "observacoes")
    private String observacoes;

    // Construtor usado no Service para criar novos agendamentos
    public Agendamento(Usuario psicologo, Paciente paciente, LocalDateTime dataHora, TipoSessao tipoSessao, BigDecimal valorCobrado) {
        this.psicologo = psicologo;
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.tipoSessao = tipoSessao;
        this.valorCobrado = valorCobrado;
        this.statusSessao = StatusSessao.AGENDADO;
    }
}