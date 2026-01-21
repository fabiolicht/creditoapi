package fabiolicht.credito.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "creditos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String numeroCreditoConstituido;

    @Column(nullable = false, length = 50)
    private String numeroNFSe;

    @Column(nullable = false)
    private LocalDate dataConstituicao;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorISSQN;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TipoCredito tipoCredito;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private StatusCredito status;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    @Column
    private LocalDateTime dataAtualizacao;

    @Column(length = 100)
    private String responsavel;

    @Column(length = 20)
    private String cnpjEmpresa;

    @PrePersist
    protected void onCreate() {
        dataRegistro = LocalDateTime.now();
        status = StatusCredito.ATIVO;
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
