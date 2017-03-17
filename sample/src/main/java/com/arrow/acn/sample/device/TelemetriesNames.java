package com.arrow.acn.sample.device;

/**
 * Created by osminin on 4/21/2016.
 */
public interface TelemetriesNames {
    String STEPS = "i|steps";
    String DISTANCE = "i|distance";

    String ACCELEROMETER_X = "f|accelerometerX";
    String ACCELEROMETER_Y = "f|accelerometerY";
    String ACCELEROMETER_Z = "f|accelerometerZ";

    String ORIENTATION_X = "f|orientationX";
    String ORIENTATION_Y = "f|orientationY";
    String ORIENTATION_Z = "f|orientationZ";

    String GYROSCOPE_X = "f|gyroscopeX";
    String GYROSCOPE_Y = "f|gyroscopeY";
    String GYROSCOPE_Z = "f|gyroscopeZ";

    String HEART_RATE = "i|heartRate";
    String UV = "s|uvLevel";
    String SKIN_TEMP = "i|skinTemperature";

    String BAROMETER = "f|barometer";
    String LIGHT = "f|light";
    String TEMPERATURE = "f|ambientTemperature";
    String IR_TEMPERATURE = "f|surfaceTemperature";
    String HUMIDITY = "f|humidity";

    String MAGNOMETER_X = "f|magnometerX";
    String MAGNOMETER_Y = "f|magnometerY";
    String MAGNOMETER_Z = "f|magnometerZ";

    String GYROMETER_X = "f|gyrometerX";
    String GYROMETER_Y = "f|gyrometerY";
    String GYROMETER_Z = "f|gyrometerZ";

    String TIMESTAMP = "_|timestamp";
    String DEVICE_HID = "_|deviceHid";

    String MAGNET = "b|magnet";
    String STATUS = "s|status";
    String AIRFLOW = "f|airflow";

    String MIC_LEVEL = "f|micLevel";
    String SWITCH = "i|switch";
}
