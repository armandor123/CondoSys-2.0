CREATE TABLE areas_comuns (
                              id UUID PRIMARY KEY,
                              nome VARCHAR(100) NOT NULL UNIQUE,
                              capacidade INTEGER NOT NULL,
                              ativa BOOLEAN NOT NULL DEFAULT TRUE,
                              descricao TEXT,

                              CONSTRAINT chk_areas_comuns_capacidade
                                  CHECK (capacidade >= 0)
);