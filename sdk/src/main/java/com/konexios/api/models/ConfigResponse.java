/*
 * Copyright (c) 2017-2019 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *     Konexios, Inc.
 */

package com.konexios.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * response for configuration request
 */
public final class ConfigResponse implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConfigResponse> CREATOR = new Parcelable.Creator<ConfigResponse>() {
        @NonNull
        @Override
        public ConfigResponse createFromParcel(@NonNull Parcel in) {
            return new ConfigResponse(in);
        }

        @NonNull
        @Override
        public ConfigResponse[] newArray(int size) {
            return new ConfigResponse[size];
        }
    };
    /**
     * String representing the platform type like IotConnect, Azure, etc
     */
    @SerializedName("cloudPlatform")
    private String cloudPlatform;
    /**
     * keys for pure mqtt platform
     */
    @SerializedName("key")
    private Key key;
    /**
     * config for AWS
     */
    @SerializedName("aws")
    private Aws aws;
    /**
     * config for IBM
     */
    @SerializedName("ibm")
    private Ibm ibm;
    /**
     * config for Azure
     */
    @SerializedName("azure")
    private Azure azure;

    public ConfigResponse() {
    }

    public ConfigResponse(@NonNull Parcel in) {
        cloudPlatform = in.readString();
        key = (Key) in.readValue(Key.class.getClassLoader());
        aws = (Aws) in.readValue(Aws.class.getClassLoader());
        ibm = (Ibm) in.readValue(Ibm.class.getClassLoader());
        azure = (Azure) in.readValue(Azure.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigResponse that = (ConfigResponse) o;
        return Objects.equals(cloudPlatform, that.cloudPlatform) &&
                Objects.equals(key, that.key) &&
                Objects.equals(aws, that.aws) &&
                Objects.equals(ibm, that.ibm) &&
                Objects.equals(azure, that.azure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cloudPlatform, key, aws, ibm, azure);
    }

    public Ibm getIbm() {
        return ibm;
    }

    public void setIbm(Ibm ibm) {
        this.ibm = ibm;
    }

    public CloudPlatform getCloudPlatform() {
        return CloudPlatform.getPlatform(cloudPlatform);
    }

    public void setCloudPlatform(String cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Aws getAws() {
        return aws;
    }

    public void setAws(Aws aws) {
        this.aws = aws;
    }

    public Azure getAzure() {
        return azure;
    }

    public void setAzure(Azure azure) {
        this.azure = azure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(cloudPlatform);
        dest.writeValue(key);
        dest.writeValue(aws);
        dest.writeValue(ibm);
        dest.writeValue(azure);
    }

    public enum CloudPlatform {
        NONE(""),
        ARROW_CONNECT("ARROWCONNECT", "IotConnect"),
        IBM("IBM"),
        AWS("AWS"),
        AZURE("AZURE");

        private String[] mString;

        CloudPlatform(String... str) {
            mString = str;
        }

        public static CloudPlatform getPlatform(String str) {
            CloudPlatform res = NONE;
            for (CloudPlatform tmp : CloudPlatform.values()) {
                for (String keys : tmp.getString())
                    if (keys.equalsIgnoreCase(str)) {
                        res = tmp;
                    }
            }
            return res;
        }

        public String[] getString() {
            return mString;
        }

        @Override
        public String toString() {
            return mString[0];
        }
    }

    public static class Key implements Parcelable {
        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Key> CREATOR = new Parcelable.Creator<Key>() {
            @NonNull
            @Override
            public Key createFromParcel(@NonNull Parcel in) {
                return new Key(in);
            }

            @NonNull
            @Override
            public Key[] newArray(int size) {
                return new Key[size];
            }
        };
        @SerializedName("apiKey")
        private String apiKey;
        @SerializedName("secretKey")
        private String secretKey;

        public Key() {
        }

        protected Key(@NonNull Parcel in) {
            apiKey = in.readString();
            secretKey = in.readString();
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (apiKey != null ? !apiKey.equals(key.apiKey) : key.apiKey != null) return false;
            return secretKey != null ? secretKey.equals(key.secretKey) : key.secretKey == null;

        }

        @Override
        public int hashCode() {
            int result = apiKey != null ? apiKey.hashCode() : 0;
            result = 31 * result + (secretKey != null ? secretKey.hashCode() : 0);
            return result;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(apiKey);
            dest.writeString(secretKey);
        }
    }

    public static class Aws implements Parcelable {
        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Aws> CREATOR = new Parcelable.Creator<Aws>() {
            @NonNull
            @Override
            public Aws createFromParcel(@NonNull Parcel in) {
                return new Aws(in);
            }

            @NonNull
            @Override
            public Aws[] newArray(int size) {
                return new Aws[size];
            }
        };
        @SerializedName("host")
        private String host;
        @SerializedName("port")
        private String port;
        @SerializedName("caCert")
        private String caCert;
        @SerializedName("clientCert")
        private String clientCert;
        @SerializedName("privateKey")
        private String privateKey;

        public Aws() {

        }

        protected Aws(@NonNull Parcel in) {
            host = in.readString();
            port = in.readString();
            caCert = in.readString();
            clientCert = in.readString();
            privateKey = in.readString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Aws aws = (Aws) o;

            if (host != null ? !host.equals(aws.host) : aws.host != null) return false;
            if (port != null ? !port.equals(aws.port) : aws.port != null) return false;
            if (caCert != null ? !caCert.equals(aws.caCert) : aws.caCert != null) return false;
            if (clientCert != null ? !clientCert.equals(aws.clientCert) : aws.clientCert != null)
                return false;
            return privateKey != null ? privateKey.equals(aws.privateKey) : aws.privateKey == null;

        }

        @Override
        public int hashCode() {
            int result = host != null ? host.hashCode() : 0;
            result = 31 * result + (port != null ? port.hashCode() : 0);
            result = 31 * result + (caCert != null ? caCert.hashCode() : 0);
            result = 31 * result + (clientCert != null ? clientCert.hashCode() : 0);
            result = 31 * result + (privateKey != null ? privateKey.hashCode() : 0);
            return result;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getCaCert() {
            return caCert;
        }

        public void setCaCert(String caCert) {
            this.caCert = caCert;
        }

        public String getClientCert() {
            return clientCert;
        }

        public void setClientCert(String clientCert) {
            this.clientCert = clientCert;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(host);
            dest.writeString(port);
            dest.writeString(caCert);
            dest.writeString(clientCert);
            dest.writeString(privateKey);
        }
    }

    public static class Ibm implements Parcelable {
        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Ibm> CREATOR = new Parcelable.Creator<Ibm>() {
            @NonNull
            @Override
            public Ibm createFromParcel(@NonNull Parcel in) {
                return new Ibm(in);
            }

            @NonNull
            @Override
            public Ibm[] newArray(int size) {
                return new Ibm[size];
            }
        };
        @SerializedName("organizationId")
        private String organicationId;
        @SerializedName("gatewayType")
        private String gatewayType;
        @SerializedName("gatewayId")
        private String gatewayId;
        @SerializedName("authMethod")
        private String authMethod;
        @SerializedName("authToken")
        private String authToken;

        public Ibm() {
        }

        protected Ibm(@NonNull Parcel in) {
            organicationId = in.readString();
            gatewayType = in.readString();
            gatewayId = in.readString();
            authMethod = in.readString();
            authToken = in.readString();

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Ibm ibm = (Ibm) o;

            if (organicationId != null ? !organicationId.equals(ibm.organicationId) : ibm.organicationId != null)
                return false;
            if (gatewayType != null ? !gatewayType.equals(ibm.gatewayType) : ibm.gatewayType != null)
                return false;
            if (gatewayId != null ? !gatewayId.equals(ibm.gatewayId) : ibm.gatewayId != null)
                return false;
            if (authMethod != null ? !authMethod.equals(ibm.authMethod) : ibm.authMethod != null)
                return false;
            return authToken != null ? authToken.equals(ibm.authToken) : ibm.authToken == null;

        }

        @Override
        public int hashCode() {
            int result = organicationId != null ? organicationId.hashCode() : 0;
            result = 31 * result + (gatewayType != null ? gatewayType.hashCode() : 0);
            result = 31 * result + (gatewayId != null ? gatewayId.hashCode() : 0);
            result = 31 * result + (authMethod != null ? authMethod.hashCode() : 0);
            result = 31 * result + (authToken != null ? authToken.hashCode() : 0);
            return result;
        }

        public String getOrganicationId() {
            return organicationId;
        }

        public void setOrganicationId(String organicationId) {
            this.organicationId = organicationId;
        }

        public String getGatewayType() {
            return gatewayType;
        }

        public void setGatewayType(String gatewayType) {
            this.gatewayType = gatewayType;
        }

        public String getGatewayId() {
            return gatewayId;
        }

        public void setGatewayId(String gatewayId) {
            this.gatewayId = gatewayId;
        }

        public String getAuthMethod() {
            return authMethod;
        }

        public void setAuthMethod(String authMethod) {
            this.authMethod = authMethod;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(organicationId);
            dest.writeString(gatewayType);
            dest.writeString(gatewayId);
            dest.writeString(authMethod);
            dest.writeString(authToken);
        }
    }

    public static class Azure implements Parcelable {
        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Azure> CREATOR = new Parcelable.Creator<Azure>() {
            @NonNull
            @Override
            public Azure createFromParcel(@NonNull Parcel in) {
                return new Azure(in);
            }

            @NonNull
            @Override
            public Azure[] newArray(int size) {
                return new Azure[size];
            }
        };
        @SerializedName("accessKey")
        private String accessKey;
        @SerializedName("host")
        private String host;

        public Azure() {
        }

        protected Azure(@NonNull Parcel in) {
            accessKey = in.readString();
            host = in.readString();
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Azure azure = (Azure) o;

            if (accessKey != null ? !accessKey.equals(azure.accessKey) : azure.accessKey != null)
                return false;
            return host != null ? host.equals(azure.host) : azure.host == null;

        }

        @Override
        public int hashCode() {
            int result = accessKey != null ? accessKey.hashCode() : 0;
            result = 31 * result + (host != null ? host.hashCode() : 0);
            return result;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(accessKey);
            dest.writeString(host);
        }
    }
}