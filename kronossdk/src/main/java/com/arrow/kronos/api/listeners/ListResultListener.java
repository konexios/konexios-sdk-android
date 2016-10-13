package com.arrow.kronos.api.listeners;

import java.util.List;

/**
 * Created by osminin on 12.10.2016.
 */

public interface ListResultListener<T> {
    void onRequestSuccess(List<T> list);
    void onRequestError();
}
