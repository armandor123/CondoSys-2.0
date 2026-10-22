
-- Permite cadastrar porteiros ainda empregados.
ALTER TABLE porteiros
    ALTER COLUMN data_demissao DROP NOT NULL;
