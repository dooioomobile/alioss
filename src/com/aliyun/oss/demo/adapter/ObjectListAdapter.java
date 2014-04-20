
/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
 package com.aliyun.oss.demo.adapter; 

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.aliyun.android.oss.model.OSSObjectSummary;
import com.aliyun.android.oss.model.query.OSSQuery;
import com.aliyun.android.widget.PaginationAdapter;
import com.aliyun.oss.demo.R;
import com.aliyun.oss.demo.util.Utils;

/** 
 * 
 * @author Ruici 
 * 
 */
 public class ObjectListAdapter extends PaginationAdapter<OSSObjectSummary> {

     private Activity mContext;
     
     /** * @param query */
     public ObjectListAdapter(Activity context, OSSQuery<OSSObjectSummary> query) {
         super(query);
         this.mContext = context;
     }

     /* (non-Javadoc) * @see com.aliyun.android.widget.PaginationAdapter#getPaginationView(int, android.view.View, android.view.ViewGroup) */
     @Override
     public View getPaginationView(int position, View convertView,
             ViewGroup parent) {
         View res = convertView;
         if (res == null) {
             res = mContext.getLayoutInflater().inflate(R.layout.object_item, null);
         }
         
         TwoLineListItem item = (TwoLineListItem) res;
         TextView textView1 = item.getText1();
         TextView textView2 = item.getText2();
         
         OSSObjectSummary object = (OSSObjectSummary) getItem(position);
         textView1.setText(Utils.getObjectKeyName(object.getKey()));
         String size = String.format("%.3fKB", object.getSize() / 1024.0);
         String date = object.getLastModified() == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(object.getLastModified());
         if (!object.isDirectory()) {
             textView2.setText(size + " , " + date);
         }
         
         return res;
     }
     
 }