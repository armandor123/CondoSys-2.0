# CondoSys 2.0

Sistema de gestão condominial desenvolvido como evolução arquitetural do projeto CondoSys.

O **CondoSys 2.0** tem como objetivo centralizar e organizar processos relacionados à administração de condomínios, incluindo usuários, moradores, funcionários, visitantes, prestadores de serviço, controle de acesso, encomendas, reservas, ocorrências e demais rotinas operacionais.

A nova versão está sendo desenvolvida com **Java 21, Spring Boot e PostgreSQL**, utilizando uma arquitetura em camadas e API REST.

> Projeto acadêmico em desenvolvimento. A aplicação ainda não está preparada para utilização em ambiente de produção.

---

## Objetivo

A primeira versão do CondoSys foi construída como uma aplicação desktop em Java.

O CondoSys 2.0 representa uma evolução do projeto, com foco em:

- arquitetura mais organizada e escalável;
- separação entre backend e interfaces;
- API REST;
- banco de dados PostgreSQL;
- versionamento do banco com Flyway;
- regras de negócio centralizadas;
- testes automatizados;
- segurança de senhas;
- futura integração com aplicações web e desktop.

---

## Arquitetura

A aplicação segue uma arquitetura em camadas:

```text
Cliente
   ↓
Controller
   ↓
DTO / Validação
   ↓
Service
   ↓
Repository
   ↓
JPA / Hibernate
   ↓
PostgreSQL
```

Cada camada possui uma responsabilidade específica.

### Controller

Responsável por receber as requisições HTTP e retornar as respostas da API.

### DTO

Controla os dados enviados e recebidos pelos endpoints.

### Service

Concentra as regras de negócio da aplicação.

### Repository

Responsável pela comunicação entre a aplicação e o banco de dados.

### Domain

Contém as entidades e regras relacionadas ao domínio do CondoSys.

---

## Tecnologias

| Tecnologia | Utilização |
| --- | --- |
| Java 21 | Linguagem principal |
| Spring Boot 4 | Framework backend |
| Spring Web MVC | Construção da API REST |
| Spring Data JPA | Persistência |
| Hibernate | ORM |
| PostgreSQL 16 | Banco de dados |
| Flyway | Versionamento do banco |
| Jakarta Validation | Validação dos dados |
| BCrypt | Hash das senhas |
| JUnit 5 | Testes automatizados |
| Mockito | Simulação de dependências nos testes |
| Maven | Gerenciamento de dependências |
| Docker | Ambiente do banco |
| Docker Compose | Orquestração local |
| Git | Versionamento |
| GitHub | Repositório remoto |

---

## Estrutura do projeto

```text
CondoSys-2.0/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/condosys/api/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── domain/
│   │   │       │   ├── enums/
│   │   │       │   └── model/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── mapper/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│           └── br/com/condosys/api/
│               └── service/
│
├── .env.example
├── .gitignore
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

# Domínio do sistema

A modelagem atual possui como base:

```text
Pessoa
  │
  └── Usuario
        │
        ├── Administrador
        ├── Porteiro
        └── Proprietario
```

Além desses perfis, o banco foi projetado para suportar outros módulos relacionados ao condomínio.

---

## Pessoa

`Pessoa` representa o cadastro-base utilizado pelo sistema.

Exemplos:

- usuários;
- visitantes;
- prestadores de serviço.

Principais funcionalidades implementadas:

- cadastro;
- listagem;
- busca por UUID;
- busca por documento;
- atualização;
- desativação;
- reativação.

A exclusão utiliza o conceito de **exclusão lógica**, preservando o registro no banco.

---

## Usuário

Um `Usuario` é associado a uma `Pessoa` e representa alguém que pode possuir acesso ao sistema.

Atualmente existem os perfis:

```text
ADMINISTRADOR
PORTEIRO
PROPRIETARIO
```

Funcionalidades implementadas:

- cadastro;
- associação com Pessoa;
- validação de e-mail duplicado;
- listagem de usuários ativos;
- busca por UUID;
- atualização de e-mail;
- atualização de perfil;
- desativação;
- reativação;
- alteração de senha;
- validação da senha atual.

---

# Segurança das senhas

O CondoSys não armazena senhas em texto puro.

As senhas são processadas utilizando:

```text
Spring Security Crypto
        ↓
