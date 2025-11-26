package com.konrad.iotmonitoring2.topology;

import com.konrad.iotmonitoring2.models.IotDeviceData;

public class WindowedEvent {
    public IotDeviceData lastEvent;
    public long count;

    public WindowedEvent() {  // no-arg constructor for aggregation
        this.lastEvent = null;
        this.count = 0;
    }

    public WindowedEvent(IotDeviceData lastEvent, long count) {
        this.lastEvent = lastEvent;
        this.count = count;
    }

    @Override
    public String toString() {
        return "WindowedEvent{" +
                "lastEvent=" + lastEvent +
                ", count=" + count +
                '}';
    }
}
