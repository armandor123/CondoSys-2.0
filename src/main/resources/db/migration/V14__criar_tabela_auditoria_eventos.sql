CREATE TABLE auditoria_eventos (
    id UUID PRIMARY KEY ,
    usuario_id UUID,

    entidade VARCHAR(100) NOT NULL,
    entidade_id UUID,
    acao VARCHAR(50) NOT NULL ,

    dados_anteriores JSONB,
    dados_novos JSONB,

    data_hora TIMESTAMPTZ NOT NULL  DEFAULT  CURRENT_TIMESTAMP,
    ip VARCHAR(45),

    CONSTRAINT fk_auditoria_eventos_usuario
                               FOREIGN KEY (usuario_id)
                               REFERENCES usuarios(id)



);