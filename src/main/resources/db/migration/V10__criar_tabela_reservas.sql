CREATE TABLE reservas (
                          id UUID PRIMARY KEY,
                          unidade_id UUID NOT NULL,
                          area_comum_id UUID NOT NULL,
                          criado_por_usuario_id UUID NOT NULL,
                          inicio TIMESTAMPTZ NOT NULL,
                          fim TIMESTAMPTZ NOT NULL,
                          status VARCHAR(30) NOT NULL DEFAULT 'ATIVA',
                          observacao TEXT,
                          criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_reservas_unidade
                              FOREIGN KEY (unidade_id)
                                  REFERENCES unidades(id),

                          CONSTRAINT fk_reservas_area_comum
                              FOREIGN KEY (area_comum_id)
                                  REFERENCES areas_comuns(id),

                          CONSTRAINT fk_reservas_usuario
                              FOREIGN KEY (criado_por_usuario_id)
                                  REFERENCES usuarios(id),

                          CONSTRAINT chk_reservas_periodo
                              CHECK (fim > inicio),

                          CONSTRAINT chk_reservas_status
                              CHECK (status IN ('ATIVA', 'CANCELADA', 'CONCLUIDA'))
);