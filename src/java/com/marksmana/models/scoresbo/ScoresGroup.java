/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marksmana.models.scoresbo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Giang
 */
public class ScoresGroup implements Serializable{

    public String subject;
    public SingleScore[] scores;
    public double avg = 0d;
    private ArrayList<SingleScore> list = new ArrayList<SingleScore>();

    public ScoresGroup() {
        list = new ArrayList<SingleScore>();
    }

    public ScoresGroup(String subject) {
        this.subject = subject;
    }
    
    void addScore(double score, int coeff, String teacher) {
        SingleScore s = new SingleScore(score, coeff, teacher);
        list.add(s);
    }

    void calculateAvg() {
        prepare();
        double count = 0d;
        double sum = 0d;
        for (SingleScore score : scores) {
            sum += (score.score * score.coeff);
            count += (double)score.coeff;
        }
        avg = sum/count;
    }
    
    private void prepare() {
        Collections.sort(list);
        scores = list.toArray(new SingleScore[list.size()]);
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
