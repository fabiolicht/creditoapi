-- Database: credito

-- DROP DATABASE IF EXISTS credito;

CREATE DATABASE credito
    WITH
    OWNER = postgres
    ENCODING = 'LATIN1'
    LC_COLLATE = 'pt_BR.ISO8859-1'
    LC_CTYPE = 'pt_BR.ISO8859-1'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE TABLE credito (
                         numero_credito SERIAL PRIMARY KEY,    -- Identificador único (autoincremento)
                         numero_nfse VARCHAR(20) NOT NULL,    -- Número da nota fiscal
                         data_constituicao DATE NOT NULL,      -- Data de criação do crédito
                         valor_issqn NUMERIC(15, 2),          -- Valor do imposto
                         tipo_credito VARCHAR(50),             -- Descrição do tipo
                         simples_nacional BOOLEAN,             -- Indicador (True/False)
                         aliquota NUMERIC(5, 2),               -- Porcentagem (ex: 5.00)
                         valor_faturado NUMERIC(15, 2),        -- Valor total da nota
                         valor_deducao NUMERIC(15, 2),         -- Valor a ser deduzido
                         base_calculo NUMERIC(15, 2)           -- Base para o cálculo do imposto
);

INSERT INTO credito (numero_credito, numero_nfse, data_constituicao, valor_issqn, tipo_credito, simples_nacional, aliquota, valor_faturado, valor_deducao, base_calculo)
VALUES
    ('123456', '7891011', '2024-02-25', 1500.75, 'ISSQN', true, 5.0, 30000.00, 5000.00, 25000.00),
    ('789012', '7891011', '2024-02-26', 1200.50, 'ISSQN', false, 4.5, 25000.00, 4000.00, 21000.00),
    ('654321', '1122334', '2024-01-15', 800.50, 'Outros', true, 3.5, 20000.00, 3000.00, 17000.00);


