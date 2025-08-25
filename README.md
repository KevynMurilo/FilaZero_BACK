# **FilaZero - Sistema de Gerenciamento de Filas de Atendimento**

> Um sistema moderno, robusto e em tempo real para gerenciamento de filas de atendimento em repartições públicas, como prefeituras, focado na eficiência operacional e na melhoria da experiência do cidadão.

## **Índice**

1.  [Visão Geral do Projeto](https://www.google.com/search?q=%231-vis%C3%A3o-geral-do-projeto)
2.  [Funcionalidades Principais](https://www.google.com/search?q=%232-funcionalidades-principais)
3.  [Modelagem do Banco de Dados](https://www.google.com/search?q=%233-modelagem-do-banco-de-dados)
4.  [Arquitetura da Solução](https://www.google.com/search?q=%234-arquitetura-da-solu%C3%A7%C3%A3o)
5.  [Estrutura da API REST](https://www.google.com/search?q=%235-estrutura-da-api-rest)
6.  [Fluxo de Uso Típico](https://www.google.com/search?q=%236-fluxo-de-uso-t%C3%ADpico)
7.  [Tecnologias Utilizadas](https://www.google.com/search?q=%237-tecnologias-utilizadas)
8.  [Configuração do Ambiente de Desenvolvimento](https://www.google.com/search?q=%238-configura%C3%A7%C3%A3o-do-ambiente-de-desenvolvimento)
9.  [Próximos Passos e Melhorias](https://www.google.com/search?q=%239-pr%C3%B3ximos-passos-e-melhorias)

-----

### **1. Visão Geral do Projeto**

O **FilaZero** foi concebido para modernizar o atendimento ao público em prefeituras e outras entidades governamentais. O sistema substitui o controle manual de senhas por uma plataforma digital centralizada, capaz de gerenciar múltiplas filas para diferentes setores (ex: IPTU, Recursos Humanos, Protocolo) simultaneamente.

O coração do sistema é um painel de chamadas ("telão") que exibe as senhas em tempo real, informando o cidadão qual senha está sendo atendida, qual é a próxima e as últimas que foram chamadas, tudo atualizado instantaneamente sem a necessidade de recarregar a página, graças ao uso de WebSockets.

A plataforma também oferece uma API REST completa para administração, permitindo que gestores configurem pontos de atendimento, setores, guichês e usuários com diferentes níveis de permissão.

### **2. Funcionalidades Principais**

  - **Gerenciamento Multi-Setor:** Capacidade de criar e gerenciar filas independentes para múltiplos departamentos dentro de um mesmo local de atendimento.
  - **Filas com Prioridade:** Sistema de emissão de senhas com distinção entre atendimento `NORMAL` e `PRIORIDADE`, garantindo o cumprimento da lei e a agilidade para quem mais precisa.
  - **Painel de Chamadas em Tempo Real (Telão):** Exibição dinâmica das senhas (Atual, Próxima, Últimas Chamadas) com atualizações instantâneas via **WebSocket**.
  - **Sessões de Atendente:** Atendentes realizam "login" em guichês específicos, criando uma sessão de trabalho que vincula o atendente ao guichê para rastreabilidade e controle.
  - **API REST para Administração:** Endpoints seguros para gerenciar todos os aspectos do sistema: usuários, pontos de atendimento, setores (tipos de serviço), guichês e painéis.
  - **Controle de Acesso Baseado em Papéis (RBAC):** Perfis de usuário (`ATENDENTE`, `GESTOR`, `ADMIN`) para garantir que cada usuário tenha acesso apenas às funcionalidades pertinentes ao seu cargo.

### **3. Modelagem do Banco de Dados**

A estrutura do sistema é sustentada pelas seguintes entidades principais:

  - `User (usuários)`: Armazena os funcionários que interagem com o sistema (atendentes, gestores, administradores).
  - `ServicePoint (pontos_atendimento)`: Representa um local físico de atendimento (ex: "Prefeitura Central - Bloco A").
  - `ServiceType (tipos_servico)`: Representa um setor ou tipo de serviço que gera uma fila (ex: "Cadastro IPTU", com a sigla "IPT"). Está sempre associado a um `ServicePoint`.
  - `ServiceDesk (guiches_atendimento)`: Representa um guichê ou mesa de atendimento físico. Está associado a um `ServicePoint`.
  - `DeskSession (sessoes_guiche)`: Vincula um `User` a um `ServiceDesk` durante um período de trabalho. É o que define "quem está atendendo e onde".
  - `Ticket (senhas)`: A entidade central. Representa a senha gerada pelo cidadão. Contém o status, tipo de prioridade e os vínculos com o setor, o atendente e o guichê que a atendeu.
  - `DisplayPanel (paineis_atendimento)`: **(Proposta)** Representa um "telão" físico. Sua principal função é se associar a um ou mais `ServiceTypes` para saber quais filas deve exibir.

### **4. Arquitetura da Solução**

O sistema é construído sobre uma arquitetura de microsserviço único (monolito modular) utilizando **Spring Boot**.

1.  **Backend (Java/Spring Boot):**
      - Expõe uma **API REST** para operações de gerenciamento (CRUDs) e ações (chamar, finalizar senha).
      - Utiliza o **Spring Security** para autenticação e autorização.
      - Gerencia um **servidor WebSocket** para comunicação em tempo real.
2.  **Frontend (SPA - Ex: React, Angular ou Vue):**
      - **Aplicação do Atendente:** Consome a API REST para se autenticar, iniciar uma sessão e realizar as operações de chamada de senha.
      - **Aplicação do Gestor:** Consome a API REST para gerenciar as entidades do sistema.
      - **Aplicação do Painel (Telão):** Conecta-se ao endpoint WebSocket para receber atualizações e usa uma rota REST para obter o estado inicial da fila.
3.  **Banco de Dados:** Um banco de dados relacional (ex: PostgreSQL) para persistir os dados.

**Fluxo da Chamada em Tempo Real:**
`Atendente clica em "Chamar" (Frontend)` -\> `Requisição POST /api/tickets/call-next` -\> `TicketService atualiza o BD` -\> `TicketService chama o PanelService` -\> `PanelService envia mensagem via WebSocket` -\> `Todos os Telões conectados recebem a atualização`.

### **5. Estrutura da API REST**

A seguir, os principais recursos e seus endpoints:

| Recurso | Endpoint | Descrição |
| :--- | :--- | :--- |
| **Autenticação** | `POST /api/auth/login` | Autentica um usuário. |
| | `POST /api/auth/register` | Registra um novo usuário (pode ser restrito). |
| **Sessões** | `POST /api/sessions/start` | Atendente inicia uma sessão em um guichê. |
| | `POST /api/sessions/{id}/end` | Atendente finaliza sua sessão. |
| **Senhas** | `POST /api/tickets/generate` | Gera uma nova senha para um cidadão (público). |
| | `POST /api/tickets/call-next` | Atendente (via sessão) chama a próxima senha da fila. |
| | `POST /api/tickets/{id}/finish` | Atendente finaliza um atendimento. |
| | `POST /api/tickets/{id}/cancel` | Atendente cancela uma senha. |
| **Consultas** | `GET /api/tickets/queue/waiting` | Lista as senhas que estão aguardando. |
| | `GET /api/panel/view` | Retorna a visão completa para o painel (Atual, Próximo, Últimos). |
| **Gerenciamento**| `(CRUD) /api/users` | Gerencia os usuários do sistema. |
| | `(CRUD) /api/service-points` | Gerencia os pontos de atendimento. |
| | `(CRUD) /api/service-types` | Gerencia os setores/tipos de serviço. |
| | `(CRUD) /api/service-desks` | Gerencia os guichês. |

### **6. Fluxo de Uso Típico**

#### **Jornada do Cidadão:**

1.  Chega ao ponto de atendimento.
2.  Em um totem (ou com um recepcionista), seleciona o serviço desejado e se é prioritário.
3.  Uma senha física ou digital é gerada (`POST /api/tickets/generate`).
4.  O cidadão aguarda, observando o telão.
5.  A sua senha é chamada e o guichê é indicado no painel.
6.  Ele se dirige ao guichê e é atendido.

#### **Jornada do Atendente:**

1.  Faz login no sistema (`POST /api/auth/login`).
2.  Inicia sua jornada de trabalho selecionando o guichê onde está (`POST /api/sessions/start`). O sistema retorna o `deskSessionId`.
3.  Clica no botão "Chamar Próximo". O frontend envia `POST /api/tickets/call-next` com o `deskSessionId`.
4.  O sistema atribui a próxima senha a ele e notifica o painel via WebSocket.
5.  O atendente realiza o atendimento.
6.  Ao finalizar, clica em "Finalizar Atendimento" (`POST /api/tickets/{id}/finish`). O sistema atualiza o status da senha e notifica o painel.
7.  Repete os passos 3-6.
8.  Ao final do turno, encerra sua sessão (`POST /api/sessions/{id}/end`).

### **7. Tecnologias Utilizadas**

  - **Backend:** Java 17+, Spring Boot 3.x, Spring Security, Spring Data JPA, Spring WebSockets (com STOMP).
  - **Persistência:** Hibernate, PostgreSQL (recomendado) ou H2 (para desenvolvimento).
  - **Build:** Apache Maven.
  - **Utilitários:** Lombok.
  - **Documentação da API:** OpenAPI 3 (Swagger).

### **8. Configuração do Ambiente de Desenvolvimento**

**Pré-requisitos:**

  - JDK 17 ou superior.
  - Apache Maven 3.6+.
  - Uma instância de PostgreSQL ou outro banco de dados relacional.

**Passos para Executar:**

1.  Clone o repositório.
2.  Configure o arquivo `src/main/resources/application.properties` com as credenciais do seu banco de dados:
    ```properties
    # Configuração do Banco de Dados (PostgreSQL)
    spring.datasource.url=jdbc:postgresql://localhost:5432/fila_zero_db
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

    # Configuração do Servidor
    server.port=8080
    ```
3.  Execute o projeto com o comando Maven:
    ```bash
    mvn spring-boot:run
    ```
4.  A API estará disponível em `http://localhost:8080`.
5.  A documentação do Swagger estará em `http://localhost:8080/swagger-ui.html`.

### **9. Próximos Passos e Melhorias**

  - **[ ] Módulo de Estatísticas:** Criar endpoints para gerar relatórios (tempo médio de espera, tempo de atendimento, produtividade por atendente).
  - **[ ] Paginação:** Implementar paginação em todas as rotas que retornam listas.
  - **[ ] Gerenciamento de Painéis:** Desenvolver o CRUD completo para a entidade `DisplayPanel`.
  - **[ ] Notificações ao Cidadão:** Integrar com serviços de SMS ou WhatsApp para notificar o cidadão quando sua senha estiver próxima de ser chamada.
  - **[ ] Múltiplos Idiomas:** Adicionar suporte à internacionalização (i18n).
  - **[ ] Testes:** Escrever testes unitários e de integração para garantir a qualidade e a estabilidade do código.
