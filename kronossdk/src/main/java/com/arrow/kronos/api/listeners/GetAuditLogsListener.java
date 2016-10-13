package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.AuditLogModel;
import com.arrow.kronos.api.models.PagingResultModel;

import java.util.List;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GetAuditLogsListener {
    void onGatewayLogsReceived(List<AuditLogModel> response);
    void onGatewayLogsFailed();
}
