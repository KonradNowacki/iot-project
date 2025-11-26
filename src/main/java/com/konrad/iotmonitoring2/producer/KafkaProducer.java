package com.konrad.iotmonitoring2.producer;

import com.konrad.iotmonitoring2.models.IotDeviceData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.konrad.iotmonitoring2.config.KafkaTopicConfig.COLLECT_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, IotDeviceData> kafkaTemplate;

    public void send(IotDeviceData event) {
        kafkaTemplate.send(COLLECT_TOPIC, event.deviceId(), event);
    }
}
