package fabiolicht.credito.repository;

import fabiolicht.credito.model.Credito;
import fabiolicht.credito.model.StatusCredito;
import fabiolicht.credito.model.TipoCredito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditoRepository extends JpaRepository<Credito, Long> {

    Optional<Credito> findByNumeroCreditoConstituido(String numeroCreditoConstituido);

    Optional<Credito> findByNumeroNFSe(String numeroNFSe);

    List<Credito> findByStatus(StatusCredito status);

    List<Credito> findByTipoCredito(TipoCredito tipoCredito);

    List<Credito> findByCnpjEmpresa(String cnpjEmpresa);

    Page<Credito> findByStatus(StatusCredito status, Pageable pageable);

    Page<Credito> findByTipoCredito(TipoCredito tipoCredito, Pageable pageable);

    @Query("SELECT c FROM Credito c WHERE c.dataConstituicao BETWEEN :dataInicio AND :dataFim")
    List<Credito> findByDataConstituicaoEntre(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    @Query("SELECT c FROM Credito c WHERE c.cnpjEmpresa = :cnpj AND c.status = :status")
    Page<Credito> findByCnpjEmpresaAndStatus(
            @Param("cnpj") String cnpj,
            @Param("status") StatusCredito status,
            Pageable pageable
    );

    @Query("SELECT c FROM Credito c WHERE LOWER(c.numeroCreditoConstituido) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(c.numeroNFSe) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Credito> buscarPorTermo(@Param("termo") String termo, Pageable pageable);
}
