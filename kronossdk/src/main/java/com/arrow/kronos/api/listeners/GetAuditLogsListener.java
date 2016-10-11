package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.AuditLogsResponse;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GetAuditLogsListener {
    void onGatewayLogsReceived(AuditLogsResponse response);
    void onGatewayLogsFailed();
}
