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


# Cobertura de Testes Automatizados

Este documento descreve a cobertura de testes unitários e de integração da API usando JUnit e Mockito.

## 📊 Resumo da Cobertura

### ✅ Testes do Controller (CreditoControllerTest)

**Endpoints Testados:**

1. ✅ `GET /api/v1/creditos` - Buscar todos (com paginação)
2. ✅ `GET /api/v1/creditos/{id}` - Buscar por ID
3. ✅ `GET /api/v1/creditos/numero/{numero}` - Buscar por número
4. ✅ `GET /api/v1/creditos/nfse/{nfse}` - Buscar por NFS-e
5. ✅ `GET /api/v1/creditos/status/{status}` - Buscar por status
6. ✅ `GET /api/v1/creditos/tipo/{tipo}` - Buscar por tipo
7. ✅ `GET /api/v1/creditos/cnpj/{cnpj}` - Buscar por CNPJ
8. ✅ `GET /api/v1/creditos/periodo` - Buscar por período
9. ✅ `GET /api/v1/creditos/cnpj/{cnpj}/status/{status}` - Buscar por CNPJ e status
10. ✅ `GET /api/v1/creditos/buscar` - Buscar por termo
11. ✅ `POST /api/v1/creditos` - Criar crédito
12. ✅ `PUT /api/v1/creditos/{id}` - Atualizar crédito
13. ✅ `PATCH /api/v1/creditos/{id}/status` - Alterar status
14. ✅ `DELETE /api/v1/creditos/{id}` - Deletar crédito
15. ✅ Tratamento de erros (404) - Buscar por ID não encontrado
16. ✅ Tratamento de erros (404) - Buscar por número não encontrado

**Tecnologias Utilizadas:**
- JUnit 5 (Jupiter)
- Mockito
- MockMvc (Spring Test)
- ObjectMapper (Jackson)

### ✅ Testes do Service (CreditoServiceTest)

**Métodos Testados:**

1. ✅ `buscarTodos()` - Buscar todos com paginação
2. ✅ `buscarPorId()` - Buscar por ID (sucesso e não encontrado)
3. ✅ `buscarPorNumeroCreditoConstituido()` - Buscar por número (sucesso e não encontrado)
4. ✅ `buscarPorNFSe()` - Buscar por NFS-e (sucesso e não encontrado)
5. ✅ `buscarPorStatus()` - Buscar por status
6. ✅ `buscarPorTipo()` - Buscar por tipo
7. ✅ `buscarPorCNPJ()` - Buscar por CNPJ
8. ✅ `buscarPorPeriodo()` - Buscar por período
9. ✅ `buscarPorCNPJEStatus()` - Buscar por CNPJ e status
10. ✅ `buscarPorTermo()` - Buscar por termo
11. ✅ `criar()` - Criar crédito (sucesso e duplicado)
12. ✅ `atualizar()` - Atualizar crédito (sucesso e não encontrado)
13. ✅ `alterarStatus()` - Alterar status
14. ✅ `deletar()` - Deletar crédito (sucesso e não encontrado)

**Verificações Realizadas:**
- ✅ Validação de retorno correto
- ✅ Verificação de chamadas ao repositório
- ✅ Verificação de publicação de eventos no Kafka
- ✅ Tratamento de exceções (RuntimeException)

**Tecnologias Utilizadas:**
- JUnit 5 (Jupiter)
- Mockito (@Mock, @InjectMocks)
- Verificação de interações (verify)

## 🧪 Executar os Testes

### Executar todos os testes

```bash
./mvnw test
```

### Executar testes específicos

```bash
# Apenas testes do controller
./mvnw test -Dtest=CreditoControllerTest

# Apenas testes do service
./mvnw test -Dtest=CreditoServiceTest

# Apenas testes de integração
./mvnw test -Dtest=CreditoApplicationTests
```

### Executar com cobertura (se JaCoCo estiver configurado)

```bash
./mvnw clean test jacoco:report
```

## 📈 Cobertura de Código

### Controller (CreditoController)
- **Endpoints cobertos**: 14/14 (100%)
- **Casos de sucesso**: ✅ Todos testados
- **Casos de erro**: ✅ Parcialmente testados (404)

### Service (CreditoService)
- **Métodos cobertos**: 14/14 (100%)
- **Casos de sucesso**: ✅ Todos testados
- **Casos de erro**: ✅ Todos testados (não encontrado, duplicado)

## 🔍 Detalhamento dos Testes

