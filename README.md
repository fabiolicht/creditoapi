# Sistema de Crédito

Sistema completo para gerenciamento de créditos com API REST (Spring Boot 4.0.1) e Frontend Angular para consulta de
créditos.

## Arquitetura

- **Backend**: API REST Spring Boot 4.0.1
- **Frontend**: Angular 17 (aplicação standalone)
- **Banco de Dados**: PostgreSQL
- **Mensageria**: Apache Kafka

## Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- Docker e Docker Compose (para executar PostgreSQL e Kafka)

## Como Executar

### 1. Iniciar os serviços (PostgreSQL e Kafka)

```bash
docker-compose up -d
```

Isso iniciará:

- **PostgreSQL** na porta `5432`
- **Kafka** na porta `9092`
- **Kafka UI** na porta `8081` (interface web para gerenciar Kafka)

### 2. Executar a aplicação Spring Boot

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

### 3. Acessar a API

A aplicação estará disponível em: **http://localhost:8080**

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
- Senha: `postgres`

## Parar os Serviços

```bash
docker-compose down
```

Para remover também os volumes (dados do banco):

```bash
docker-compose down -v
```

## Frontend Angular

O frontend está localizado na pasta `frontend/` e permite consultar créditos por número do crédito ou número da NFS-e.

### Executar o Frontend

1. Navegue até a pasta do frontend:

```bash
cd frontend
```

2. Instale as dependências:

```bash
npm install
```

3. Execute o servidor de desenvolvimento:

```bash
npm start
```

4. Acesse no navegador: **http://localhost:4200**

### Funcionalidades do Frontend

- ✅ Busca por número do crédito
- ✅ Busca por número da NFS-e
- ✅ Exibição dos resultados em tabela responsiva
- ✅ Design adaptado para dispositivos móveis
- ✅ Interface moderna e intuitiva

Para mais detalhes, consulte o [README do Frontend](frontend/README.md).

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
