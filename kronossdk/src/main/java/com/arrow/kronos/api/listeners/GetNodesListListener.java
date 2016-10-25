package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.NodeModel;

import java.util.List;

/**
 * Created by osminin on 10/11/2016.
 */

public interface GetNodesListListener {
    void onGetListNodesSuccess(List<NodeModel> result);
    void onGetListNodesFailed();
}
