package fabiolicht.credito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fabiolicht.credito.dto.CreditoDTO;
import fabiolicht.credito.model.StatusCredito;
import fabiolicht.credito.model.TipoCredito;
import fabiolicht.credito.service.CreditoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreditoControllerTest {

    @Mock
    private CreditoService creditoService;

    @InjectMocks
    private CreditoController creditoController;

    private MockMvc mockMvc;
    private CreditoDTO creditoDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configura ObjectMapper diretamente com suporte a Java 8 Time (sem usar classes deprecadas)
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configura MockMvc - o Spring configurará os message converters padrão automaticamente
        // que incluem suporte a Jackson
        mockMvc = MockMvcBuilders.standaloneSetup(creditoController)
                .build();

        creditoDTO = CreditoDTO.builder()
                .id(1L)
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
    }

    @Test
    public void testBuscarTodos() throws Exception {
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CreditoDTO> page = new PageImpl<>(creditos, pageRequest, creditos.size());

        when(creditoService.buscarTodos(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/creditos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].numeroCreditoConstituido").value("CR001"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        when(creditoService.buscarPorId(1L)).thenReturn(creditoDTO);

        mockMvc.perform(get("/api/v1/creditos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numeroCreditoConstituido").value("CR001"));
    }

    @Test
    public void testBuscarPorNumero() throws Exception {
        when(creditoService.buscarPorNumeroCreditoConstituido("CR001"))
                .thenReturn(creditoDTO);

        mockMvc.perform(get("/api/v1/creditos/numero/CR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCreditoConstituido").value("CR001"));
    }

    @Test
    public void testCriarCredito() throws Exception {
        when(creditoService.criar(any())).thenReturn(creditoDTO);

        mockMvc.perform(post("/api/v1/creditos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testAlterarStatus() throws Exception {
        CreditoDTO creditoAtualizado = creditoDTO;
        creditoAtualizado.setStatus(StatusCredito.INATIVO);

        when(creditoService.alterarStatus(1L, StatusCredito.INATIVO))
                .thenReturn(creditoAtualizado);

        mockMvc.perform(patch("/api/v1/creditos/1/status")
                        .param("novoStatus", "INATIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INATIVO"));
    }

    @Test
    public void testDeletarCredito() throws Exception {
        mockMvc.perform(delete("/api/v1/creditos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testBuscarPorNFSe() throws Exception {
        when(creditoService.buscarPorNFSe("NFS001")).thenReturn(creditoDTO);

        mockMvc.perform(get("/api/v1/creditos/nfse/NFS001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroNFSe").value("NFS001"));
    }

    @Test
    public void testBuscarPorStatus() throws Exception {
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CreditoDTO> page = new PageImpl<>(creditos, pageRequest, creditos.size());

        when(creditoService.buscarPorStatus(StatusCredito.ATIVO, any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/creditos/status/ATIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("ATIVO"));
    }

    @Test
    public void testBuscarPorTipo() throws Exception {
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CreditoDTO> page = new PageImpl<>(creditos, pageRequest, creditos.size());

        when(creditoService.buscarPorTipo(TipoCredito.PRINCIPAL, any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/creditos/tipo/PRINCIPAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tipoCredito").value("PRINCIPAL"));
    }

    @Test
    public void testBuscarPorCNPJ() throws Exception {
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        when(creditoService.buscarPorCNPJ("12345678000100")).thenReturn(creditos);

        mockMvc.perform(get("/api/v1/creditos/cnpj/12345678000100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cnpjEmpresa").value("12345678000100"));
    }

    @Test
    public void testBuscarPorPeriodo() throws Exception {
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        when(creditoService.buscarPorPeriodo(any(), any())).thenReturn(creditos);

        mockMvc.perform(get("/api/v1/creditos/periodo")
                        .param("dataInicio", "2024-01-01")
                        .param("dataFim", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testBuscarPorCNPJEStatus() throws Exception {
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CreditoDTO> page = new PageImpl<>(creditos, pageRequest, creditos.size());

        when(creditoService.buscarPorCNPJEStatus("12345678000100", StatusCredito.ATIVO, any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/creditos/cnpj/12345678000100/status/ATIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cnpjEmpresa").value("12345678000100"));
    }

    @Test
    public void testBuscarPorTermo() throws Exception {
        List<CreditoDTO> creditos = Arrays.asList(creditoDTO);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CreditoDTO> page = new PageImpl<>(creditos, pageRequest, creditos.size());

        when(creditoService.buscarPorTermo("CR001", any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/creditos/buscar")
                        .param("termo", "CR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].numeroCreditoConstituido").value("CR001"));
    }

    @Test
    public void testAtualizarCredito() throws Exception {
        when(creditoService.atualizar(1L, any())).thenReturn(creditoDTO);

        mockMvc.perform(put("/api/v1/creditos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() throws Exception {
        when(creditoService.buscarPorId(999L))
                .thenThrow(new RuntimeException("Crédito não encontrado com ID: 999"));

        mockMvc.perform(get("/api/v1/creditos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testBuscarPorNumeroNaoEncontrado() throws Exception {
        when(creditoService.buscarPorNumeroCreditoConstituido("INVALIDO"))
                .thenThrow(new RuntimeException("Crédito não encontrado com número: INVALIDO"));

        mockMvc.perform(get("/api/v1/creditos/numero/INVALIDO"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
