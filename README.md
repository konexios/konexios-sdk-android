# KonexiosSDK #
Konexios SDK for Android

### Add AcnSDK to Project ###

Add repository to the main build file
```xml
maven { url "https://dl.bintray.com/arrow-acs/acn-sdk/" }
```

grab via Maven:
```xml
<dependency> 
    <groupId>com.arrow.acn.api</groupId>
    <artifactId>acnsdk</artifactId>
    <version>1.0.0</version>
    <type>pom</type>
</dependency>
```
or Gradle:
```groovy
compile 'com.arrow.acn.api:acnsdk:0.9.3'
```
### Create new instance of sdk ###

```java
AcnApiService acnApiService = new AcnApi.Builder()
        .setRestEndpoint(ENDPOINT_URL, API_KEY, API_SECRET)
        .setMqttEndpoint(MQTT_HOST, MQTT_PREFIX)
        .setDebug(true)
        .build();
```

### Create new account or login if the account already exists ###

```java
AccountRequest model = new AccountRequest();
model.setName("Some Name");
model.setEmail("some.email@mail.com");
model.setPassword("password");
acnApiService .registerAccount(model, new RegisterAccountListener() {
    @Override
    public void onAccountRegistered(AccountResponse accountResponse) {
        //do smth here
    }
    @Override
    public void onAccountRegisterFailed(ApiError e) {
        // handle error here
    }
});
```

### Register gateway ###

```java
GatewayModel gatewayModel = new GatewayModel();
gatewayModel.setName("Name");
gatewayModel.setOsName("osName");
gatewayModel.setSoftwareName("swName");
gatewayModel.setUid("uid");
gatewayModel.setType(GatewayType.Mobile);
//userHid from AccountResponse
gatewayModel.setUserHid("userHid");
//applicationHid from AccountResponse
gatewayModel.setApplicationHid("applicationHid");
gatewayModel.setSoftwareVersion("swVersion");

acnApiService.registerGateway(gatewayModel, new GatewayRegisterListener() {
    @Override
    public void onGatewayRegistered(@NonNull GatewayResponse response) {
        // do some stuff here
    }
    ...
```

### Get gateway config ###

```java
// take gatewayHid from GatewayResponse after registering gateway
acnApiService.getGatewayConfig(gatewayHid, new GetGatewayConfigListener() {
    @Override
    public void onGatewayConfigReceived(ConfigResponse response) {
        // do some stuff here
    }
    ...
```

### Establish a persistent connection to cloud ###

```java
acnApiService.connect(new ConnectionListener() {
    @Override
    public void onConnectionSuccess() {
        //now you are able to send some telemetry to the cloud or to get some commands from cloud
    }
    ...
```

### Register a new device ###

```java
DeviceRegistrationModel payload = new DeviceRegistrationModel();
payload.setUid("unique device user id");
payload.setName("some name");
payload.setGatewayHid(gatewayHid);
payload.setUserHid("user hid from account response");
payload.setType("device type name");
payload.setEnabled(true);
acnApiService.registerDevice(payload, new RegisterDeviceListener() {
    @Override
    public void onDeviceRegistered(@NonNull DeviceRegistrationResponse response) {
        //new device is registered 
    }
    ...
```

### Send a telemetry ###

```java
String telemetryJson = 
"{
	"_|timestamp": 1502113061590,
	"_|deviceHid": "211f793a3bfd00244b28f1c519c19bf702e356e6",
	"f|light": "63.663162",
	"f|magnometerZ": "-29.759216",
	"f|accelerometerX": "0.61331177",
	"f|gyroscopeX": "0.33015442",
	"f|gyroscopeZ": "0.5884552",
	"f|accelerometerZ": "4.2137146",
	"f|accelerometerY": "8.158783",
	"f|gyroscopeY": "0.37690735",
	"f|magnometerY": "-54.359436",
	"f|magnometerX": "-31.019592"
}";
TelemetryModel telemetryModel = new TelemetryModel();
telemetryModel.setTelemetry(telemetryJson);
telemetryModel.setDeviceType("device type");
mSender.sendTelemetry(telemetryModel);
...
```

License
=======

    Copyright 2017 Arrow Electronics, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
