CREATE TABLE pessoas (
                         id UUID PRIMARY KEY,
                         nome VARCHAR(150) NOT NULL,
                         documento VARCHAR(30) NOT NULL UNIQUE,
                         telefone VARCHAR(30),
                         tipo_pessoa VARCHAR(30) NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT TRUE,
                         criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         atualizado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
