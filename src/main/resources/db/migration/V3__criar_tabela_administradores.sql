CREATE TABLE administradores (
                                 usuario_id UUID PRIMARY KEY,
                                 data_contratacao DATE NOT NULL,

                                 CONSTRAINT fk_administradores_usuario
                                     FOREIGN KEY (usuario_id)
                                           REFERENCES usuarios(id)
);