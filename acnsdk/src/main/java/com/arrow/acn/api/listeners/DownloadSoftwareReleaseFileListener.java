/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.listeners;

import com.arrow.acn.api.models.ApiError;

/**
 * Created by batrakov on 26.01.18.
 */

public interface DownloadSoftwareReleaseFileListener {

    /**
     * Call if request for downloading file was successfully done.
     * @param bytes - byte array that represent received file from response body
     * @param md5 - checksum for received file
     */
    void onDownloadSoftwareReleaseFileListenerSuccess(byte[] bytes, String md5);

    void onDownloadSoftwareReleaseFileListenerError(ApiError apiError);
}
