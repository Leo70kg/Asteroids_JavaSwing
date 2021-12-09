package screens;

import ucd.comp2011j.engine.Score;
import utilities.PersistentScoreKeeper;
import utilities.ScoreSort;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ScoreScreen extends JPanel {
    JTable jt;
    JScrollPane sp;
    Score[] scores;
    public ScoreScreen()
    {
        scores = PersistentScoreKeeper.getInstance().getScores();

        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent evt) {
                reload();
            }
        });

        ScoreSort.bubbleSort(scores);
        int n = scores.length;
        String[][] data = new String[n][];
        for (int i = 1; i <= scores.length; i++) {
            String idx = String.valueOf(i);
            String name = scores[i-1].getName();
            String score = String.valueOf(scores[i-1].getScore());
            data[i-1] = new String[]{idx, name, score};
        }

        String[] column = {"ID","NAME","SCORE"};

        jt = new JTable(data, column);
        jt.setBounds(30,40,200,300);
        jt.setEnabled(false);
        sp = new JScrollPane(jt);
        this.add(sp);
    }

    public void reload() {
        this.removeAll();

        scores = PersistentScoreKeeper.getInstance().getScores();
        ScoreSort.bubbleSort(scores);
        int n = scores.length;
        String[][] data = new String[n][];
        for (int i = 1; i <= scores.length; i++) {
            String idx = String.valueOf(i);
            String name = scores[i-1].getName();
            String score = String.valueOf(scores[i-1].getScore());
            data[i-1] = new String[]{idx, name, score};
        }

        String[] column = {"ID","NAME","SCORE"};

        jt = new JTable(data, column);
        jt.setBounds(30,40,200,300);
        jt.setEnabled(false);
        sp = new JScrollPane(jt);
        this.add(sp);

        this.revalidate();
    }


}
