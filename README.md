# Sistema de Crédito

Sistema completo para gerenciamento de créditos com API REST (Spring Boot 4.0.1) e Frontend Angular para consulta de créditos.

## 🚀 Início Rápido - Acessar a Interface Web

### Passo 1: Iniciar os serviços (PostgreSQL e Kafka)
```bash
docker-compose up -d
```

### Passo 2: Executar a API Backend
```bash
./mvnw spring-boot:run
```
A API estará disponível em: **http://localhost:8080**

### Passo 3: Executar o Frontend Angular
```bash
cd frontend
npm install    # Primeira vez apenas
npm start       # Inicia o servidor na porta 4200
```

### Passo 4: Acessar a Interface Web
Abra seu navegador e acesse: **http://localhost:4200**

**🎯 A interface permite:**
- 🔍 Buscar créditos por número do crédito
- 🔍 Buscar créditos por número da NFS-e
- 📊 Visualizar resultados em tabela responsiva
- 📱 Interface otimizada para dispositivos móveis

**📝 Nota**: Certifique-se de que a API Backend está rodando em `http://localhost:8080` antes de usar o frontend.

---

## Arquitetura

- **Backend**: API REST Spring Boot 4.0.1
- **Frontend**: Angular 17 (aplicação standalone)
- **Banco de Dados**: PostgreSQL
- **Mensageria**: Apache Kafka

## Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- Node.js 18+ e npm (para o frontend)
- Docker e Docker Compose (para executar PostgreSQL e Kafka)

## 📋 Guia Completo de Execução

### 1. Iniciar os serviços (PostgreSQL e Kafka)

```bash
docker-compose up -d
```

Isso iniciará:

- **PostgreSQL** na porta `5432`
- **Kafka** na porta `9092`
- **Kafka UI** na porta `8081` (interface web para gerenciar Kafka)

### 2. Executar a aplicação Spring Boot (Backend API)

#### Opção A: Usando Maven

```bash
./mvnw spring-boot:run
```

ou no Windows:

```bash
mvnw.cmd spring-boot:run
```

#### Opção B: Usando o Maven Wrapper

```bash
./mvnw clean package
java -jar target/credito-0.0.1-SNAPSHOT.war
```

#### Opção C: Executar diretamente no IntelliJ IDEA

1. Abra a classe `CreditoApplication.java`
2. Clique com o botão direito e selecione `Run 'CreditoApplication'`

### 3. Verificar se a API está rodando

A API estará disponível em: **http://localhost:8080**

Teste se está funcionando:
```bash
curl http://localhost:8080/api/v1/creditos
```

### 4. Executar o Frontend Angular (Interface Web)

