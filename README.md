# proj2

Aplicação Spring Boot com API REST para gestão de encomendas, orçamentos e projetos personalizados.

---

## Credenciais de Admin (Artesã)

Ao arrancar a aplicação, é criado automaticamente um utilizador admin caso ainda não exista.

| Campo    | Valor               |
|----------|---------------------|
| **Email**    | `admin@artesa.com`  |
| **Password** | `admin123`          |

---

## Como correr

```bash
./gradlew bootRun
```

A aplicação fica disponível em: `http://localhost:8080`

---

## Endpoints principais

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
| `GET`  | `/api/mensagens-chat/projeto/{projetoId}/total` | Devolve o total de mensagens do projeto |
| `POST` | `/api/mensagens-chat/projeto/{projetoId}/utilizador/{utilizadorId}` | Envia mensagem como utilizador |
| `POST` | `/api/mensagens-chat/projeto/{projetoId}/artesa/{artesaId}` | Envia mensagem como artesã |

### Reuniões
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/reunioes` | Lista todas as reuniões |
| `GET`  | `/api/reunioes/projeto/{projetoId}` | Lista reuniões de um projeto |
| `POST` | `/api/reunioes/projeto/{projetoId}/artesa/{artesaId}` | Marca reunião para um projeto com a artesã |
| `PUT`  | `/api/reunioes/{reuniaoId}/confirmar-presenca` | Confirma presença e altera estado para `confirmada` |
| `PUT`  | `/api/reunioes/{reuniaoId}/remarcar` | Remarca reunião com nova data/hora |
| `PUT`  | `/api/reunioes/{reuniaoId}/cancelar` | Cancela reunião |

### Encomendas por Projeto
| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/encomendas/projeto/{projetoId}/reencomendar` | Reencomenda com base na última encomenda completa do projeto |

### Fichas Técnicas
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/fichas-tecnicas` | Lista todas as fichas técnicas |
| `GET`  | `/api/fichas-tecnicas/projeto/{projetoId}` | Lista fichas técnicas de um projeto |
| `POST` | `/api/fichas-tecnicas/projeto/{projetoId}` | Cria ficha técnica para um projeto |
| `GET`  | `/api/fichas-tecnicas/artigo/{artigoId}` | Obtém a ficha técnica associada a um artigo |
| `POST` | `/api/fichas-tecnicas/artigo/{artigoId}` | Cria ficha técnica e associa ao artigo |
| `PUT`  | `/api/fichas-tecnicas/artigo/{artigoId}` | Atualiza ficha técnica associada ao artigo |
| `DELETE` | `/api/fichas-tecnicas/artigo/{artigoId}` | Remove ficha técnica associada ao artigo |
| `PUT`  | `/api/fichas-tecnicas/{id}` | Atualiza ficha técnica |
| `DELETE` | `/api/fichas-tecnicas/{id}` | Remove ficha técnica |

### Artigos de Catálogo
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/artigos-catalogo` | Lista todos os artigos do catálogo |
| `POST` | `/api/artigos-catalogo` | Cria um artigo do catálogo |
| `PUT`  | `/api/artigos-catalogo/{artigoId}` | Atualiza um artigo do catálogo |
| `DELETE` | `/api/artigos-catalogo/{artigoId}` | Remove um artigo do catálogo |
| `PUT`  | `/api/artigos-catalogo/{artigoId}/ficha-tecnica/{fichaId}` | Associa ficha técnica a um artigo |

### Pagamentos
| Método | URL | Descrição |
|--------|-----|-----------|
| `GET`  | `/api/pagamentos` | Lista todos os pagamentos |
| `POST` | `/api/pagamentos/orcamento/{orcamentoId}/pagar` | Cria/processa pagamento do orçamento no momento do pagamento |