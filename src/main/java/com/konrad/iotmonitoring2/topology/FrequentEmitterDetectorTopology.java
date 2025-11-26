package com.konrad.iotmonitoring2.topology;

import com.konrad.iotmonitoring2.models.IotDeviceData;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

import static com.konrad.iotmonitoring2.config.KafkaTopicConfig.*;


@Configuration
@EnableKafkaStreams
public class FrequentEmitterDetectorTopology {


    @Bean
    public KStream<String, IotDeviceData> iotStream(StreamsBuilder builder) {


        // JSON serde for your IotDeviceData record
        JsonSerde<IotDeviceData> iotSerde = new JsonSerde<>(IotDeviceData.class);

        // 1. Consume the source stream
        KStream<String, IotDeviceData> source =
                builder.stream(
                        COLLECT_TOPIC,
                        Consumed.with(Serdes.String(), iotSerde)
                );


        source.foreach((key, value) -> System.out.println("👉👉👉" + key + ": " + value));


        // 2. Group by deviceId
        KGroupedStream<String, IotDeviceData> grouped =
                source.groupBy((key, value) -> value.deviceId(),
                        Grouped.with(Serdes.String(), iotSerde));

        KTable<String, Long> counts = grouped.count();


        counts.toStream().foreach((deviceId, count) ->
                System.out.println("Device: " + deviceId + ", Count: " + count));


//        // 3. Apply 10-second tumbling window and count events
//        TimeWindowedKStream<String, IotDeviceData> windowed =
//                grouped.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10)));
//


        return source;

    }

}
