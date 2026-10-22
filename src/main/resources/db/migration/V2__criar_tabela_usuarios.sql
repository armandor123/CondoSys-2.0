CREATE TABLE usuarios(
    id UUID PRIMARY KEY,
    pessoa_id UUID NOT NULL  UNIQUE,
    email VARCHAR(150) NOT NULL  UNIQUE,
    senha_hash VARCHAR (255) NOT NULL,
    perfil VARCHAR (30) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    ciado_em TIMESTAMPTZ NOT NULL DEFAULT  CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT  CURRENT_TIMESTAMP,

    CONSTRAINT  fk_usuarios_pessoa
                    FOREIGN KEY (pessoa_id)
                    REFERENCES pessoas(id),


    CONSTRAINT  chk_usuarios_perfil
                     check ( perfil IN ('ADMINISTRADOR', 'PORTEIRO', 'PROPRIETARIO') )
);