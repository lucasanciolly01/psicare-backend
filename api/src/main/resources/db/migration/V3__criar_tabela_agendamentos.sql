CREATE TABLE tb_agendamentos (
    id UUID PRIMARY KEY,
    psicologo_id UUID NOT NULL REFERENCES tb_usuarios(id),
    paciente_id UUID NOT NULL REFERENCES tb_pacientes(id),
    data_hora TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    tipo_sessao VARCHAR(50) NOT NULL,
    valor_cobrado NUMERIC(10, 2) NOT NULL, -- Essencial para mapear BigDecimal
    status_sessao VARCHAR(50) NOT NULL,
    observacoes TEXT,

    -- Garante que um psicólogo não agende duas sessões no mesmo horário (Índice opcional, mas útil)
    CONSTRAINT uk_psicologo_data_hora UNIQUE (psicologo_id, data_hora)
);