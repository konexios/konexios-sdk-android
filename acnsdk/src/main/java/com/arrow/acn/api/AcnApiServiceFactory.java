package com.arrow.acn.api;


/**
 * Created by osminin on 9/21/2016.
 */

public final class AcnApiServiceFactory {

    private static AcnApiService service;

    public static AcnApiService createAcnApiService() {
        service = new AcnApiImpl();
        return service;
    }

    public static AcnApiService getAcnApiService() {
        return service;
    }
}
