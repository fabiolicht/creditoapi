import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CreditoService } from '../../services/credito.service';
import { Credito } from '../../models/credito.model';

@Component({
  selector: 'app-busca-credito',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './busca-credito.component.html',
  styleUrls: ['./busca-credito.component.css']
})
export class BuscaCreditoComponent {
  tipoBusca: 'credito' | 'nfse' = 'credito';
  termoBusca: string = '';
  creditos: Credito[] = [];
  loading: boolean = false;
  erro: string = '';
  nenhumResultado: boolean = false;

  constructor(private creditoService: CreditoService) { }

  buscar(): void {
    if (!this.termoBusca || this.termoBusca.trim() === '') {
      this.erro = 'Por favor, informe um termo para busca.';
      return;
    }

    this.loading = true;
    this.erro = '';
    this.nenhumResultado = false;
    this.creditos = [];

    const termo = this.termoBusca.trim();

    if (this.tipoBusca === 'credito') {
      this.creditoService.buscarPorNumeroCredito(termo).subscribe({
        next: (credito) => {
          this.creditos = [credito];
          this.loading = false;
          this.nenhumResultado = false;
        },
        error: (err) => {
          this.loading = false;
          if (err.status === 404) {
            this.nenhumResultado = true;
            this.erro = 'Nenhum crédito encontrado com o número informado.';
          } else {
            this.erro = 'Erro ao buscar crédito. Tente novamente.';
          }
        }
      });
    } else {
      this.creditoService.buscarPorNFSe(termo).subscribe({
        next: (credito) => {
          this.creditos = [credito];
          this.loading = false;
          this.nenhumResultado = false;
        },
        error: (err) => {
          this.loading = false;
          if (err.status === 404) {
            this.nenhumResultado = true;
            this.erro = 'Nenhum crédito encontrado com o número da NFS-e informado.';
          } else {
            this.erro = 'Erro ao buscar crédito. Tente novamente.';
          }
        }
      });
    }
  }

  limpar(): void {
    this.termoBusca = '';
    this.creditos = [];
    this.erro = '';
    this.nenhumResultado = false;
  }

  formatarData(data: string | undefined): string {
    if (!data) return '-';
    return new Date(data).toLocaleDateString('pt-BR');
  }

  formatarMoeda(valor: number | undefined): string {
    if (valor === undefined || valor === null) return '-';
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }
}
