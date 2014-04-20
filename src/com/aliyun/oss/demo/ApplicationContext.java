
/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
 package com.aliyun.oss.demo; 

import com.aliyun.android.oss.OSSClient;

import android.app.Application;

/** 
 * 
 * @author Ruici 
 * 
 */
public class ApplicationContext extends Application {

    public final static String ACCESS_ID = "32LHJSMCD46aDVz5";
    
    public final static String ACCESS_KEY = "l3CSnGeZPe6QWolIO9LiFj8AAoasP3";
    
    private OSSClient client;
    
    

    public ApplicationContext() {
        super();
        client = new OSSClient();
        client.setAccessId(ACCESS_ID);
        client.setAccessKey(ACCESS_KEY);
    }



    public OSSClient getClient() {
        return client;
    }
}

