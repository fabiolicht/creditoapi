export interface Credito {
  id?: number;
  numeroCreditoConstituido: string;
  numeroNFSe: string;
  dataConstituicao: string;
  valorISSQN: number;
  tipoCredito: string;
  descricao?: string;
  status: string;
  dataRegistro?: string;
  dataAtualizacao?: string;
  responsavel?: string;
  cnpjEmpresa?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
