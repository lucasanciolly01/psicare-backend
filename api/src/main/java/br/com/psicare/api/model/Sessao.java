package br.com.psicare.api.model;

import br.com.psicare.api.dto.DadosCadastroSessao;
import br.com.psicare.api.enums.StatusSessao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "tb_sessoes")
@Entity(name = "Sessao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    // CORREÇÃO 1: Campo unificado para Data (sem hora)
    private LocalDate data;

    // CORREÇÃO 2: Adicionado Tipo (Ex: "Acompanhamento", "Anamnese")
    private String tipo;

    // CORREÇÃO 3: Renomeado para statusSessao para clareza
    @Enumerated(EnumType.STRING)
    @Column(name = "status_sessao")
    private StatusSessao statusSessao;

    @Column(columnDefinition = "TEXT") // Garante que cabe textos grandes
    private String evolucao;

    @Column(columnDefinition = "TEXT")
    private String anotacoes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Sessao(DadosCadastroSessao dados, Paciente paciente) {
        this.paciente = paciente;
        this.data = dados.data();         // DTO deve fornecer .data()
        this.tipo = dados.tipo();         // DTO deve fornecer .tipo()
        this.statusSessao = dados.statusSessao(); // DTO deve fornecer .statusSessao()
        this.evolucao = dados.evolucao();
        // this.anotacoes = dados.anotacoes(); // Se o DTO tiver anotacoes, descomente
        this.createdAt = LocalDateTime.now();
    }
}