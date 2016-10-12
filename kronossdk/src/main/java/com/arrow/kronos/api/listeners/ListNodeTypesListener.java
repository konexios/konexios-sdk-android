package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.ListResultModel;
import com.arrow.kronos.api.models.NodeTypeModel;

/**
 * Created by osminin on 12.10.2016.
 */

public interface ListNodeTypesListener {
    void onListNodeTypesSuccess(ListResultModel<NodeTypeModel> result);
    void onListNodeTypesFiled();
}
