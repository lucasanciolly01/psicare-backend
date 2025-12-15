-- Habilita extensão para gerar UUIDs randomicos
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela de Usuários (Psicólogos)
CREATE TABLE tb_usuarios
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    telefone   VARCHAR(20),
    foto_blob  TEXT, -- Base64 pode ser grande, usamos TEXT
    iniciais   VARCHAR(5),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Pacientes
CREATE TABLE tb_pacientes
(
    id                   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id           UUID         NOT NULL, -- FK: Paciente pertence a um Psicólogo
    nome                 VARCHAR(255) NOT NULL,
    data_nascimento      DATE         NOT NULL,
    email                VARCHAR(255),
    telefone             VARCHAR(20)  NOT NULL,
    queixa_principal     TEXT,
    historico_familiar   TEXT,
    observacoes_iniciais TEXT,
    anotacoes TEXT,
    status               VARCHAR(20)  NOT NULL, -- ativo, inativo, pausa
    frequencia_sessao    VARCHAR(20),
    avatar_color         VARCHAR(20),
    created_at           TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_paciente_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios (id)
);

-- Índices para performance
CREATE INDEX idx_pacientes_usuario ON tb_pacientes (usuario_id);
CREATE INDEX idx_usuarios_email ON tb_usuarios (email);