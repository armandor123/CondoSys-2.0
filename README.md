# CondoSys 2.0

Sistema web de gestão condominial desenvolvido como evolução do projeto acadêmico **CondoSys**, originalmente criado como uma aplicação desktop em Java.

O **CondoSys 2.0** amplia a proposta inicial utilizando uma arquitetura cliente-servidor, API REST, banco de dados relacional e interface web.

O sistema tem como objetivo centralizar processos comuns da gestão de condomínios, como cadastro de pessoas, usuários, proprietários, unidades, visitantes, autorizações de acesso, registros de entrada e saída, encomendas e outras rotinas operacionais.

> Projeto acadêmico em desenvolvimento. O sistema representa um MVP funcional e ainda não está preparado para utilização em ambiente de produção.

---

## Evolução do projeto

A primeira versão do CondoSys foi desenvolvida como uma aplicação desktop utilizando Java.

Na segunda versão, o projeto passou por uma reestruturação arquitetural, separando interface, regras de negócio e persistência.

```text
CondoSys 1.0
Java Desktop
     ↓
Evolução arquitetural
     ↓
CondoSys 2.0
React + TypeScript
     ↓
API REST
Java + Spring Boot
     ↓
PostgreSQL
```

Essa evolução permite maior separação de responsabilidades, manutenção do código, escalabilidade e possibilidade de integração com diferentes interfaces.

---

# Arquitetura

A aplicação utiliza uma arquitetura em camadas.

```text
Frontend
React + TypeScript + Vite
        ↓
      HTTP
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

## Frontend

Responsável pela interface utilizada pelo usuário.

Tecnologias principais:

- React;
- TypeScript;
- Vite;
- React Router;
- Lucide React.

O frontend consome a API REST disponibilizada pelo backend.

---

## Controller

Responsável por receber as requisições HTTP e retornar as respostas da API.

---

## DTO

Define os dados utilizados na comunicação entre cliente e servidor e auxilia na validação das requisições.

---

## Service

Concentra as principais regras de negócio do sistema.

---

## Repository

Responsável pela comunicação da aplicação com o banco de dados através do Spring Data JPA.

---

## Domain

Contém as entidades, relacionamentos e regras relacionadas ao domínio do CondoSys.

---

# Tecnologias

| Tecnologia | Utilização |
| --- | --- |
| Java 21 | Linguagem principal do backend |
| Spring Boot 4 | Framework backend |
| Spring Web MVC | Construção da API REST |
| Spring Data JPA | Persistência |
| Hibernate | ORM |
| PostgreSQL 16 | Banco de dados |
| Flyway | Versionamento do banco |
| Jakarta Validation | Validação dos dados |
| BCrypt | Hash de senhas |
| JUnit 5 | Testes automatizados |
| Mockito | Simulação de dependências |
| Maven | Gerenciamento do projeto backend |
| React | Interface web |
| TypeScript | Linguagem do frontend |
| Vite | Ambiente de desenvolvimento frontend |
| Docker | Execução do banco de dados |
| Docker Compose | Orquestração local |
| Git | Controle de versão |
| GitHub | Repositório remoto |

---

# Estrutura geral

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
│
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.ts
│
├── .env.example
├── .gitignore
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

# Domínio do sistema

Entre as principais entidades atualmente utilizadas estão:

```text
Pessoa
 ├── Visitante
 │
 └── Usuario
      ├── Administrador
      ├── Porteiro
      └── Proprietario

Proprietario
     ↓
   Unidade

Visitante
     ↓
Autorizacao
     ↓
RegistroAcesso

Unidade
     ↓
 Encomenda
```

O banco também possui estrutura para áreas comuns, reservas, prestadores de serviço e auditoria.

---

# Pessoa

`Pessoa` representa o cadastro-base utilizado por diferentes módulos.

Exemplos:

- usuários;
- visitantes;
- prestadores de serviço.

Entre as funcionalidades disponíveis estão:

- cadastro;
- listagem;
- busca;
- atualização;
- desativação;
- reativação.

A exclusão utiliza o conceito de **exclusão lógica**, permitindo preservar o histórico do registro.

---

# Usuário

Um `Usuario` é associado a uma `Pessoa` e representa alguém que pode possuir acesso ao sistema.

Perfis atualmente modelados:

```text
ADMINISTRADOR
PORTEIRO
PROPRIETARIO
```

Entre as funcionalidades implementadas estão:

- cadastro;
- associação com Pessoa;
- validação de e-mail;
- listagem;
- atualização;
- desativação;
- reativação;
- alteração de senha.

---

# Administrador

O módulo Administrador utiliza uma relação de chave compartilhada com `Usuario`.

```text
Usuario
   │
   ▼
