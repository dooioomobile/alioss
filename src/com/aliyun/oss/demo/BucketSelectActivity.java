/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
 package com.aliyun.oss.demo; 

import java.util.ArrayList;
import java.util.List;

import com.aliyun.android.oss.asynctask.ListBucketAsyncTask;
import com.aliyun.android.oss.model.Bucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/** 
 * 
 * @author Ruici 
 * 
 */
public class BucketSelectActivity extends Activity{

    ArrayAdapter<String> adapter;
    ListView listview;
    
    /* (non-Javadoc) * @see android.app.Activity#onCreate(android.os.Bundle) */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bucket_select);
        
        listview = (ListView) findViewById(R.id.buckets);
        
        new ListBucketAsyncTask<Void>(ApplicationContext.ACCESS_ID, ApplicationContext.ACCESS_KEY) {

            @Override
            protected void onPostExecute(List<Bucket> result) {
                List<String> ret = new ArrayList<String>();
                for (Bucket bucket : result) {
                    ret.add(bucket.getName());
                }
                adapter = new ArrayAdapter<String>(BucketSelectActivity.this, R.layout.bucket_item, ret);
                listview.setAdapter(adapter);
            }
        }.execute();
        
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra("result", adapter.getItem(arg2));
                BucketSelectActivity.this.setResult(RESULT_OK, intent);
                finish();
            }});
    }

}

