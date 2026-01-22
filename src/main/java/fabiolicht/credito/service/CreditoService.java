package fabiolicht.credito.service;

import fabiolicht.credito.dto.CreditoDTO;
import fabiolicht.credito.model.Credito;
import fabiolicht.credito.model.StatusCredito;
import fabiolicht.credito.model.TipoCredito;
import fabiolicht.credito.repository.CreditoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CreditoService {

    private static final String KAFKA_TOPIC = "creditos-events";
    @Autowired
    private CreditoRepository creditoRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Busca todos os créditos com paginação
     */
    public Page<CreditoDTO> buscarTodos(Pageable pageable) {
        log.info("Buscando todos os créditos - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        return creditoRepository.findAll(pageable).map(this::convertToDTO);
    }

    /**
     * Busca crédito por ID
     */
    public CreditoDTO buscarPorId(Long id) {
        log.info("Buscando crédito com ID: {}", id);
        return creditoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Crédito não encontrado com ID: " + id));
    }

    /**
     * Busca crédito por número de crédito constituído
     */
    public CreditoDTO buscarPorNumeroCreditoConstituido(String numero) {
        log.info("Buscando crédito com número: {}", numero);
        return creditoRepository.findByNumeroCreditoConstituido(numero)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Crédito não encontrado com número: " + numero));
    }

    /**
     * Busca crédito por número de NFS-e
     */
    public CreditoDTO buscarPorNFSe(String nfse) {
        log.info("Buscando crédito com NFS-e: {}", nfse);
        return creditoRepository.findByNumeroNFSe(nfse)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Crédito não encontrado com NFS-e: " + nfse));
    }

    /**
     * Busca créditos por status
     */
    public Page<CreditoDTO> buscarPorStatus(StatusCredito status, Pageable pageable) {
        log.info("Buscando créditos com status: {}", status);
        return creditoRepository.findByStatus(status, pageable).map(this::convertToDTO);
    }

    /**
     * Busca créditos por tipo
     */
    public Page<CreditoDTO> buscarPorTipo(TipoCredito tipo, Pageable pageable) {
        log.info("Buscando créditos com tipo: {}", tipo);
        return creditoRepository.findByTipoCredito(tipo, pageable).map(this::convertToDTO);
    }

    /**
     * Busca créditos por CNPJ
     */
    public List<CreditoDTO> buscarPorCNPJ(String cnpj) {
        log.info("Buscando créditos com CNPJ: {}", cnpj);
        return creditoRepository.findByCnpjEmpresa(cnpj)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca créditos por intervalo de data de constituição
     */
    public List<CreditoDTO> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        log.info("Buscando créditos no período de {} a {}", dataInicio, dataFim);
        return creditoRepository.findByDataConstituicaoEntre(dataInicio, dataFim)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca créditos por CNPJ e status com paginação
     */
    public Page<CreditoDTO> buscarPorCNPJEStatus(String cnpj, StatusCredito status, Pageable pageable) {
        log.info("Buscando créditos com CNPJ: {} e status: {}", cnpj, status);
        return creditoRepository.findByCnpjEmpresaAndStatus(cnpj, status, pageable).map(this::convertToDTO);
    }

    /**
     * Busca créditos por termo (número ou NFS-e)
     */
    public Page<CreditoDTO> buscarPorTermo(String termo, Pageable pageable) {
        log.info("Buscando créditos com termo: {}", termo);
        return creditoRepository.buscarPorTermo(termo, pageable).map(this::convertToDTO);
    }

    /**
     * Cria um novo crédito
     */
    public CreditoDTO criar(CreditoDTO creditoDTO) {
        log.info("Criando novo crédito com número: {}", creditoDTO.getNumeroCreditoConstituido());

        if (creditoRepository.findByNumeroCreditoConstituido(creditoDTO.getNumeroCreditoConstituido()).isPresent()) {
            throw new RuntimeException("Crédito já existe com este número");
        }

        Credito credito = convertToEntity(creditoDTO);
        Credito saved = creditoRepository.save(credito);

        // Publica evento no Kafka
        kafkaTemplate.send(KAFKA_TOPIC, "CREDITO_CRIADO:" + saved.getId() + ":" + saved.getNumeroCreditoConstituido());

        log.info("Crédito criado com sucesso - ID: {}", saved.getId());
        return convertToDTO(saved);
    }

    /**
     * Atualiza um crédito existente
     */
    public CreditoDTO atualizar(Long id, CreditoDTO creditoDTO) {
        log.info("Atualizando crédito com ID: {}", id);

        Credito credito = creditoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crédito não encontrado com ID: " + id));

        credito.setNumeroNFSe(creditoDTO.getNumeroNFSe());
        credito.setValorISSQN(creditoDTO.getValorISSQN());
        credito.setTipoCredito(credito.getTipoCredito());
        credito.setDescricao(creditoDTO.getDescricao());
        credito.setStatus(creditoDTO.getStatus());
        credito.setResponsavel(creditoDTO.getResponsavel());
        credito.setCnpjEmpresa(creditoDTO.getCnpjEmpresa());

        Credito updated = creditoRepository.save(credito);

        // Publica evento no Kafka
        kafkaTemplate.send(KAFKA_TOPIC, "CREDITO_ATUALIZADO:" + updated.getId() + ":" + updated.getNumeroCreditoConstituido());

        log.info("Crédito atualizado com sucesso - ID: {}", id);
        return convertToDTO(updated);
    }

    /**
     * Deleta um crédito
     */
    public void deletar(Long id) {
        log.info("Deletando crédito com ID: {}", id);

        Credito credito = creditoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crédito não encontrado com ID: " + id));

        creditoRepository.deleteById(id);

        // Publica evento no Kafka
        kafkaTemplate.send(KAFKA_TOPIC, "CREDITO_DELETADO:" + id + ":" + credito.getNumeroCreditoConstituido());

        log.info("Crédito deletado com sucesso - ID: {}", id);
    }

    /**
     * Altera o status de um crédito
     */
    public CreditoDTO alterarStatus(Long id, StatusCredito novoStatus) {
        log.info("Alterando status do crédito ID: {} para: {}", id, novoStatus);

        Credito credito = creditoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crédito não encontrado com ID: " + id));

        credito.setStatus(novoStatus);
        Credito updated = creditoRepository.save(credito);

        // Publica evento no Kafka
        kafkaTemplate.send(KAFKA_TOPIC, "CREDITO_STATUS_ALTERADO:" + id + ":" + novoStatus);

        log.info("Status alterado com sucesso - ID: {}", id);
        return convertToDTO(updated);
    }

    /**
     * Converte Credito entity para CreditoDTO
     */
    private CreditoDTO convertToDTO(Credito credito) {
        return CreditoDTO.builder()
                .id(credito.getId())
                .numeroCreditoConstituido(credito.getNumeroCreditoConstituido())
                .numeroNFSe(credito.getNumeroNFSe())
                .dataConstituicao(credito.getDataConstituicao())
                .valorISSQN(credito.getValorISSQN())
                .tipoCredito(credito.getTipoCredito())
                .descricao(credito.getDescricao())
                .status(credito.getStatus())
                .dataRegistro(credito.getDataRegistro())
                .dataAtualizacao(credito.getDataAtualizacao())
                .responsavel(credito.getResponsavel())
                .cnpjEmpresa(credito.getCnpjEmpresa())
                .build();
    }

    /**
     * Converte CreditoDTO para Credito entity
     */
    private Credito convertToEntity(CreditoDTO dto) {
        Credito credito = new Credito();
        credito.setNumeroCreditoConstituido(dto.getNumeroCreditoConstituido());
        credito.setNumeroNFSe(dto.getNumeroNFSe());
        credito.setDataConstituicao(dto.getDataConstituicao());
        credito.setValorISSQN(dto.getValorISSQN());
        credito.setTipoCredito(dto.getTipoCredito());
        credito.setDescricao(dto.getDescricao());
        credito.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusCredito.ATIVO);
        credito.setResponsavel(dto.getResponsavel());
        credito.setCnpjEmpresa(dto.getCnpjEmpresa());
        return credito;
    }
}
