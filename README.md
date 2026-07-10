# proj2 — Taça Lab (Backend / API REST)

Backend em **Spring Boot** do sistema **Taça Lab** (atelier de cerâmica). Expõe uma API
REST para gestão de utilizadores, artesãs, catálogo, projetos personalizados, orçamentos,
encomendas, fichas técnicas, chat, reuniões, pagamentos e faturas, com upload de fotos.

## Ecossistema Taça Lab

| Componente | Repositório | Stack |
|------------|-------------|-------|
| **Backend / API REST** (este repo) | [proj2](https://github.com/manuca17/proj2) | Spring Boot + PostgreSQL |
| **Portal Web** | [Sistemadeinformaocermica](https://github.com/manuca17/Sistemadeinformaocermica) | React + Vite |
| **App Desktop (admin)** | [AppDesktop](https://github.com/manuca17/AppDesktop) | JavaFX |

## Stack

- **Java 17** + **Spring Boot 3.3.5** (Web, Data JPA, JDBC)
- **PostgreSQL** (configurado para [Neon](https://neon.tech))
- **Cloudinary** para armazenamento de imagens
- **springdoc-openapi** (Swagger UI)
- **Gradle** (com wrapper `gradlew`)
- Jackson com estratégia de nomes **snake_case**

## Requisitos

- **JDK 17+**
- Uma base de dados **PostgreSQL** (local ou Neon)

## Configuração

A configuração está em `src/main/resources/application.properties`. Antes de correr,
define as credenciais da tua base de dados e do Cloudinary — de preferência por
**variáveis de ambiente** para não versionar segredos:

```properties
spring.datasource.url=jdbc:postgresql://<host>/<db>?sslmode=require
spring.datasource.username=<user>
spring.datasource.password=<password>
cloudinary.cloud-name=<cloud-name>
# cloudinary.api-key / cloudinary.api-secret conforme aplicável
```

O `schema.sql` na raiz contém o **esquema completo** da base de dados (tabelas `artesa`,
`utilizador`, `projeto_personalizado`, `artigo_catalogo`, etc.); podes corrê-lo no editor
SQL do Neon. O Hibernate está com `ddl-auto=update`.

Uploads são guardados na pasta `uploads/` (config. `app.upload.dir`), com limite de 50 MB.

## Como correr

```bash
./gradlew bootRun          # Linux/macOS
gradlew.bat bootRun        # Windows
```

A aplicação fica disponível em **`http://localhost:8080`**.

### Documentação da API (Swagger)

Com a aplicação a correr:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

## Credenciais de Admin (Artesã)

No arranque, o `DataInitializer` cria automaticamente um utilizador admin se ainda não existir:

| Campo    | Valor              |
|----------|--------------------|
| **Email**    | `admin@artesa.com` |
| **Password** | `admin123`         |

## Estrutura

```
src/main/java/com/example/proj2/
  Proj2Application.java        Classe principal
  config/                      DataInitializer, JacksonConfig, WebConfig (CORS)
  controllers/                 Endpoints REST (um por área de domínio)
  services/                    Lógica de negócio
  repository/                  Repositórios Spring Data JPA
  models/                      Entidades JPA (Utilizador, Artesa, ArtigoCatalogo,
                               ProjetoPersonalizado, Orcamento, Encomenda, Fatura,
                               FichaTecnica, MensagemChat, Reuniao, Pagamento, ...)
schema.sql                     Esquema completo da base de dados
uploads/                       Ficheiros carregados (imagens)
```

## Endpoints

A lista completa e interativa está no **Swagger UI**. Resumo por área:

### Autenticação / Utilizadores
| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/utilizadores/login` | Login de cliente |
| `POST` | `/api/utilizadores/registar` | Registo de cliente |
| `POST` | `/api/artesas/login` | Login de artesã/admin |

### Orçamentos
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/orcamentos` | Lista todos os orçamentos |
| `GET`  | `/api/orcamentos/projeto/{projetoId}` | Lista orçamentos de um projeto |
| `POST` | `/api/orcamentos/projeto/{projetoId}` | Cria orçamento para um projeto |

### Chat
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/mensagens-chat` | Lista todas as mensagens |
| `GET`  | `/api/mensagens-chat/projeto/{projetoId}` | Lista mensagens de um projeto |
| `GET`  | `/api/mensagens-chat/projeto/{projetoId}/total` | Total de mensagens do projeto |
| `POST` | `/api/mensagens-chat/projeto/{projetoId}/utilizador/{utilizadorId}` | Envia mensagem como utilizador |
| `POST` | `/api/mensagens-chat/projeto/{projetoId}/artesa/{artesaId}` | Envia mensagem como artesã |

### Reuniões
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/reunioes` | Lista todas as reuniões |
| `GET`  | `/api/reunioes/projeto/{projetoId}` | Lista reuniões de um projeto |
| `POST` | `/api/reunioes/projeto/{projetoId}/artesa/{artesaId}` | Marca reunião com a artesã |
| `PUT`  | `/api/reunioes/{reuniaoId}/confirmar-presenca` | Confirma presença (estado `confirmada`) |
| `PUT`  | `/api/reunioes/{reuniaoId}/remarcar` | Remarca reunião |
| `PUT`  | `/api/reunioes/{reuniaoId}/cancelar` | Cancela reunião |

### Encomendas
| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/encomendas/projeto/{projetoId}/reencomendar` | Reencomenda com base na última encomenda completa do projeto |

### Fichas Técnicas
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/fichas-tecnicas` | Lista todas as fichas técnicas |
| `GET`  | `/api/fichas-tecnicas/projeto/{projetoId}` | Fichas técnicas de um projeto |
| `POST` | `/api/fichas-tecnicas/projeto/{projetoId}` | Cria ficha técnica para um projeto |
| `GET`  | `/api/fichas-tecnicas/artigo/{artigoId}` | Ficha técnica de um artigo |
| `POST` | `/api/fichas-tecnicas/artigo/{artigoId}` | Cria e associa ficha técnica a um artigo |
| `PUT`  | `/api/fichas-tecnicas/artigo/{artigoId}` | Atualiza ficha técnica do artigo |
| `DELETE` | `/api/fichas-tecnicas/artigo/{artigoId}` | Remove ficha técnica do artigo |
| `PUT`  | `/api/fichas-tecnicas/{id}` | Atualiza ficha técnica |
| `DELETE` | `/api/fichas-tecnicas/{id}` | Remove ficha técnica |

### Artigos de Catálogo
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/artigos-catalogo` | Lista todos os artigos |
| `POST` | `/api/artigos-catalogo` | Cria um artigo |
| `PUT`  | `/api/artigos-catalogo/{artigoId}` | Atualiza um artigo |
| `DELETE` | `/api/artigos-catalogo/{artigoId}` | Remove um artigo |
| `PUT`  | `/api/artigos-catalogo/{artigoId}/ficha-tecnica/{fichaId}` | Associa ficha técnica a um artigo |

### Pagamentos
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/pagamentos` | Lista todos os pagamentos |
| `POST` | `/api/pagamentos/orcamento/{orcamentoId}/pagar` | Processa o pagamento de um orçamento |

### Upload
| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/upload/imagem` | Carrega uma imagem (multipart, até 50 MB) |

> Existem ainda controladores para **encomendas de catálogo**, **itens de encomenda**,
> **faturas**, **projetos personalizados** e **artesãs** — consulta o Swagger UI para a
> referência completa.
