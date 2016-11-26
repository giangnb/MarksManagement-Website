/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marksmana.beans;

import com.marksmana.controllers.ApiHelper;
import com.marksmana.info.Information;
import com.marksmana.models.Score;
import com.marksmana.models.ScoreLog;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.marksmana.models.Student;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Giang
 */
@ManagedBean
@SessionScoped
public class StudentBean implements Serializable{
    private int id;
    private Student student;

    /**
     * Creates a new instance of StudentBean
     */
    public StudentBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    public String doGetStudent() {
        try {
            student = ApiHelper.readFromApi("student/"+id, Student.class);
        } catch (Exception ex) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage("noStudent", new FacesMessage("Không tìm thấy học sinh"));
            return "index";
        }
        return "student";
    }
    
    public Information doGetScoresList() {
        Information i = new Information();
        try {
            if (ApiHelper.readFromApi("student/"+id+"/scores", List.class).size()>0) {
                i.put("0", "Hiện tại");
            }
            List<LinkedHashMap> logs = ApiHelper.readFromApi("student/"+id+"/logs", List.class);
            for (LinkedHashMap l : logs) {
                i.put(l.get("id").toString(), l.get("schoolYear").toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return i;
    }
}
