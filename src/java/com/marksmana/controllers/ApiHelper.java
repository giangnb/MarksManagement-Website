/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marksmana.controllers;

import com.marksmana.utils.Json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 *
 * @author Giang
 */
public class ApiHelper {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static String readFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return jsonText.equals("")?null:jsonText;
        } finally {
            is.close();
        }
    }
    
    public static <E extends Object> E readFromApi(String subUrl, Class<E> c) throws Exception {
        return Json.DeserializeObject(readFromUrl("http://marksmanager.56mfu9rwbj.us-east-1.elasticbeanstalk.com/api/viewer/"+subUrl), c);
    }
}
