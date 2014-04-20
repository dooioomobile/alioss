package com.aliyun.oss.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aliyun.android.oss.asynctask.UploadObjectAsyncTask;
import com.aliyun.android.oss.model.OSSObjectSummary;
import com.aliyun.android.oss.model.query.OSSQuery;
import com.aliyun.android.oss.model.query.ObjectsQuery;
import com.aliyun.android.widget.PaginationAdapter;
import com.aliyun.android.widget.PaginationListView;
import com.aliyun.oss.demo.adapter.ObjectListAdapter;
import com.aliyun.oss.demo.util.Utils;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

    public static final int BUCKET_SELECT_REQUEST_CODE = 1;
    
    public static final int UPLOAD_REQUEST_CODE = 2;

    private PaginationListView listView;
    
    private PaginationAdapter<OSSObjectSummary> adapter;

    private String bucketName;

    private View bucketTextView;
    
    private View uploadTextView;
    
    private View refreshTextView;

    private View backButton;

    private List<String> currentPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (PaginationListView) findViewById(R.id.listview);
        listView.setLoadingView(getLayoutInflater().inflate(
                R.layout.loading_view, null));

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (currentPath.size() == 0) {
                    Toast.makeText(MainActivity.this, "已经到达根目录", Toast.LENGTH_SHORT).show();
                } else {
                    currentPath.remove(currentPath.size() - 1);
                    showBucketObjects();
                }
            }
        });
        bucketTextView = findViewById(R.id.bucket_textview);
        bucketTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,
                        BucketSelectActivity.class), BUCKET_SELECT_REQUEST_CODE);
            }
        });

        uploadTextView = findViewById(R.id.upload_textview);
        uploadTextView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (bucketName == null) {
                    Toast.makeText(MainActivity.this, "请先选择Bucket", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, UPLOAD_REQUEST_CODE);
            }
        });
        
        refreshTextView = findViewById(R.id.refresh_textview);
        refreshTextView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                showBucketObjects();
            }
            
        });
        
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                OSSObjectSummary object = (OSSObjectSummary) adapter.getItem(arg2);
                if (object.isDirectory()) {
                    currentPath.add(Utils.getObjectKeyName(object.getKey()));
                    showBucketObjects();
                }
            }
        });
        
        startActivityForResult(new Intent(MainActivity.this,
                BucketSelectActivity.class), BUCKET_SELECT_REQUEST_CODE);
    }

    private void showBucketObjects() {
        if (this.bucketName != null) {
            OSSQuery<OSSObjectSummary> query = new ObjectsQuery(
                    ApplicationContext.ACCESS_ID,
                    ApplicationContext.ACCESS_KEY, this.bucketName, getPath(), 15);
            this.adapter = new ObjectListAdapter(this, query);
            listView.setAdapter(this.adapter);
        }
    }

    private String getPath() {
        StringBuilder sb = new StringBuilder();
        for (String dir: this.currentPath) {
            sb.append(dir);
        }

        return sb.toString();
    }

    private byte[] getBytesData(Intent data){
        Uri uri = data.getData();
        try {
            return Utils.readBytes(this.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BUCKET_SELECT_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            this.bucketName = data.getExtras().getString("result");
            currentPath = new ArrayList<String>();
            showBucketObjects();
        } else if (requestCode == UPLOAD_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            byte[] bytes = getBytesData(data);
            if (bytes != null) {
                new UploadObjectAsyncTask<Void>(ApplicationContext.ACCESS_ID, ApplicationContext.ACCESS_KEY,
                        bucketName, getPath() + new Date().getTime() + ".jpg") {
                    
                    /* (non-Javadoc) * @see android.os.AsyncTask#onPostExecute(java.lang.Object) */
                    @Override
                    protected void onPostExecute(String result) {
                        if (result != null) {
                            showBucketObjects();
                        } else {
                            Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    
                }.execute(bytes);
            } else {
                Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