BCryptPasswordEncoder
        ↓
Hash BCrypt
        ↓
PostgreSQL
```

Exemplo conceitual:

```text
Senha digitada
     ↓
BCrypt
     ↓
$2a$10$...
```

Ao alterar uma senha, o sistema utiliza:

```java
passwordEncoder.matches(...)
```

para comparar a senha informada com o hash armazenado.

---

## Importante sobre segurança

A proteção de senha com BCrypt já está implementada.

Entretanto, a API ainda está em desenvolvimento e **não possui autenticação e autorização completas nas rotas**.

Antes de qualquer implantação em ambiente público ainda deverão ser implementados:

- autenticação;
- autorização por perfil;
- proteção dos endpoints administrativos;
- políticas de acesso;
- configuração de CORS;
- gerenciamento seguro de credenciais;
- revisão das regras de alteração de perfil.

Por esse motivo, a aplicação deve permanecer apenas em ambiente de desenvolvimento.

---

# Administrador

O módulo `Administrador` utiliza uma relação de chave compartilhada com `Usuario`.

```text
Usuario
   │
   │ mesmo UUID
   ▼
Administrador
```

O relacionamento utiliza:

```java
@OneToOne
@MapsId
```

Entre as regras de negócio desenvolvidas estão:

- usuário precisa existir;
- usuário precisa possuir perfil `ADMINISTRADOR`;
- usuário precisa estar ativo;
- um mesmo usuário não pode possuir dois cadastros de administrador;
- data de contratação não pode ser futura.

---

# Testes automatizados

O projeto utiliza:

- JUnit 5;
- Mockito.

Os testes do `AdministradorService` validam diferentes cenários de negócio.

Atualmente são verificados casos como:

```text
✔ impedir perfil incorreto
✔ permitir administrador válido
✔ impedir cadastro duplicado
✔ impedir usuário inativo
✔ impedir data de contratação futura
```

Os testes utilizam mocks para testar a camada de serviço sem necessidade de acessar o PostgreSQL.

Exemplo da arquitetura de teste:

```text
AdministradorServiceTest
         ↓
AdministradorService
      ↙       ↘
   Mock       Mock
Repository  UsuarioService
```

---

# Porteiro

O módulo `Porteiro` está em desenvolvimento.

A entidade possui informações específicas do vínculo operacional, como:

```text
usuario_id
turno
data_demissao
```

O registro compartilha o UUID com `Usuario`.

```text
Usuario
   │
   ▼
Porteiro
```

A data de demissão permanece nula enquanto o porteiro possuir vínculo ativo.

Entre as regras previstas estão:

- usuário precisa possuir perfil `PORTEIRO`;
- validação do turno;
- alteração de turno;
- registro de demissão;
- impedir datas futuras;
- impedir registro duplicado de demissão.

---

# Banco de dados

O projeto utiliza:

```text
PostgreSQL 16
```

executado localmente através de Docker.

O banco possui migrations controladas pelo Flyway.

A modelagem contempla módulos como:

```text
pessoas
usuarios
administradores
porteiros
proprietarios
visitantes
prestadores_servico
unidades
areas_comuns
reservas
autorizacoes
registros_acesso
encomendas
auditoria_eventos
```

Novas alterações no banco devem ser realizadas através de novas migrations.

Migrations já executadas **não devem ser alteradas**, pois o Flyway utiliza checksum para garantir a integridade do histórico.

---

# Docker

O PostgreSQL é executado através do Docker Compose.

A configuração atual utiliza:

```text
PostgreSQL 16
porta local: 5434
porta container: 5432
```

Para iniciar:

```bash
docker compose up -d
```

Para verificar o banco:

```bash
docker exec condosys-postgres \
  pg_isready -U condosys -d condosys_db
