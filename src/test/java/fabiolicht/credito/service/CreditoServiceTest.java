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
}
