package utilities;

import ucd.comp2011j.engine.Score;
import ucd.comp2011j.engine.ScoreKeeper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersistentScoreKeeper implements ScoreKeeper {
    private static final int NUM_LIMIT = 10;
    private List<Score> scores;

    private static PersistentScoreKeeper scoreKeeper = new PersistentScoreKeeper();

    public static PersistentScoreKeeper getInstance() {
        return scoreKeeper;
    }

    private PersistentScoreKeeper() {
        super();
        scores = new ArrayList<>();
        Collections.addAll(scores, getScores());

    }
    @Override
    public void addScore(String name, int score) {
        if (name == null) return;

        if (scores.size() < NUM_LIMIT) {
            scores.add(new Score(name, score));
        }
        else {
            if (score >= getLowestScore()) {
                int idx = getLowestScoreIndex();
                scores.remove(idx);
                scores.add(new Score(name, score));
            }
        }
        saveScores();

    }

    @Override
    public void saveScores() {
        try {
            String file = "scores.txt";
            File writeFile = new File(PersistentScoreKeeper.class.getResource(file).toURI().getPath());
            FileWriter fw = new FileWriter(writeFile,false);

            for (Score s : scores) {
                fw.write(s.getName());
                fw.write(",");
                fw.write(String.valueOf(s.getScore()));
                fw.write("\n");
            }
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Score[] getScores() {
        try {
            List<String> list = new ArrayList<>();
            String file = "scores.txt";
            File filename = new File(PersistentScoreKeeper.class.getResource(file).toURI().getPath());
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String s;
            while ((s = br.readLine()) != null) {
                list.add(s);
            }
            if (list.size() <= 0) {
                return new Score[0];
            }
            br.close();
            Score[] scores = new Score[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String result = list.get(i);
                Score score = new Score(result.split(",")[0], Integer.parseInt(result.split(",")[1]));
                scores[i] = score;
            }

            return scores;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Score[0];
    }

    @Override
    public int getLowestScore() {
        if (scores.size() < 10) {
            return 0;
        }
        int low = Integer.MAX_VALUE;
        for (Score s : scores) {
            low = Math.min(s.getScore(), low);
        }
        return low;
    }

    private int getLowestScoreIndex() {
        int low = Integer.MAX_VALUE;
        int res = 0;
        for (int i = 0; i < scores.size(); i++) {
            if (low > scores.get(i).getScore()) {
                low = scores.get(i).getScore();
                res = i;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        PersistentScoreKeeper keeper = new PersistentScoreKeeper();
        System.out.println(keeper.getLowestScore());

        keeper.addScore("kerry", 20000);

    }
}
