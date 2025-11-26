package com.konrad.iotmonitoring2;

import com.konrad.iotmonitoring2.models.IotDeviceData;
import com.konrad.iotmonitoring2.producer.KafkaProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class IotMonitoring2Application {

    public static final int NUM_OF_GOOD_DEVICES = 3;
    public static final int GOOD_DEVICE_EMITTING_FREQUENCY = 15;


    public static final int NUM_OF_BAD_DEVICES = 1;
    public static final int BAD_DEVICE_EMITTING_FREQUENCY = 5;


    public static void main(String[] args) {

        var ctx = SpringApplication.run(IotMonitoring2Application.class, args);
        var producer = ctx.getBean(KafkaProducer.class);


        try (var executor = Executors.newFixedThreadPool(NUM_OF_GOOD_DEVICES + NUM_OF_GOOD_DEVICES)) {

            for (int i = 0; i < NUM_OF_GOOD_DEVICES; i++) {

                final int deviceNum = i;

                executor.execute(() -> {
                    while (true) {
                        var record = new IotDeviceData(
                                "good-device-" + deviceNum + Thread.currentThread().getName(),
                                System.currentTimeMillis(),
                                20 + Math.random() * 5,
                                1013 + Math.random() * 10
                        );

                        producer.send(record);

                        try {
                            Thread.sleep(Duration.ofSeconds(GOOD_DEVICE_EMITTING_FREQUENCY).toMillis());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                });
            }

            for (int i = 0; i < NUM_OF_BAD_DEVICES; i++) {

                final int deviceNum = i;

                executor.execute(() -> {
                    while (true) {
                        var record = new IotDeviceData(
                                "bad-device-" + deviceNum + Thread.currentThread().getName(),
                                System.currentTimeMillis(),
                                20 + Math.random() * 5,
                                1013 + Math.random() * 10
                        );

                        producer.send(record);

                        try {
                            Thread.sleep(Duration.ofSeconds(BAD_DEVICE_EMITTING_FREQUENCY).toMillis());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                });
            }


        }



    }
}