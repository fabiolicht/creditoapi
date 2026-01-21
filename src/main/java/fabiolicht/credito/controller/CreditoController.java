package fabiolicht.credito.controller;

import fabiolicht.credito.dto.CreditoDTO;
import fabiolicht.credito.model.StatusCredito;
import fabiolicht.credito.model.TipoCredito;
import fabiolicht.credito.service.CreditoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/creditos")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    /**
     * GET /api/v1/creditos
     * Busca todos os créditos com paginação
     */
    @GetMapping
    public ResponseEntity<Page<CreditoDTO>> buscarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        
        log.info("GET /api/v1/creditos - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(creditoService.buscarTodos(pageable));
    }

    /**
     * GET /api/v1/creditos/{id}
     * Busca crédito por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditoDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/creditos/{} - Buscando crédito por ID", id);
        return ResponseEntity.ok(creditoService.buscarPorId(id));
    }

    /**
     * GET /api/v1/creditos/numero/{numero}
     * Busca crédito por número
     */
    @GetMapping("/numero/{numero}")
    public ResponseEntity<CreditoDTO> buscarPorNumero(@PathVariable String numero) {
        log.info("GET /api/v1/creditos/numero/{} - Buscando crédito por número", numero);
        return ResponseEntity.ok(creditoService.buscarPorNumeroCreditoConstituido(numero));
    }

    /**
     * GET /api/v1/creditos/nfse/{nfse}
     * Busca crédito por NFS-e
     */
    @GetMapping("/nfse/{nfse}")
    public ResponseEntity<CreditoDTO> buscarPorNFSe(@PathVariable String nfse) {
        log.info("GET /api/v1/creditos/nfse/{} - Buscando crédito por NFS-e", nfse);
        return ResponseEntity.ok(creditoService.buscarPorNFSe(nfse));
    }

    /**
     * GET /api/v1/creditos/status/{status}
     * Busca créditos por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CreditoDTO>> buscarPorStatus(
            @PathVariable StatusCredito status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/v1/creditos/status/{} - Buscando créditos por status", status);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(creditoService.buscarPorStatus(status, pageable));
    }

    /**
     * GET /api/v1/creditos/tipo/{tipo}
     * Busca créditos por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<CreditoDTO>> buscarPorTipo(
            @PathVariable TipoCredito tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/v1/creditos/tipo/{} - Buscando créditos por tipo", tipo);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(creditoService.buscarPorTipo(tipo, pageable));
    }

    /**
     * GET /api/v1/creditos/cnpj/{cnpj}
     * Busca créditos por CNPJ
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<List<CreditoDTO>> buscarPorCNPJ(@PathVariable String cnpj) {
        log.info("GET /api/v1/creditos/cnpj/{} - Buscando créditos por CNPJ", cnpj);
        return ResponseEntity.ok(creditoService.buscarPorCNPJ(cnpj));
    }

    /**
     * GET /api/v1/creditos/periodo
     * Busca créditos por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<CreditoDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        log.info("GET /api/v1/creditos/periodo - dataInicio: {}, dataFim: {}", dataInicio, dataFim);
        return ResponseEntity.ok(creditoService.buscarPorPeriodo(dataInicio, dataFim));
    }

    /**
     * GET /api/v1/creditos/cnpj/{cnpj}/status/{status}
     * Busca créditos por CNPJ e status
     */
    @GetMapping("/cnpj/{cnpj}/status/{status}")
    public ResponseEntity<Page<CreditoDTO>> buscarPorCNPJEStatus(
            @PathVariable String cnpj,
            @PathVariable StatusCredito status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/v1/creditos/cnpj/{}/status/{} - page: {}, size: {}", cnpj, status, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(creditoService.buscarPorCNPJEStatus(cnpj, status, pageable));
    }

    /**
     * GET /api/v1/creditos/buscar
     * Busca créditos por termo (número ou NFS-e)
     */
    @GetMapping("/buscar")
    public ResponseEntity<Page<CreditoDTO>> buscarPorTermo(
            @RequestParam String termo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("GET /api/v1/creditos/buscar - termo: {}, page: {}, size: {}", termo, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(creditoService.buscarPorTermo(termo, pageable));
    }

    /**
     * POST /api/v1/creditos
     * Cria um novo crédito
     */
    @PostMapping
    public ResponseEntity<CreditoDTO> criar(@RequestBody CreditoDTO creditoDTO) {
        log.info("POST /api/v1/creditos - Criando novo crédito");
        CreditoDTO created = creditoService.criar(creditoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/v1/creditos/{id}
     * Atualiza um crédito existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<CreditoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody CreditoDTO creditoDTO) {
        
        log.info("PUT /api/v1/creditos/{} - Atualizando crédito", id);
        return ResponseEntity.ok(creditoService.atualizar(id, creditoDTO));
    }

    /**
     * PATCH /api/v1/creditos/{id}/status
     * Altera o status de um crédito
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<CreditoDTO> alterarStatus(
            @PathVariable Long id,
            @RequestParam StatusCredito novoStatus) {
        
        log.info("PATCH /api/v1/creditos/{}/status - Novo status: {}", id, novoStatus);
        return ResponseEntity.ok(creditoService.alterarStatus(id, novoStatus));
    }

    /**
     * DELETE /api/v1/creditos/{id}
     * Deleta um crédito
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /api/v1/creditos/{} - Deletando crédito", id);
        creditoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exception handler
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException ex) {
        log.error("Erro ao processar requisição: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Classe auxiliar para resposta de erro
     */
    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