Administrador
```

Entre as regras implementadas estão:

- validação do perfil;
- validação de usuário ativo;
- prevenção de cadastro duplicado;
- validação da data de contratação.

---

# Porteiro

O módulo Porteiro está implementado no backend.

O cadastro possui informações específicas do vínculo operacional, incluindo turno e dados relacionados ao vínculo do funcionário.

O porteiro participa diretamente dos fluxos operacionais de controle de acesso e encomendas.

---

# Proprietário e unidades

O sistema possui cadastro de proprietários e unidades condominiais.

As unidades são vinculadas aos seus respectivos proprietários e utilizadas por outros módulos do sistema, como:

- autorizações;
- controle de acesso;
- encomendas;
- reservas.

Para os dados de demonstração são utilizadas identificações genéricas como:

```text
TORRE A
TORRE B
```

---

# Visitantes

O módulo de visitantes está integrado entre frontend e backend.

Permite:

- cadastrar visitantes;
- listar visitantes;
- consultar dados;
- alterar informações;
- pesquisar visitantes durante o atendimento da portaria.

O cadastro de visitante utiliza a entidade `Pessoa` como base.

---

# Autorizações

O CondoSys possui um módulo de autorização de acesso.

Tipos atualmente modelados:

```text
UNICA_ENTRADA
PRAZO_DETERMINADO
PRAZO_INDETERMINADO
```

Status possíveis:

```text
ATIVA
EXPIRADA
REVOGADA
UTILIZADA
```

Uma autorização relaciona:

```text
Pessoa
   ↓
Proprietário
   ↓
Unidade
   ↓
Autorização
```

Autorizações de entrada única são marcadas como `UTILIZADA` após a primeira entrada autorizada.

---

# Controle de acesso

O módulo registra movimentações de entrada e saída.

O fluxo principal implementado pode ser representado por:

```text
Visitante
   ↓
Autorização
   ↓
Validação
   ↓
Entrada
   ↓
Registro de acesso
   ↓
Autorização de entrada única = UTILIZADA
   ↓
Saída
   ↓
Histórico
```

O backend impede uma entrada autorizada sem uma autorização válida, exceto nos fluxos explicitamente tratados como liberação manual.

A saída pode ser registrada mesmo após uma autorização de entrada única já ter sido utilizada.

Esse fluxo foi validado durante os testes funcionais do sistema.

---

# Encomendas

O módulo de encomendas está integrado entre frontend e backend.

O fluxo operacional implementado permite acompanhar a encomenda através dos estados:

```text
RECEBIDA
    ↓
AGUARDANDO_RETIRADA
    ↓
ENTREGUE
```

O sistema relaciona a encomenda à unidade e registra sua evolução operacional.

---

# Frontend

A interface web está sendo construída com React, TypeScript e Vite.

Entre as telas atualmente disponíveis estão:

- Login;
- Dashboard;
- Visitantes;
- Autorizações;
- Controle de Acesso;
- Encomendas.

O frontend se comunica com a API Spring Boot através de requisições HTTP.

A configuração de CORS permite a comunicação entre o ambiente local do Vite e o backend durante o desenvolvimento.

---

# Dashboard

O Dashboard apresenta a estrutura visual planejada para indicadores e informações resumidas do condomínio.

Neste estágio do projeto, parte dos dados apresentados no Dashboard ainda utiliza informações demonstrativas.

A integração desses indicadores com dados reais da API permanece como evolução futura.

---

# Login e segurança

O sistema já utiliza BCrypt para processamento das senhas armazenadas.

```text
Senha
  ↓
BCrypt
  ↓
Hash
  ↓
PostgreSQL
```

Entretanto, a autenticação apresentada atualmente no frontend é **simulada para fins acadêmicos e de demonstração**.

O sistema ainda não utiliza um mecanismo completo de autenticação e autorização de rotas, como JWT ou sessões autenticadas.

Antes de qualquer utilização em ambiente de produção ainda seriam necessárias medidas como:

- autenticação real;
- autorização baseada em perfil;
- proteção completa dos endpoints;
- gerenciamento seguro de sessão ou tokens;
- auditoria ampliada;
- políticas de segurança;
- testes adicionais;
- revisão de credenciais e infraestrutura.

---

# Tratamento de erros

A API possui tratamento centralizado de exceções através do `GlobalExceptionHandler`.

São tratados cenários como:

- registros não encontrados;
- documentos ou e-mails duplicados;
- regras de negócio inválidas;
- dados de entrada inválidos;
- operações incompatíveis com o estado atual do registro.

Isso permite que o frontend receba respostas HTTP mais adequadas em situações de erro.

---

# Banco de dados

O projeto utiliza PostgreSQL 16.

O banco contempla tabelas relacionadas a:

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

As alterações de estrutura são controladas pelo Flyway.

Migrations já executadas não devem ser modificadas, pois o Flyway utiliza checksum para garantir a integridade do histórico.

---

# Docker

O PostgreSQL é executado localmente através do Docker.

Configuração atual:

```text
PostgreSQL 16
porta local: 5434
porta container: 5432
container: condosys-postgres
```

Para iniciar:

```bash
docker compose up -d
```

Para verificar:

```bash
docker exec condosys-postgres \
  pg_isready -U condosys -d condosys_db
