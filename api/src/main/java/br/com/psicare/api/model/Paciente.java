package br.com.psicare.api.model;

import br.com.psicare.api.dto.DadosCadastroPaciente;
import br.com.psicare.api.dto.DadosAtualizacaoPaciente;
import br.com.psicare.api.enums.FrequenciaSessao;
import br.com.psicare.api.enums.StatusPaciente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "tb_pacientes")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String nome;
    private String email;
    private String telefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    // Prontuário / Dados Clínicos
    @Column(name = "queixa_principal", columnDefinition = "TEXT")
    private String queixaPrincipal;

    @Column(name = "historico_familiar", columnDefinition = "TEXT")
    private String historicoFamiliar;

    @Column(name = "observacoes_iniciais", columnDefinition = "TEXT")
    private String observacoesIniciais;

    // --- NOVO CAMPO ADICIONADO ---
    @Column(columnDefinition = "TEXT") // Garante que cabe bastante texto no banco
    private String anotacoes;

    // Configurações
    @Enumerated(EnumType.STRING)
    private StatusPaciente status;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequencia_sessao")
    private FrequenciaSessao frequenciaSessao;

    @Column(name = "avatar_color")
    private String avatarColor;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtor para Cadastro
    public Paciente(DadosCadastroPaciente dados, Usuario usuario) {
        this.usuario = usuario;
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.dataNascimento = dados.dataNascimento();
        this.queixaPrincipal = dados.queixaPrincipal();
        this.historicoFamiliar = dados.historicoFamiliar();
        this.observacoesIniciais = dados.observacoesIniciais();

        // Captura as anotações do DTO
        this.anotacoes = dados.anotacoes();

        this.frequenciaSessao = dados.frequenciaSessao();
        this.avatarColor = dados.avatarColor(); // Front manda a cor ou geramos aqui
        this.status = StatusPaciente.ATIVO;
        this.createdAt = LocalDateTime.now();
    }

    // Método para Atualizar Dados (PUT)
    public void atualizarInformacoes(DadosAtualizacaoPaciente dados) {
        if (dados.nome() != null) this.nome = dados.nome();
        if (dados.telefone() != null) this.telefone = dados.telefone();
        if (dados.email() != null) this.email = dados.email();

        // --- LINHA NOVA ---
        if (dados.dataNascimento() != null) this.dataNascimento = dados.dataNascimento();

        if (dados.queixaPrincipal() != null) this.queixaPrincipal = dados.queixaPrincipal();
        if (dados.historicoFamiliar() != null) this.historicoFamiliar = dados.historicoFamiliar();
        if (dados.observacoesIniciais() != null) this.observacoesIniciais = dados.observacoesIniciais();
        if (dados.anotacoes() != null) this.anotacoes = dados.anotacoes();

        if (dados.status() != null) this.status = dados.status();
        if (dados.frequenciaSessao() != null) this.frequenciaSessao = dados.frequenciaSessao();
        if (dados.avatarColor() != null) this.avatarColor = dados.avatarColor();
    }

    // Método lógico para exclusão (Soft Delete) ou inativação, se preferir não apagar do banco
    public void inativar() {
        this.status = StatusPaciente.INATIVO;
    }
}