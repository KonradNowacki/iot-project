package com.konrad.iotmonitoring2.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String COLLECT_TOPIC = "iot-collect-topic";
    public static final String ALERT_TOPIC = "iot-alert-topic";
    public static final String STATISTICS_TOPIC = "iot-statistics-topic";

    @Bean
    public NewTopic collectTopic() {
        return TopicBuilder.name(COLLECT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic alertTopic() {
        return TopicBuilder.name(ALERT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic statisticsTopic() {
        return TopicBuilder.name(STATISTICS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}