```

---

# Configuração do ambiente

O projeto utiliza variáveis de ambiente para dados de configuração.

Existe um arquivo de exemplo:

```text
.env.example
```

Para criar o arquivo local:

```bash
cp .env.example .env
```

Exemplo:

```env
DB_NAME=condosys_db
DB_USER=condosys
DB_PASSWORD=troque_por_uma_senha_segura
```

O arquivo `.env` contém dados locais e não deve ser versionado.

---

# Executando o backend

## Pré-requisitos

```text
Java 21
Docker
Docker Compose
Git
```

Clone o projeto:

```bash
git clone git@github.com:armandor123/CondoSys-2.0.git
cd CondoSys-2.0
```

Crie o ambiente:

```bash
cp .env.example .env
```

Inicie o PostgreSQL:

```bash
docker compose up -d
```

Carregue as variáveis no Linux:

```bash
set -a
source .env
set +a
```

Execute o backend:

```bash
./mvnw spring-boot:run
```

A API será disponibilizada em:

```text
http://localhost:8080
```

---

# Executando o frontend

Entre no diretório:

```bash
cd frontend
```

Instale as dependências quando necessário:

```bash
npm install
```

Execute:

```bash
npm run dev
```

Por padrão, o ambiente local do frontend utiliza:

```text
http://localhost:5173
```

---

# Testes

Para executar os testes do backend:

```bash
./mvnw test
```

A suíte atual possui testes unitários das regras do módulo Administrador e teste de carregamento do contexto Spring.

Além dos testes automatizados, foram realizados testes funcionais dos principais fluxos integrados.

Entre os fluxos já validados estão:

```text
Visitante → Cadastro → Consulta

Encomenda
RECEBIDA → AGUARDANDO_RETIRADA → ENTREGUE

Autorização
ATIVA → Entrada → UTILIZADA

Controle de acesso
Entrada → Saída → Histórico
```

---

# Status atual

```text
Backend Spring Boot            ✔ Implementado
Frontend React                 ✔ Implementado
PostgreSQL                     ✔ Configurado
Flyway                         ✔ Configurado
Docker                         ✔ Configurado
Pessoa                         ✔ Implementado
Usuário                        ✔ Implementado
Administrador                  ✔ Implementado
Porteiro                       ✔ Implementado
Proprietário                   ✔ Implementado
Unidade                        ✔ Implementado
Visitante                      ✔ Implementado
Encomenda                      ✔ Implementado
Autorização                    ✔ Implementado
Controle de Acesso             ✔ Implementado
Áreas Comuns                   ✔ Backend implementado
Reservas                       ✔ Backend implementado
Dashboard                      ◐ Integração parcial
Login                          ◐ Simulado para o MVP
Prestadores                    ◐ Estrutura em evolução
Autenticação real              ○ Planejada
Autorização por perfil         ○ Planejada
Auditoria avançada             ○ Planejada
```

Legenda:

```text
✔ implementado
◐ parcialmente implementado
○ planejado
```

---

# Próximas etapas

O desenvolvimento seguirá de forma incremental.

Entre as melhorias futuras estão:

- autenticação real;
- autorização baseada em perfis;
- ampliação dos testes automatizados;
- integração completa dos indicadores do Dashboard;
- evolução do módulo de prestadores;
- integração completa de áreas comuns e reservas com o frontend;
- expansão da auditoria;
- documentação da API;
- aperfeiçoamento das regras de autorização;
- melhorias de segurança;
- preparação para implantação.

---

# Finalidade acadêmica

O CondoSys 2.0 é desenvolvido como Projeto Integrador e permite aplicar, em um sistema único, conhecimentos relacionados a:

- Programação Orientada a Objetos;
- Engenharia de Software;
- Banco de Dados;
- APIs REST;
- arquitetura em camadas;
- desenvolvimento web;
- persistência de dados;
- modelagem de sistemas;
- testes automatizados;
- controle de versão;
- segurança de software;
- desenvolvimento colaborativo.

O projeto demonstra a evolução de uma aplicação desktop inicial para uma arquitetura web dividida entre frontend, backend e banco de dados, mantendo o desenvolvimento orientado às necessidades encontradas na gestão condominial.
