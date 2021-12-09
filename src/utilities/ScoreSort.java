package utilities;

import ucd.comp2011j.engine.Score;

public class ScoreSort {
    public static Score[] bubbleSort(Score[] scores) {
         if (scores.length == 0)
             return scores;
         for (int i = 0; i < scores.length; i++) {
             for (int j = 0; j < scores.length - 1 - i; j++) {
                 if (scores[j + 1].getScore() > scores[j].getScore()) {
                     Score temp = scores[j + 1];
                     scores[j + 1] = scores[j];
                     scores[j] = temp;
                 }
            }
         }
        return scores;
    }

    public static void main(String[] args) {
        Score[] scores = PersistentScoreKeeper.getInstance().getScores();
        ScoreSort.bubbleSort(scores);
        for (Score s : scores) {
            System.out.println(s.getName() + ", " + s.getScore());
        }

    }
}