Veja a seção [Interface Web (Frontend Angular)](#-interface-web-frontend-angular) acima para instruções detalhadas.

**Resumo rápido:**
```bash
cd frontend
npm install    # Primeira vez apenas
npm start       # Inicia o servidor na porta 4200
```

Acesse: **http://localhost:4200**

## Endpoints da API

### Base URL: `http://localhost:8080/api/v1/creditos`

#### GET - Listar todos os créditos (com paginação)

```
GET /api/v1/creditos?page=0&size=10&sortBy=id&direction=DESC
```

#### GET - Buscar crédito por ID

```
GET /api/v1/creditos/{id}
```

#### GET - Buscar crédito por número

```
GET /api/v1/creditos/numero/{numero}
```

#### GET - Buscar crédito por NFS-e

```
GET /api/v1/creditos/nfse/{nfse}
```

#### GET - Buscar créditos por status

```
GET /api/v1/creditos/status/{status}?page=0&size=10
```

#### GET - Buscar créditos por tipo

```
GET /api/v1/creditos/tipo/{tipo}?page=0&size=10
```

#### GET - Buscar créditos por CNPJ

```
GET /api/v1/creditos/cnpj/{cnpj}
```

#### GET - Buscar créditos por período

```
GET /api/v1/creditos/periodo?dataInicio=2024-01-01&dataFim=2024-12-31
```

#### POST - Criar novo crédito

```
POST /api/v1/creditos
Content-Type: application/json

{
  "numeroCreditoConstituido": "CR001",
  "numeroNFSe": "NFS001",
  "dataConstituicao": "2024-01-15",
  "valorISSQN": 1000.00,
  "tipoCredito": "PRINCIPAL",
  "descricao": "Descrição do crédito",
  "status": "ATIVO",
  "responsavel": "João Silva",
  "cnpjEmpresa": "12345678000100"
}
```

#### PUT - Atualizar crédito

```
PUT /api/v1/creditos/{id}
Content-Type: application/json

{
  "numeroNFSe": "NFS002",
  "valorISSQN": 1500.00,
  ...
}
```

#### PATCH - Alterar status do crédito

```
PATCH /api/v1/creditos/{id}/status?novoStatus=INATIVO
```

#### DELETE - Deletar crédito

```
DELETE /api/v1/creditos/{id}
```

## Testando a API

### Usando cURL

```bash
# Listar todos os créditos
curl http://localhost:8080/api/v1/creditos

# Criar um novo crédito
curl -X POST http://localhost:8080/api/v1/creditos \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCreditoConstituido": "CR001",
    "numeroNFSe": "NFS001",
    "dataConstituicao": "2024-01-15",
    "valorISSQN": 1000.00,
    "tipoCredito": "PRINCIPAL",
    "descricao": "Crédito de teste",
    "status": "ATIVO",
    "responsavel": "João Silva",
    "cnpjEmpresa": "12345678000100"
  }'

# Buscar crédito por ID
curl http://localhost:8080/api/v1/creditos/1
```

### Usando Postman ou Insomnia

1. Importe a coleção de endpoints acima
2. Configure a base URL como `http://localhost:8080/api/v1/creditos`
3. Teste os endpoints

### Usando Swagger/OpenAPI (se configurado)

Se você adicionar o SpringDoc OpenAPI, poderá acessar:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

## Serviços Auxiliares

### Kafka UI

Interface web para gerenciar Kafka: **http://localhost:8081**

### PostgreSQL

- Host: `localhost`
- Porta: `5432`
- Database: `credito_db`
- Usuário: `postgres`
- Senha: `z1x2c3v4`

## Parar os Serviços

```bash
docker-compose down
```

Para remover também os volumes (dados do banco):

```bash
docker-compose down -v
```

## 🌐 Interface Web (Frontend Angular)

### Como Acessar a Interface Web

A interface web está disponível após executar os seguintes passos:

#### 1. Instalar Dependências (primeira vez apenas)

```bash
cd frontend
npm install
```

**Nota**: Se você ainda não tem o Angular CLI instalado globalmente:
```bash
npm install -g @angular/cli@17
```

#### 2. Executar o Frontend

```bash
npm start
```

ou

```bash
ng serve
```

#### 3. Acessar no Navegador

Abra seu navegador e acesse: **http://localhost:4200**

### ⚠️ Importante

- Certifique-se de que a **API Backend está rodando** em `http://localhost:8080` antes de usar o frontend
- O frontend se conecta automaticamente à API através de um proxy configurado

### Funcionalidades da Interface Web

- ✅ **Busca por número do crédito**: Digite o número do crédito e clique em buscar
- ✅ **Busca por número da NFS-e**: Selecione "Número da NFS-e" e digite o número
- ✅ **Tabela de resultados**: Exibe todos os dados do crédito encontrado
- ✅ **Design responsivo**: Funciona perfeitamente em celulares, tablets e desktops
- ✅ **Interface moderna**: Design limpo e intuitivo
- ✅ **Tratamento de erros**: Mensagens claras quando não encontra resultados

### Estrutura da Interface

A interface possui:
- **Cabeçalho**: Título da aplicação
- **Formulário de busca**: 
  - Seleção do tipo de busca (Crédito ou NFS-e)
  - Campo de entrada
  - Botões de Buscar e Limpar
- **Tabela de resultados**: Exibe os dados quando encontra resultados
- **Mensagens**: Feedback visual para o usuário

### Características da Interface

A interface possui:
- Layout responsivo que se adapta ao tamanho da tela
- Cores e badges para status dos créditos (Ativo, Inativo, Pendente, etc.)
- Formatação de datas e valores monetários em português (BRL)
- Mensagens de erro amigáveis
- Loading states durante as buscas

### Troubleshooting

**Problema**: A interface não encontra créditos
- ✅ Verifique se a API está rodando em `http://localhost:8080`
- ✅ Teste a API diretamente: `curl http://localhost:8080/api/v1/creditos`
- ✅ Verifique o console do navegador (F12) para erros

**Problema**: Erro de CORS
- ✅ O proxy está configurado automaticamente no `proxy.conf.json`
- ✅ Certifique-se de usar `npm start` (não `ng serve` diretamente)

**Problema**: Porta 4200 já em uso
- ✅ O Angular perguntará se deseja usar outra porta
- ✅ Ou pare o processo na porta 4200: `lsof -ti:4200 | xargs kill`

Para mais detalhes técnicos, consulte o [README do Frontend](frontend/README.md).

## Estrutura do Projeto

```
credito/
├── frontend/                      # Frontend Angular
│   ├── src/
│   │   └── app/
│   │       ├── components/        # Componentes Angular
│   │       ├── services/          # Serviços HTTP
│   │       └── models/            # Interfaces TypeScript
│   └── package.json
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── fabiolicht/credito/
│   │   │       ├── controller/    # REST Controllers
│   │   │       ├── service/       # Lógica de negócio
│   │   │       ├── repository/    # Repositórios JPA
│   │   │       ├── model/         # Entidades
│   │   │       ├── dto/           # Data Transfer Objects
│   │   │       ├── config/        # Configurações
│   │   │       └── kafka/         # Consumidores Kafka
│   │   └── resources/
│   │       └── application.properties
│   └── test/                      # Testes
├── compose.yaml                   # Docker Compose
└── pom.xml                        # Dependências Maven
```

## Desenvolvimento

### Executar testes

```bash
./mvnw test
```

### Build do projeto

```bash
./mvnw clean package
```

### Executar com perfil específico

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
