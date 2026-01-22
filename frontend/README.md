# Frontend Angular - Consulta de Créditos

Frontend em Angular para consulta de créditos através da API REST.

## Funcionalidades

- ✅ Busca por número do crédito
- ✅ Busca por número da NFS-e
- ✅ Exibição dos resultados em tabela
- ✅ Design responsivo para dispositivos móveis
- ✅ Interface moderna e intuitiva

## Pré-requisitos

- Node.js 18+ e npm
- Angular CLI 17+

## Instalação

1. Instale as dependências:

```bash
npm install
```

2. Instale o Angular CLI globalmente (se ainda não tiver):

```bash
npm install -g @angular/cli
```

## Executar a Aplicação

1. Certifique-se de que a API Spring Boot está rodando em `http://localhost:8080`

2. Execute o frontend:

```bash
npm start
```

ou

```bash
ng serve
```

3. Acesse no navegador: `http://localhost:4200`

## Build para Produção

```bash
ng build
```

Os arquivos serão gerados na pasta `dist/credito-frontend`

## Estrutura do Projeto

```
frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   └── busca-credito/     # Componente de busca
│   │   ├── models/                # Interfaces/Modelos
│   │   ├── services/              # Serviços HTTP
│   │   └── app.component.ts       # Componente principal
│   ├── index.html
│   ├── main.ts
│   └── styles.css
├── angular.json
├── package.json
└── tsconfig.json
```

## Configuração da API

A URL da API está configurada em `src/app/services/credito.service.ts`:

```typescript
private apiUrl = 'http://localhost:8080/api/v1/creditos';
```

Se a API estiver em outro endereço, altere essa variável.

## Responsividade

O frontend é totalmente responsivo e funciona bem em:

- Desktop (1200px+)
- Tablet (768px - 1199px)
- Mobile (até 767px)

Em dispositivos móveis, a tabela pode ser rolada horizontalmente se necessário.
