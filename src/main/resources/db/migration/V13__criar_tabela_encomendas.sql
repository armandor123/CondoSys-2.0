CREATE TABLE encomendas (
                            id UUID PRIMARY KEY,
                            unidade_id UUID NOT NULL,
                            porteiro_id UUID NOT NULL,

                            remetente VARCHAR(150),
                            destinatario VARCHAR(150) NOT NULL,
                            codigo_rastreio VARCHAR(100),
                            descricao TEXT,

                            recebido_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            retirado_em TIMESTAMPTZ,

                            status VARCHAR(30) NOT NULL DEFAULT 'RECEBIDA',
                            retirado_por VARCHAR(150),

                            CONSTRAINT fk_encomendas_unidade
                                FOREIGN KEY (unidade_id)
                                    REFERENCES unidades(id),

                            CONSTRAINT fk_encomendas_porteiro
                                FOREIGN KEY (porteiro_id)
                                    REFERENCES porteiros(usuario_id),

                            CONSTRAINT chk_encomendas_status
                                CHECK (status IN (
                                                  'RECEBIDA',
                                                  'AGUARDANDO_RETIRADA',
                                                  'ENTREGUE'
                                    ))
);