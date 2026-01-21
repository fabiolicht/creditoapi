package fabiolicht.credito.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic creditosEventsTopic() {
        return TopicBuilder.name("creditos-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic creditosNotificationTopic() {
        return TopicBuilder.name("creditos-notification")
                .partitions(2)
                .replicas(1)
                .build();
    }
}
