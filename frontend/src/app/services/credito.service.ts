import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Credito, PageResponse } from '../models/credito.model';

@Injectable({
  providedIn: 'root'
})
export class CreditoService {
  private apiUrl = '/api/v1/creditos';

  constructor(private http: HttpClient) { }

  buscarPorNumeroCredito(numero: string): Observable<Credito> {
    return this.http.get<Credito>(`${this.apiUrl}/numero/${numero}`);
  }

  buscarPorNFSe(nfse: string): Observable<Credito> {
    return this.http.get<Credito>(`${this.apiUrl}/nfse/${nfse}`);
  }

  buscarPorTermo(termo: string): Observable<PageResponse<Credito>> {
    const params = new HttpParams()
      .set('termo', termo)
      .set('page', '0')
      .set('size', '100');
    return this.http.get<PageResponse<Credito>>(`${this.apiUrl}/buscar`, { params });
  }
}
