import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BuscaCreditoComponent } from './components/busca-credito/busca-credito.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, BuscaCreditoComponent],
  template: `
    <div class="app-container">
      <header class="app-header">
        <h1>Consulta de Créditos</h1>
      </header>
      <main class="app-main">
        <app-busca-credito></app-busca-credito>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }
    
    .app-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    
    .app-header h1 {
      font-size: 24px;
      font-weight: 600;
    }
    
    .app-main {
      flex: 1;
      padding: 20px;
    }
    
    @media (max-width: 768px) {
      .app-header h1 {
        font-size: 20px;
      }
      
      .app-main {
        padding: 10px;
      }
    }
  `]
})
export class AppComponent {
  title = 'Consulta de Créditos';
}
