package com.konrad.iotmonitoring2.consumer;

import com.konrad.iotmonitoring2.models.IotDeviceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.konrad.iotmonitoring2.config.KafkaTopicConfig.*;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = COLLECT_TOPIC, groupId = "qwe")
    public void listenCollect(IotDeviceData event) {
        log.info("✅ Collect topic event: {}", event.deviceId());
    }

    @KafkaListener(topics = STATISTICS_TOPIC, groupId = "statistics-group")
    public void listenStatistics(IotDeviceData event) {
        log.info("✅ Received event: {}", event);
    }

    @KafkaListener(topics = ALERT_TOPIC, groupId = "alert-group")
    public void listenAlerts(IotDeviceData event) {
        log.warn("❌ ALERT event: {}", event);
    }
}
