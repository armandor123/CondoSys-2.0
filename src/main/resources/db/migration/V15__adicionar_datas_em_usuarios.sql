-- Adiciona campos de controle de criação e atualização
-- na tabela usuarios.

ALTER TABLE usuarios
    ADD COLUMN IF NOT EXISTS criado_em TIMESTAMPTZ
    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMPTZ
    NOT NULL DEFAULT CURRENT_TIMESTAMP;