package fabiolicht.credito.dto;

import fabiolicht.credito.model.StatusCredito;
import fabiolicht.credito.model.TipoCredito;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditoDTO {

    private Long id;
    private String numeroCreditoConstituido;
    private String numeroNFSe;
    private LocalDate dataConstituicao;
    private BigDecimal valorISSQN;
    private TipoCredito tipoCredito;
    private String descricao;
    private StatusCredito status;
    private LocalDateTime dataRegistro;
    private LocalDateTime dataAtualizacao;
    private String responsavel;
    private String cnpjEmpresa;
}
