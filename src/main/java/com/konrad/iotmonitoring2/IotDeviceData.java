package com.konrad.iotmonitoring2;

import java.time.Instant;
import java.time.LocalDateTime;

public record IotDeviceData(
        String deviceId,
        long timestamp,
        double temperature,
        double pressure
) {
}
