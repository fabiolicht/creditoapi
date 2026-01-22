package fabiolicht.credito.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreditoKafkaConsumer {

    @KafkaListener(topics = "creditos-events", groupId = "creditos-group")
    public void consumeCreditosEvent(String message) {
        log.info("Recebido evento de créditos: {}", message);
        processarEvento(message);
    }

    @KafkaListener(topics = "creditos-notification", groupId = "creditos-notification-group")
    public void consumeNotification(String message) {
        log.info("Recebida notificação de créditos: {}", message);
        processarNotificacao(message);
    }

    private void processarEvento(String message) {
        try {
            String[] partes = message.split(":");
            if (partes.length >= 2) {
                String tipoEvento = partes[0];
                String dados = String.join(":", java.util.Arrays.copyOfRange(partes, 1, partes.length));

                switch (tipoEvento) {
                    case "CREDITO_CRIADO":
                        log.info("Processando criação de crédito: {}", dados);
                        break;
                    case "CREDITO_ATUALIZADO":
                        log.info("Processando atualização de crédito: {}", dados);
                        break;
                    case "CREDITO_DELETADO":
                        log.info("Processando exclusão de crédito: {}", dados);
                        break;
                    case "CREDITO_STATUS_ALTERADO":
                        log.info("Processando alteração de status: {}", dados);
                        break;
                    default:
                        log.warn("Tipo de evento desconhecido: {}", tipoEvento);
                }
            }
        } catch (Exception e) {
            log.error("Erro ao processar evento: {}", message, e);
        }
    }

    private void processarNotificacao(String message) {
        log.info("Notificação processada: {}", message);
    }
}
