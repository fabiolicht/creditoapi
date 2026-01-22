package fabiolicht.credito.service;

import fabiolicht.credito.dto.CreditoDTO;
import fabiolicht.credito.model.Credito;
import fabiolicht.credito.model.StatusCredito;
import fabiolicht.credito.model.TipoCredito;
import fabiolicht.credito.repository.CreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreditoServiceTest {

    @Mock
    private CreditoRepository creditoRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private CreditoService creditoService;

    private CreditoDTO creditoDTO;
    private Credito credito;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        creditoDTO = CreditoDTO.builder()
                .numeroCreditoConstituido("CR001")
                .numeroNFSe("NFS001")
                .dataConstituicao(LocalDate.now())
                .valorISSQN(new BigDecimal("1000.00"))
                .tipoCredito(TipoCredito.PRINCIPAL)
                .descricao("Crédito Teste")
                .status(StatusCredito.ATIVO)
                .responsavel("João Silva")
                .cnpjEmpresa("12345678000100")
                .build();

        credito = new Credito();
        credito.setId(1L);
        credito.setNumeroCreditoConstituido("CR001");
        credito.setNumeroNFSe("NFS001");
        credito.setDataConstituicao(LocalDate.now());
        credito.setValorISSQN(new BigDecimal("1000.00"));
        credito.setTipoCredito(TipoCredito.PRINCIPAL);
        credito.setDescricao("Crédito Teste");
        credito.setStatus(StatusCredito.ATIVO);
        credito.setResponsavel("João Silva");
        credito.setCnpjEmpresa("12345678000100");
    }

    @Test
    public void testBuscarPorIdSucesso() {
        when(creditoRepository.findById(1L)).thenReturn(Optional.of(credito));

        CreditoDTO resultado = creditoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("CR001", resultado.getNumeroCreditoConstituido());
        verify(creditoRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(creditoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> creditoService.buscarPorId(99L));
        verify(creditoRepository, times(1)).findById(99L);
    }

    @Test
    public void testBuscarPorNumeroCreditoConstituido() {
        when(creditoRepository.findByNumeroCreditoConstituido("CR001"))
                .thenReturn(Optional.of(credito));

        CreditoDTO resultado = creditoService.buscarPorNumeroCreditoConstituido("CR001");

        assertNotNull(resultado);
        assertEquals("CR001", resultado.getNumeroCreditoConstituido());
        verify(creditoRepository, times(1)).findByNumeroCreditoConstituido("CR001");
    }

    @Test
    public void testBuscarPorNFSe() {
        when(creditoRepository.findByNumeroNFSe("NFS001"))
                .thenReturn(Optional.of(credito));

        CreditoDTO resultado = creditoService.buscarPorNFSe("NFS001");

        assertNotNull(resultado);
        assertEquals("NFS001", resultado.getNumeroNFSe());
        verify(creditoRepository, times(1)).findByNumeroNFSe("NFS001");
    }

    @Test
    public void testCriarCredito() {
        when(creditoRepository.findByNumeroCreditoConstituido("CR001"))
                .thenReturn(Optional.empty());
        when(creditoRepository.save(any(Credito.class)))
                .thenReturn(credito);

        CreditoDTO resultado = creditoService.criar(creditoDTO);

        assertNotNull(resultado);
        assertEquals("CR001", resultado.getNumeroCreditoConstituido());
        verify(creditoRepository, times(1)).save(any(Credito.class));
        verify(kafkaTemplate, times(1)).send("creditos-events", "CREDITO_CRIADO:1:CR001");
    }

    @Test
    public void testCriarCreditoDuplicado() {
        when(creditoRepository.findByNumeroCreditoConstituido("CR001"))
                .thenReturn(Optional.of(credito));

        assertThrows(RuntimeException.class, () -> creditoService.criar(creditoDTO));
        verify(creditoRepository, never()).save(any(Credito.class));
    }

    @Test
    public void testAlterarStatus() {
        when(creditoRepository.findById(1L)).thenReturn(Optional.of(credito));
        when(creditoRepository.save(any(Credito.class))).thenReturn(credito);

        CreditoDTO resultado = creditoService.alterarStatus(1L, StatusCredito.INATIVO);

        assertNotNull(resultado);
        verify(creditoRepository, times(1)).findById(1L);
        verify(creditoRepository, times(1)).save(any(Credito.class));
    }

    @Test
    public void testDeletarCredito() {
        when(creditoRepository.findById(1L)).thenReturn(Optional.of(credito));
        doNothing().when(creditoRepository).deleteById(1L);

        creditoService.deletar(1L);

        verify(creditoRepository, times(1)).findById(1L);
        verify(creditoRepository, times(1)).deleteById(1L);
        verify(kafkaTemplate, times(1)).send("creditos-events", "CREDITO_DELETADO:1:CR001");
    }

    @Test
    public void testDeletarCreditoNaoEncontrado() {
        when(creditoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> creditoService.deletar(99L));
        verify(creditoRepository, never()).deleteById(any());
    }

    @Test
    public void testBuscarTodos() {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        org.springframework.data.domain.Page<Credito> page = new org.springframework.data.domain.PageImpl<>(
                Arrays.asList(credito), pageable, 1);

        when(creditoRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        org.springframework.data.domain.Page<CreditoDTO> resultado = creditoService.buscarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(creditoRepository, times(1)).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    public void testBuscarPorStatus() {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        org.springframework.data.domain.Page<Credito> page = new org.springframework.data.domain.PageImpl<>(
                Arrays.asList(credito), pageable, 1);

        when(creditoRepository.findByStatus(StatusCredito.ATIVO, pageable)).thenReturn(page);

        org.springframework.data.domain.Page<CreditoDTO> resultado = creditoService.buscarPorStatus(StatusCredito.ATIVO, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(creditoRepository, times(1)).findByStatus(StatusCredito.ATIVO, pageable);
    }

    @Test
    public void testBuscarPorTipo() {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        org.springframework.data.domain.Page<Credito> page = new org.springframework.data.domain.PageImpl<>(
                Arrays.asList(credito), pageable, 1);

        when(creditoRepository.findByTipoCredito(TipoCredito.PRINCIPAL, pageable)).thenReturn(page);

        org.springframework.data.domain.Page<CreditoDTO> resultado = creditoService.buscarPorTipo(TipoCredito.PRINCIPAL, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(creditoRepository, times(1)).findByTipoCredito(TipoCredito.PRINCIPAL, pageable);
    }

    @Test
    public void testBuscarPorCNPJ() {
        when(creditoRepository.findByCnpjEmpresa("12345678000100"))
                .thenReturn(Arrays.asList(credito));

        List<CreditoDTO> resultado = creditoService.buscarPorCNPJ("12345678000100");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("12345678000100", resultado.get(0).getCnpjEmpresa());
        verify(creditoRepository, times(1)).findByCnpjEmpresa("12345678000100");
    }

    @Test
    public void testBuscarPorPeriodo() {
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        LocalDate dataFim = LocalDate.of(2024, 12, 31);

        when(creditoRepository.findByDataConstituicaoEntre(dataInicio, dataFim))
                .thenReturn(Arrays.asList(credito));

        List<CreditoDTO> resultado = creditoService.buscarPorPeriodo(dataInicio, dataFim);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(creditoRepository, times(1)).findByDataConstituicaoEntre(dataInicio, dataFim);
    }

    @Test
    public void testBuscarPorCNPJEStatus() {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        org.springframework.data.domain.Page<Credito> page = new org.springframework.data.domain.PageImpl<>(
                Arrays.asList(credito), pageable, 1);

        when(creditoRepository.findByCnpjEmpresaAndStatus("12345678000100", StatusCredito.ATIVO, pageable))
                .thenReturn(page);

        org.springframework.data.domain.Page<CreditoDTO> resultado = creditoService.buscarPorCNPJEStatus(
                "12345678000100", StatusCredito.ATIVO, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(creditoRepository, times(1)).findByCnpjEmpresaAndStatus("12345678000100", StatusCredito.ATIVO, pageable);
    }

    @Test
    public void testBuscarPorTermo() {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        org.springframework.data.domain.Page<Credito> page = new org.springframework.data.domain.PageImpl<>(
                Arrays.asList(credito), pageable, 1);

        when(creditoRepository.buscarPorTermo("CR001", pageable)).thenReturn(page);

        org.springframework.data.domain.Page<CreditoDTO> resultado = creditoService.buscarPorTermo("CR001", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(creditoRepository, times(1)).buscarPorTermo("CR001", pageable);
    }

    @Test
    public void testAtualizarCredito() {
        when(creditoRepository.findById(1L)).thenReturn(Optional.of(credito));
        when(creditoRepository.save(any(Credito.class))).thenReturn(credito);

        CreditoDTO resultado = creditoService.atualizar(1L, creditoDTO);

        assertNotNull(resultado);
        verify(creditoRepository, times(1)).findById(1L);
        verify(creditoRepository, times(1)).save(any(Credito.class));
        verify(kafkaTemplate, times(1)).send("creditos-events", "CREDITO_ATUALIZADO:1:CR001");
    }

    @Test
    public void testAtualizarCreditoNaoEncontrado() {
        when(creditoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> creditoService.atualizar(99L, creditoDTO));
        verify(creditoRepository, never()).save(any(Credito.class));
    }

    @Test
    public void testBuscarPorNFSeNaoEncontrado() {
        when(creditoRepository.findByNumeroNFSe("INVALIDO"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> creditoService.buscarPorNFSe("INVALIDO"));
        verify(creditoRepository, times(1)).findByNumeroNFSe("INVALIDO");
    }

    @Test
    public void testBuscarPorNumeroNaoEncontrado() {
        when(creditoRepository.findByNumeroCreditoConstituido("INVALIDO"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> creditoService.buscarPorNumeroCreditoConstituido("INVALIDO"));
        verify(creditoRepository, times(1)).findByNumeroCreditoConstituido("INVALIDO");
    }
}
