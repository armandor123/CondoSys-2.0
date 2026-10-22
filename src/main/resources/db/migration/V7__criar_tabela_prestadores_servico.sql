CREATE TABLE prestadores_servico (
                                     pessoa_id UUID PRIMARY KEY,
                                     empresa VARCHAR(150),
                                     tipo_servico VARCHAR(100) NOT NULL,
                                     observacao TEXT,

                                     CONSTRAINT fk_prestadores_servico_pessoa
                                         FOREIGN KEY (pessoa_id)
                                             REFERENCES pessoas(id)
);