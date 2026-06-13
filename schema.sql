-- ============================================================
-- Taça Lab — Schema completo
-- Corre este script no SQL Editor do Neon
-- ============================================================

-- artesa
CREATE TABLE IF NOT EXISTS artesa (
    id_artesa  SERIAL PRIMARY KEY,
    email      VARCHAR(255),
    nome       VARCHAR(255),
    password   VARCHAR(255)
);

-- utilizador
CREATE TABLE IF NOT EXISTS utilizador (
    id_utilizador    SERIAL PRIMARY KEY,
    nome_empresa     VARCHAR(255) NOT NULL,
    nif              VARCHAR(9)   NOT NULL,
    email            VARCHAR(255) NOT NULL,
    password         VARCHAR(255) NOT NULL,
    telefone         VARCHAR(15),
    morada_faturacao TEXT
);

-- projeto_personalizado
CREATE TABLE IF NOT EXISTS projeto_personalizado (
    id_projeto     SERIAL PRIMARY KEY,
    id_utilizador  INTEGER REFERENCES utilizador(id_utilizador),
    id_artesa      INTEGER REFERENCES artesa(id_artesa),
    titulo_projeto VARCHAR(255) NOT NULL,
    briefing       TEXT,
    data_criacao   TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    estado_atual   VARCHAR(255) DEFAULT 'briefing'
);

-- artigo_catalogo (id_projeto_origem created after projeto_personalizado due to FK)
CREATE TABLE IF NOT EXISTS artigo_catalogo (
    id_artigo          SERIAL PRIMARY KEY,
    nome               VARCHAR(255)    NOT NULL,
    preco_unitario     NUMERIC(10, 2)  NOT NULL,
    stock              INTEGER         DEFAULT 0,
    visivel            BOOLEAN         DEFAULT TRUE,
    foto_url           VARCHAR(500),
    id_projeto_origem  INTEGER         REFERENCES projeto_personalizado(id_projeto)
);

-- ficha_tecnica
CREATE TABLE IF NOT EXISTS ficha_tecnica (
    id_ficha             SERIAL PRIMARY KEY,
    id_projeto           INTEGER REFERENCES projeto_personalizado(id_projeto),
    id_artigo            INTEGER REFERENCES artigo_catalogo(id_artigo),
    tipo_barro           VARCHAR(100),
    cor_vidrado          VARCHAR(100),
    temperatura_cozedura INTEGER,
    tempo_secagem        VARCHAR(50),
    observacoes          TEXT,
    foto_design          VARCHAR(255),
    foto_prototipo       VARCHAR(255),
    ref_molde            VARCHAR(100)
);

-- encomenda_catalogo
CREATE TABLE IF NOT EXISTS encomenda_catalogo (
    id_encomenda  SERIAL PRIMARY KEY,
    id_utilizador INTEGER REFERENCES utilizador(id_utilizador),
    id_projeto    INTEGER REFERENCES projeto_personalizado(id_projeto),
    data_pedido   TIMESTAMPTZ    DEFAULT CURRENT_TIMESTAMP,
    valor_final   NUMERIC(10, 2) DEFAULT 0.00,
    estado        VARCHAR(255)   DEFAULT 'pendente'
);

-- item_encomenda
CREATE TABLE IF NOT EXISTS item_encomenda (
    id_item      SERIAL PRIMARY KEY,
    id_encomenda INTEGER REFERENCES encomenda_catalogo(id_encomenda),
    id_artigo    INTEGER REFERENCES artigo_catalogo(id_artigo),
    quantidade   INTEGER DEFAULT 1
);

-- reuniao
CREATE TABLE IF NOT EXISTS reuniao (
    id_reuniao SERIAL PRIMARY KEY,
    id_projeto INTEGER REFERENCES projeto_personalizado(id_projeto),
    data_hora  TIMESTAMPTZ  NOT NULL,
    tipo       VARCHAR(255),
    status     VARCHAR(255),
    local      VARCHAR(255)
);

-- mensagem_chat
CREATE TABLE IF NOT EXISTS mensagem_chat (
    id_mensagem              SERIAL PRIMARY KEY,
    id_projeto               INTEGER REFERENCES projeto_personalizado(id_projeto),
    id_remetente_utilizador  INTEGER REFERENCES utilizador(id_utilizador),
    id_remetente_artesa      INTEGER REFERENCES artesa(id_artesa),
    conteudo                 TEXT        NOT NULL,
    url_foto                 TEXT,
    data_envio               TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- fatura
CREATE TABLE IF NOT EXISTS fatura (
    id_fatura        SERIAL PRIMARY KEY,
    id_encomenda     INTEGER UNIQUE REFERENCES encomenda_catalogo(id_encomenda),
    numero_fatura    VARCHAR(30)  UNIQUE,
    nome             VARCHAR(255),
    nif              VARCHAR(20),
    morada           TEXT,
    cidade           VARCHAR(100),
    codigo_postal    VARCHAR(10),
    telefone         VARCHAR(20),
    metodo_pagamento VARCHAR(50),
    data_emissao     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- pagamento
CREATE TABLE IF NOT EXISTS pagamento (
    id_pagamento   SERIAL PRIMARY KEY,
    id_projeto     INTEGER REFERENCES projeto_personalizado(id_projeto),
    id_encomenda   INTEGER REFERENCES encomenda_catalogo(id_encomenda),
    fase           VARCHAR(20),
    descricao      TEXT,
    valor          NUMERIC(10, 2) NOT NULL,
    tipo_pagamento VARCHAR(50),
    data_pagamento TIMESTAMPTZ    DEFAULT CURRENT_TIMESTAMP,
    pago           BOOLEAN        DEFAULT FALSE
);