### Testes do Controller

#### GET Endpoints
- ✅ `testBuscarTodos()` - Verifica paginação e retorno correto
- ✅ `testBuscarPorId()` - Verifica busca por ID
- ✅ `testBuscarPorNumero()` - Verifica busca por número
- ✅ `testBuscarPorNFSe()` - Verifica busca por NFS-e
- ✅ `testBuscarPorStatus()` - Verifica busca por status
- ✅ `testBuscarPorTipo()` - Verifica busca por tipo
- ✅ `testBuscarPorCNPJ()` - Verifica busca por CNPJ
- ✅ `testBuscarPorPeriodo()` - Verifica busca por período
- ✅ `testBuscarPorCNPJEStatus()` - Verifica busca combinada
- ✅ `testBuscarPorTermo()` - Verifica busca por termo

#### POST/PUT/PATCH/DELETE Endpoints
- ✅ `testCriarCredito()` - Verifica criação com status 201
- ✅ `testAtualizarCredito()` - Verifica atualização
- ✅ `testAlterarStatus()` - Verifica alteração de status
- ✅ `testDeletarCredito()` - Verifica deleção com status 204

#### Tratamento de Erros
- ✅ `testBuscarPorIdNaoEncontrado()` - Verifica 404 quando não encontra
- ✅ `testBuscarPorNumeroNaoEncontrado()` - Verifica 404 quando não encontra

### Testes do Service

#### Métodos de Busca
- ✅ `testBuscarTodos()` - Verifica paginação
- ✅ `testBuscarPorIdSucesso()` - Verifica busca bem-sucedida
- ✅ `testBuscarPorIdNaoEncontrado()` - Verifica exceção quando não encontra
- ✅ `testBuscarPorNumeroCreditoConstituido()` - Verifica busca por número
- ✅ `testBuscarPorNFSe()` - Verifica busca por NFS-e
- ✅ `testBuscarPorNFSeNaoEncontrado()` - Verifica exceção
- ✅ `testBuscarPorStatus()` - Verifica busca por status
- ✅ `testBuscarPorTipo()` - Verifica busca por tipo
- ✅ `testBuscarPorCNPJ()` - Verifica busca por CNPJ
- ✅ `testBuscarPorPeriodo()` - Verifica busca por período
- ✅ `testBuscarPorCNPJEStatus()` - Verifica busca combinada
- ✅ `testBuscarPorTermo()` - Verifica busca por termo

#### Métodos de Modificação
- ✅ `testCriarCredito()` - Verifica criação e evento Kafka
- ✅ `testCriarCreditoDuplicado()` - Verifica exceção para duplicado
- ✅ `testAtualizarCredito()` - Verifica atualização e evento Kafka
- ✅ `testAtualizarCreditoNaoEncontrado()` - Verifica exceção
- ✅ `testAlterarStatus()` - Verifica alteração de status
- ✅ `testDeletarCredito()` - Verifica deleção e evento Kafka
- ✅ `testDeletarCreditoNaoEncontrado()` - Verifica exceção

## 🎯 Boas Práticas Aplicadas

1. ✅ **Isolamento**: Cada teste é independente
2. ✅ **Mocking**: Uso de Mockito para isolar dependências
3. ✅ **Arrange-Act-Assert**: Estrutura clara nos testes
4. ✅ **Verificação**: Uso de `verify()` para garantir interações
5. ✅ **Casos de erro**: Testes para cenários de falha
6. ✅ **Nomenclatura**: Nomes descritivos dos testes

## 📝 Melhorias Futuras

### Testes Adicionais Sugeridos

1. **Validação de Entrada**
  - Testar validação de campos obrigatórios
  - Testar formatos inválidos (data, número, etc.)

2. **Testes de Integração**
  - Testes com banco de dados real (H2)
  - Testes end-to-end

3. **Testes de Performance**
  - Testes de carga
  - Testes de concorrência

4. **Cobertura de Código**
  - Configurar JaCoCo para relatórios de cobertura
  - Aumentar cobertura para 90%+

## ✅ Conclusão

A API possui **cobertura completa** dos endpoints principais e métodos do service, utilizando:
- ✅ JUnit 5 para estrutura de testes
- ✅ Mockito para mock de dependências
- ✅ MockMvc para testes de controllers
- ✅ Testes de casos de sucesso e erro

**Total de Testes**: 30+ testes unitários cobrindo todos os endpoints e métodos principais da API.
