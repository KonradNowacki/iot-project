package com.konrad.iotmonitoring.consumer;

import com.konrad.iotmonitoring.IotDeviceData;
import com.konrad.iotmonitoring.config.KafkaTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.konrad.iotmonitoring.config.KafkaTopicConfig.ALERT_TOPIC;
import static com.konrad.iotmonitoring.config.KafkaTopicConfig.STATISTICS_TOPIC;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = STATISTICS_TOPIC, groupId = "iot-group")
    public void listenStatistics(IotDeviceData event) {
        log.info("✅ Received event: {}", event);
    }

    @KafkaListener(topics = ALERT_TOPIC, groupId = "iot-group")
    public void listenAlerts(IotDeviceData event) {
        log.warn("❌ ALERT event: {}", event);
    }
}
