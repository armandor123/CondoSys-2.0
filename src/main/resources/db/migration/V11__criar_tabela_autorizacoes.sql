CREATE TABLE autorizacoes (
                              id UUID PRIMARY KEY,
                              pessoa_id UUID NOT NULL,
                              proprietario_id UUID NOT NULL,
                              unidade_id UUID NOT NULL,

                              tipo VARCHAR(30) NOT NULL,
                              status VARCHAR(30) NOT NULL DEFAULT 'ATIVA',

                              inicio_validade TIMESTAMPTZ NOT NULL,
                              fim_validade TIMESTAMPTZ,

                              observacao TEXT,
                              criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              revogado_em TIMESTAMPTZ,

                              CONSTRAINT fk_autorizacoes_pessoa
                                  FOREIGN KEY (pessoa_id)
                                      REFERENCES pessoas(id),

                              CONSTRAINT fk_autorizacoes_proprietario
                                  FOREIGN KEY (proprietario_id)
                                      REFERENCES proprietarios(usuario_id),

                              CONSTRAINT fk_autorizacoes_unidade
                                  FOREIGN KEY (unidade_id)
                                      REFERENCES unidades(id),

                              CONSTRAINT chk_autorizacoes_tipo
                                  CHECK (tipo IN (
                                                  'UNICA_ENTRADA',
                                                  'PRAZO_DETERMINADO',
                                                  'PRAZO_INDETERMINADO'
                                      )),

                              CONSTRAINT chk_autorizacoes_status
                                  CHECK (status IN (
                                                    'ATIVA',
                                                    'EXPIRADA',
                                                    'REVOGADA',
                                                    'UTILIZADA'
                                      )),

                              CONSTRAINT chk_autorizacoes_periodo
                                  CHECK (
                                      fim_validade IS NULL
                                          OR fim_validade > inicio_validade
                                      )
);