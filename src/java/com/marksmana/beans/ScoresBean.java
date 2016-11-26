/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marksmana.beans;

import com.marksmana.controllers.ApiHelper;
import com.marksmana.info.SingleInformation;
import com.marksmana.models.Score;
import com.marksmana.models.ScoreLog;
import com.marksmana.models.scoresbo.ScoresGroup;
import com.marksmana.models.scoresbo.ScoresRecord;
import com.marksmana.models.scoresbo.SingleScore;
import com.marksmana.utils.Json;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Giang
 */
@ManagedBean
@RequestScoped
public class ScoresBean implements Serializable {

    private int id;
    private ArrayList<String[]> list;
    private ScoresRecord record;

    /**
     * Creates a new instance of ScoresBean
     */
    public ScoresBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String[]> getList() {
        return list;
    }

    public void setList(ArrayList<String[]> list) {
        this.list = list;
    }

    public ScoresRecord getRecord() {
        return record;
    }

    public void setRecord(ScoresRecord record) {
        this.record = record;
    }

    public String doLoadScore(int studentId, String logId) {
        try {
            id = Integer.parseInt(logId);
        } catch (NumberFormatException ex) {
            return "student";
        }
        loadArchive(id, studentId);
        return "logview";
    }

    public String doLoadCurrent(int studentId) {
        loadCurrentScores(studentId);
        return "logview";
    }

    private ArrayList<String[]> loadCurrentScores(int studentId) {
        list = new ArrayList<>();
        int minCoeff = getMinCoeff();
        int maxCoeff = getMaxCoeff();
        try {
            String[] ctx = null;
            int coeff;
            Score[] scores = ApiHelper.readFromApi("student/" + studentId + "/scores", Score[].class);
            for (Score s : scores) {
                System.out.println(s.getScore());
            }
            System.out.println(scores);
            Hashtable<Integer, String> subjects = new Hashtable<>();
            // Init subjects
            for (Score s : scores) {
                subjects.put(s.getSubjectId().getId(), s.getSubjectId().getName());
            }
            // Table header
            ctx = new String[maxCoeff - minCoeff + 1 + 1];
            ctx[0] = "Môn học";
            for (int i = minCoeff; i <= maxCoeff; i++) {
                ctx[i] = "Hệ số " + i;
            }
            list.add(ctx);
            // Table body
            for (Integer key : subjects.keySet()) {
                ctx = new String[maxCoeff - minCoeff + 1 + 1]; // Cols needed for scores = max - min + 1 plus 1 col for subject name
                ctx[0] = subjects.get(key);
                for (Score s : scores) {
                    if (s.getSubjectId().getId().equals(key)) {
                        coeff = Integer.parseInt(s.getCoefficient() + "");
                        if (ctx[coeff] == null) {
                            ctx[coeff] = "";
                        }
                        ctx[coeff] += s.getScore() + " ; ";
                    }
                }
                list.add(ctx);
            }
        } catch (Exception ex) {
        }
        return list;
    }

    private ArrayList<String[]> loadArchive(int id, int studentId) {
        list = new ArrayList<>();
        int minCoeff = getMinCoeff();
        int maxCoeff = getMaxCoeff();
        try {
            ScoreLog[] logs = ApiHelper.readFromApi("student/" + studentId + "/logs", ScoreLog[].class);
            for (ScoreLog log : logs) {
                if (log.getId() == id) {
                    String[] ctx;
                    // Load scores
                    record = Json.DeserializeObject(log.getScores(), ScoresRecord.class);
                    // Table header
                    ctx = new String[maxCoeff - minCoeff + 1 + 2];
                    ctx[0] = "Môn học";
                    for (int i = minCoeff; i <= maxCoeff; i++) {
                        ctx[i] = "Hệ số " + i;
                    }
                    ctx[ctx.length-1] = "Trung bình";
                    list.add(ctx);
                    // Table body
                    for (ScoresGroup sg : record.scores) {
                        ctx = new String[maxCoeff - minCoeff + 1 + 2];
                        ctx[0] = sg.subject;
                        ctx[ctx.length-1] = sg.avg+"";
                        for (SingleScore ss : sg.scores) {
                            if (ctx[ss.coeff]==null) {
                                ctx[ss.coeff] = "";
                            }
                            ctx[ss.coeff] += ss.score+" ; ";
                        }
                        list.add(ctx);
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private int getMinCoeff() {
        SingleInformation si;
        try {
            si = ApiHelper.readFromApi("prop/min_coeff", SingleInformation.class);
            return Integer.parseInt(si.getValue());
        } catch (Exception ex) {
            return 0;
        }
    }

    private int getMaxCoeff() {
        SingleInformation si;
        try {
            si = ApiHelper.readFromApi("prop/max_coeff", SingleInformation.class);
            return Integer.parseInt(si.getValue());
        } catch (Exception ex) {
            return 0;
        }
    }
}
