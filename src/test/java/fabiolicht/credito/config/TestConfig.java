package fabiolicht.credito.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Configuração de teste que fornece um KafkaTemplate mockado
 * para evitar problemas de conexão durante os testes de contexto
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public KafkaTemplate<String, String> kafkaTemplate() {
        return Mockito.mock(KafkaTemplate.class);
    }
}
