CREATE TABLE proprietarios (
                               usuario_id UUID PRIMARY KEY,
                               data_adesao DATE NOT NULL,

                               CONSTRAINT fk_proprietarios_usuario
                                   FOREIGN KEY (usuario_id)
                                       REFERENCES usuarios(id)
);