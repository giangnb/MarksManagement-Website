/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marksmana.beans;

import com.marksmana.controllers.ApiHelper;
import com.marksmana.info.Information;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class StatusBean implements Serializable{

    /**
     * Creates a new instance of StatusBean
     */
    public StatusBean() {
    }
    
    public boolean doGetAvailable() {
        Information i;
        try {
            i = ApiHelper.readFromApi("", Information.class);
            return !i.getValue("web_status").equals("0");
        } catch (Exception ex) {
        }
        return false;
    }
    
    public void doAlertContact() {
        try {
            Information i = ApiHelper.readFromApi("", Information.class);
            RequestContext.getCurrentInstance().execute("alert(\""+i.getValue("admin_contact")+"\")");
        } catch (Exception ex) {
        }
    }
}
