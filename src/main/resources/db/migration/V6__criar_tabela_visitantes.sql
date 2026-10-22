CREATE TABLE visitantes (
                            pessoa_id UUID PRIMARY KEY,
                            observacao TEXT,

                            CONSTRAINT fk_visitantes_pessoa
                                FOREIGN KEY (pessoa_id)
                                    REFERENCES pessoas(id)
);