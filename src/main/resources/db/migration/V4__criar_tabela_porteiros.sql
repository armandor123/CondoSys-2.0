CREATE TABLE PORTEIROS (
    usuario_id UUID PRIMARY KEY,
    turno VARCHAR(30) NOT NULL,
    data_demissao DATE NOT NULL,

    CONSTRAINT fk_porteiros_usuario
                       FOREIGN KEY (usuario_id)
                       REFERENCES usuarios(id)
);