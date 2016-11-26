/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marksmana.beans;

import com.marksmana.controllers.ApiHelper;
import com.marksmana.info.Information;
import com.marksmana.info.SingleInformation;
import com.marksmana.utils.Json;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@SessionScoped
public class ViewBean implements Serializable{
    private int currentPage = 0;
    private Information info;

    /**
     * Creates a new instance of viewBean
     */
    public ViewBean() {
        try {
            info = ApiHelper.readFromApi("", Information.class);
        } catch (Exception ex) {
            info = new Information();
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Information getInfo() {
        return info;
    }

    public void setInfo(Information info) {
        this.info = info;
    }
    
    public String doGotoPage(int id, String name) {
        currentPage = id;
        return name;
    }
    
    public String doGetProp(String key) {
        String value = info.getValue(key);
        if (value==null) {
            return "";
        }
        return value.replace("\n", " ; ");
    }
    
    public SingleInformation[] doGetInformation(String key) {
        Information i = new Information();
        try {
            i = Json.DeserializeObject(ApiHelper.readFromApi("", Information.class).getValue(key).replace("\\\"", "\""), Information.class);
        } catch (Exception ex) {
        }
        return i.toArray(new SingleInformation[i.size()]);
    }
}
