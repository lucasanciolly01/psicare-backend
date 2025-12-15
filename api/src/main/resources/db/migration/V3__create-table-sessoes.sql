CREATE TABLE tb_sessoes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    paciente_id UUID NOT NULL,

    data DATE NOT NULL,              -- OBRIGATÓRIO: campo 'data'
    tipo VARCHAR(100),               -- OBRIGATÓRIO: campo 'tipo'
    status_sessao VARCHAR(50),       -- OBRIGATÓRIO: campo 'status_sessao'

    evolucao TEXT,
    anotacoes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sessoes_paciente FOREIGN KEY (paciente_id) REFERENCES tb_pacientes(id)
);

CREATE INDEX idx_sessoes_paciente ON tb_sessoes(paciente_id);
CREATE INDEX idx_sessoes_data ON tb_sessoes(data);