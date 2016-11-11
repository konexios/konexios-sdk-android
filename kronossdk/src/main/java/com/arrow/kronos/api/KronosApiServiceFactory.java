package com.arrow.kronos.api;


/**
 * Created by osminin on 9/21/2016.
 */

public final class KronosApiServiceFactory {

    private static KronosApiService service;

    public static KronosApiService createKronosApiService() {
        service = new KronosApiImpl();
        return service;
    }

    public static KronosApiService getKronosApiService() {
        return service;
    }
}
