/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pong;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author yaakov
 */
public class HighScoreKeeper implements Serializable {
    ArrayList<Integer> hScore = new ArrayList<>();
    private ArrayList<String> initials= new ArrayList<>();
    
    public HighScoreKeeper(ArrayList<Integer> score, ArrayList<String> initials){
//        System.arraycopy(score, 0, this.score, 0, score.size());
//        System.arraycopy(initials, 0, this.initials, 0, initials.size());
           for (int i = 0; i < 10; i++) {
             this.hScore.add(score.get(i));
             this.initials.add(initials.get(i));
            }
    
    }
}
