package com.konrad.iotmonitoring2.models;

public record IotDeviceData(
        String deviceId,
        long timestamp,
        double temperature,
        double pressure
) {
}
