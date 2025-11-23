package com.konrad.iotmonitoring;

import java.time.Instant;

public record IotDeviceData(
        String deviceId,
        Instant timestamp,
        double temperature,
        double pressure
) {
}
