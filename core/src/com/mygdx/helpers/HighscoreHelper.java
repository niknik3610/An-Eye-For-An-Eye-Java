package com.mygdx.helpers;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Scanner;

public class HighscoreHelper {
    private static HighscoreHelper highscoreHelper;

    public static HighscoreHelper getHighscoreHelper(){
        if (highscoreHelper == null) {
            highscoreHelper = new HighscoreHelper();
        }
        return  highscoreHelper;
    }
    public void saveNewHighscore(int time) {
        JSONArray array;
        try {
            int highScorePos = 0;
            File file = new File("high_scores.json");
            if (!file.createNewFile()) {
                String fileContents = getContentsOfFile(file);

                JSONParser parser = new JSONParser();
                Object obj = parser.parse(fileContents);
                array = (JSONArray) obj;

                highScorePos = checkHighScore(time, array);
                if (highScorePos < 0) {
                    return;
                }
                insertIntoArray(array, highScorePos, time);
            }
            else {
                array = new JSONArray();
                array.add(time);
                for (int i = 0; i < 4; i++) {
                    array.add(9999);
                }
            }
            StringWriter out = new StringWriter();
            array.writeJSONString(out);
            FileWriter writer = new FileWriter("high_scores.json");
            writer.write(out.toString());
            writer.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getHighscores(){
        try {
            File file = new File("high_scores.json");
            if (!file.exists() || file.isDirectory()) {
                return "Win a game! (Noob)";
            }
            String data = getContentsOfFile(file);
            data = data.substring(1, data.length() - 2);
            String[] splitData = data.split(",");
            data = "";
            for (int i = 0; i < splitData.length; i++) {
                if (!splitData[i].equals("9999")) {
                    data += i + 1 + ": " + splitData[i] + " Seconds\n";
                }
            }

            return data;
        } catch (Exception e) {
            System.out.println("Error Reading file");
            return "Win a game! (Noob)";
        }
    }

    private String getContentsOfFile(File file){
        try {
            Scanner reader = new Scanner(file);
            String data = "";
            while (reader.hasNextLine()){
                data += reader.nextLine() + "\n";
            }
            reader.close();
            return data;
        } catch (Exception e) {
            System.out.println("Could not read file");
            return null;
        }
    }

    private int checkHighScore(int time, JSONArray array){
        try {
            for (int i = 0; i < 5; i++) {
                if (((long) array.get(i)) > time) {
                    return i;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    private void insertIntoArray(JSONArray array, int pos, int time) {
        if (pos == 4) {
            array.set(4, time);
            return;
        }
        array.add(0);
        for (int i = 4; i > pos - 1; i-- ) {
            array.set(i + 1, array.get(i));
        }
        array.set(pos, time);
        array.remove(5);
    }

    public static void main(String[] args) {
        HighscoreHelper helper = new HighscoreHelper();
        helper.saveNewHighscore(69);
    }
}
