package com.kronossdk.api.mqtt;

/**
 * Created by osminin on 4/29/2016.
 */
public interface KronosEventNames {
    public interface ServerToGateway {
        public final static String DEVICE_START = "ServerToGateway_DeviceStart";
        public final static String DEVICE_STOP = "ServerToGateway_DeviceStop";

        public final static String DEVICE_PROPERTY_CHANGE = "ServerToGateway_DevicePropertyChange";
        public final static String DEVICE_COMMAND = "ServerToGateway_DeviceCommand";

        public final static String SENSOR_PROPERTY_CHANGE = "ServerToGateway_SensorPropertyChange";
        public final static String SENSOR_TELEMETRY_CHANGE = "ServerToGateway_SensorTelemetryChange";
        public final static String SENSOR_COMMAND = "ServerToGateway_SensorCommand";
    }
}
