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



        JsonSerde<IotDeviceData> iotSerde = new JsonSerde<>(IotDeviceData.class);

        // 1️⃣ Consume the source stream
        KStream<String, IotDeviceData> source =
                builder.stream(COLLECT_TOPIC, Consumed.with(Serdes.String(), iotSerde));

        // 2️⃣ Group by deviceId
        KGroupedStream<String, IotDeviceData> grouped =
                source.groupBy((key, value) -> value.deviceId(),
                        Grouped.with(Serdes.String(), iotSerde));

        // 3️⃣ Apply 10-second tumbling window
        TimeWindowedKStream<String, IotDeviceData> windowed =
                grouped.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10)));

        KTable<Windowed<String>, WindowedEvent> aggregated = windowed.aggregate(
                // Initializer: create a new WindowedEvent
                WindowedEvent::new,

                // Aggregator: update lastEvent + count
                (key, newValue, agg) -> {
                    agg.lastEvent = newValue;
                    agg.count += 1;
                    return agg;
                },
                Materialized.with(Serdes.String(), new JsonSerde<>(WindowedEvent.class))
        );

        // 5️⃣ Convert to stream
        KStream<Windowed<String>, WindowedEvent> aggregatedStream = aggregated.toStream();

        // 6️⃣ Branch based on count
        KStream<Windowed<String>, WindowedEvent>[] branches = aggregatedStream.branch(
                (windowedKey, windowedEvent) -> windowedEvent.count > 1, // ALERT
                (windowedKey, windowedEvent) -> true                     // STATISTICS
        );

        // 7️⃣ Send last event to ALERT_TOPIC
        branches[0]
                .map((windowedKey, windowedEvent) -> KeyValue.pair(windowedKey.key(), windowedEvent.lastEvent))
                .to(ALERT_TOPIC, Produced.with(Serdes.String(), iotSerde));

        // 8️⃣ Send last event to STATISTICS_TOPIC
        branches[1]
                .map((windowedKey, windowedEvent) -> KeyValue.pair(windowedKey.key(), windowedEvent.lastEvent))
                .to(STATISTICS_TOPIC, Produced.with(Serdes.String(), iotSerde));

        //Ļ Optional: print for debugging
        aggregatedStream.foreach((windowedKey, event) -> {
            System.out.println("Device: " + windowedKey.key() +
                    ", Window: [" + windowedKey.window().start() + " - " + windowedKey.window().end() + "]" +
                    ", LastEvent: " + event.lastEvent +
                    ", Count: " + event.count);
        });

        return source;


    }

}
