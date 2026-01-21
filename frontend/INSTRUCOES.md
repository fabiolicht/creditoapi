# Instruções de Instalação e Execução do Frontend

## Passo a Passo

### 1. Instalar Node.js e npm

Certifique-se de ter Node.js 18+ instalado:
```bash
node --version
npm --version
```

Se não tiver, baixe em: https://nodejs.org/

### 2. Instalar Angular CLI

```bash
npm install -g @angular/cli@17
```

### 3. Instalar Dependências

Na pasta `frontend/`:
```bash
cd frontend
npm install
```

### 4. Executar o Frontend

```bash
npm start
```

ou

```bash
ng serve
```

O frontend estará disponível em: **http://localhost:4200**

### 5. Executar o Backend

Certifique-se de que a API Spring Boot está rodando em `http://localhost:8080`

## Estrutura Criada

✅ Componente de busca (`busca-credito`)
✅ Serviço para consumir a API (`credito.service`)
✅ Modelos TypeScript (`credito.model`)
✅ Estilos responsivos para mobile
✅ Tabela de resultados
✅ Tratamento de erros

## Funcionalidades Implementadas

- Busca por número do crédito
- Busca por número da NFS-e
- Exibição em tabela responsiva
- Design mobile-first
- Mensagens de erro amigáveis
- Loading states

## Próximos Passos

1. Execute `npm install` na pasta `frontend/`
2. Execute `npm start` para iniciar o servidor de desenvolvimento
3. Acesse http://localhost:4200 no navegador
4. Teste a busca por número de crédito ou NFS-e
