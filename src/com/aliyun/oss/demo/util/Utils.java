/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.oss.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ruici
 */
public class Utils {

    public static String getObjectKeyName(String key) {
        if (key.length() == 0) {
            return key;
        }
        
        int index = key.substring(0, key.length() - 1).lastIndexOf('/');
        if (index == -1) {
            return key;
        }
        
        return key.substring(index + 1);
    }
    
    public static byte[] readBytes(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = in.read(b)) != -1) {
            buffer.write(b, 0, len);
        }
        
        return buffer.toByteArray();
    }
}
