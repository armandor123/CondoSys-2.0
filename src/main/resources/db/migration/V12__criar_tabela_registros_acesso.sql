CREATE TABLE registros_acesso (
                                  id UUID PRIMARY KEY,
                                  pessoa_id UUID NOT NULL,
                                  unidade_id UUID NOT NULL,
                                  porteiro_id UUID NOT NULL,
                                  autorizacao_id UUID,

                                  tipo_movimento VARCHAR(20) NOT NULL,
                                  status VARCHAR(20) NOT NULL,

                                  liberacao_manual BOOLEAN NOT NULL DEFAULT FALSE,
                                  motivo_liberacao TEXT,
                                  observacao TEXT,

                                  data_hora TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_registros_acesso_pessoa
                                      FOREIGN KEY (pessoa_id)
                                          REFERENCES pessoas(id),

                                  CONSTRAINT fk_registros_acesso_unidade
                                      FOREIGN KEY (unidade_id)
                                          REFERENCES unidades(id),

                                  CONSTRAINT fk_registros_acesso_porteiro
                                      FOREIGN KEY (porteiro_id)
                                          REFERENCES porteiros(usuario_id),

                                  CONSTRAINT fk_registros_acesso_autorizacao
                                      FOREIGN KEY (autorizacao_id)
                                          REFERENCES autorizacoes(id),

                                  CONSTRAINT chk_registros_acesso_movimento
                                      CHECK (tipo_movimento IN ('ENTRADA', 'SAIDA')),

                                  CONSTRAINT chk_registros_acesso_status
                                      CHECK (status IN ('AUTORIZADO', 'NEGADO')),

                                  CONSTRAINT chk_registros_acesso_liberacao_manual
                                      CHECK (
                                          liberacao_manual = FALSE
                                              OR (
                                              motivo_liberacao IS NOT NULL
                                                  AND TRIM(motivo_liberacao) <> ''
                                              )
                                          )
);