```

Para acessar o PostgreSQL:

```bash
docker exec -it condosys-postgres \
  psql -U condosys -d condosys_db
```

Para sair:

```sql
\q
```

---

# Configuração do ambiente

O projeto utiliza variáveis de ambiente para informações sensíveis.

Existe um arquivo:

```text
.env.example
```

que serve apenas como modelo.

Exemplo:

```env
DB_NAME=condosys_db
DB_USER=condosys
DB_PASSWORD=troque_por_uma_senha_segura
```

Crie o arquivo local:

```bash
cp .env.example .env
```

Depois configure uma senha própria.

O arquivo:

```text
.env
```

está incluído no `.gitignore` e **não deve ser enviado ao GitHub**.

---

# Executando o projeto

## Pré-requisitos

Para executar localmente:

```text
Java 21
Docker
Docker Compose
Git
Maven Wrapper
```

---

## 1. Clonar o projeto

```bash
git clone git@github.com:armandor123/CondoSys-2.0.git
```

Entre na pasta:

```bash
cd CondoSys-2.0
```

---

## 2. Criar o ambiente local

```bash
cp .env.example .env
```

Configure os dados locais do PostgreSQL no `.env`.

---

## 3. Iniciar o banco

```bash
docker compose up -d
```

---

## 4. Carregar as variáveis

Em Linux:

```bash
set -a
source .env
set +a
```

---

## 5. Executar a aplicação

```bash
./mvnw spring-boot:run
```

Por padrão, a aplicação é disponibilizada em:

```text
http://localhost:8080
```

---

# Testes

Para executar toda a suíte:

```bash
./mvnw test
```

Os testes também podem ser executados diretamente pela IDE.

---

# Exemplos de endpoints

## Pessoas

```http
GET /pessoas
GET /pessoas/{id}
GET /pessoas/documento/{documento}
POST /pessoas
PUT /pessoas/{id}
PUT /pessoas/{id}/reativar
DELETE /pessoas/{id}
```

## Usuários

```http
GET /usuarios
GET /usuarios/{id}
POST /usuarios
PUT /usuarios/{id}
PUT /usuarios/{id}/reativar
PUT /usuarios/{id}/senha
DELETE /usuarios/{id}
```

Os endpoints poderão sofrer alterações conforme a evolução da API.

---

# Próximas etapas

O desenvolvimento está sendo realizado de forma incremental.

Entre as próximas etapas estão:

- concluir módulo Porteiro;
- desenvolver módulo Proprietário;
- ampliar testes automatizados;
- implementar autenticação;
- implementar autorização por perfil;
- controle de visitantes;
- cadastro de prestadores;
- controle de autorizações;
- registros de entrada e saída;
- gestão de encomendas;
- reservas de áreas comuns;
- auditoria de eventos;
- documentação da API;
- integração com interfaces web e desktop.

---

# Status

```text
Pessoa                  ✔ Implementado
Usuario                 ✔ Implementado
BCrypt                  ✔ Implementado
PostgreSQL              ✔ Configurado
Flyway                  ✔ Configurado
Docker                  ✔ Configurado
Administrador           ◐ Em desenvolvimento
Testes Administrador    ✔ Implementados
Porteiro                ◐ Em desenvolvimento
Proprietario            ○ Planejado
Autenticação            ○ Planejada
Autorização             ○ Planejada
Frontend Web            ○ Planejado
```

Legenda:

```text
✔ concluído
◐ em desenvolvimento
○ planejado
```

---

# Finalidade acadêmica

O CondoSys 2.0 é um projeto acadêmico utilizado para aplicação prática de conceitos relacionados a:

- Programação Orientada a Objetos;
- Engenharia de Software;
- Banco de Dados;
- APIs REST;
- arquitetura em camadas;
- testes automatizados;
- versionamento;
- segurança;
- desenvolvimento colaborativo.

A arquitetura e as funcionalidades continuarão evoluindo durante o desenvolvimento do projeto.
