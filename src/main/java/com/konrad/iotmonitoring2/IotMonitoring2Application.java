package com.konrad.iotmonitoring2;

import com.konrad.iotmonitoring2.producer.KafkaProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;

@SpringBootApplication
public class IotMonitoring2Application {

    public static final int NUM_OF_DEVICES = 3;

    public static void main(String[] args) {

        var ctx = SpringApplication.run(IotMonitoring2Application.class, args);
        var producer = ctx.getBean(KafkaProducer.class);

//        var data = new IotDeviceData(
//                "a",
//                System.currentTimeMillis(),
//                20,
//                30
//        );
//
//        producer.send(data);

        var executor = Executors.newFixedThreadPool(NUM_OF_DEVICES);
//
        for (int i = 0; i < NUM_OF_DEVICES; i++) {

            final int deviceNum = i;

            executor.execute(() -> {
                while (true) {
                    var record = new IotDeviceData(
                            "device-" + deviceNum,
                            System.currentTimeMillis(),
                            20 + Math.random() * 5,
                            1013 + Math.random() * 10
                    );

                    producer.send(record);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            });
        }
    }
}