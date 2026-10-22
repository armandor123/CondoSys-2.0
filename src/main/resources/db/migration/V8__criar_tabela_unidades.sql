CREATE TABLE unidades (
                          id UUID PRIMARY KEY,
                          proprietario_id UUID NOT NULL,
                          bloco VARCHAR(20) NOT NULL,
                          numero VARCHAR(20) NOT NULL,
                          andar INTEGER,
                          ativa BOOLEAN NOT NULL DEFAULT TRUE,

                          CONSTRAINT fk_unidades_proprietario
                              FOREIGN KEY (proprietario_id)
                                  REFERENCES proprietarios(usuario_id),

                          CONSTRAINT uk_unidades_bloco_numero
                              UNIQUE (bloco, numero)
);