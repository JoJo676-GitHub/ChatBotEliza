package de.jojo676.chatbot;

import java.io.*;
import java.util.*;

public class Chat {

    Scanner scanner = new Scanner(System.in);
    ArrayList<String> input = new ArrayList<>();
    BufferedReader reader;
    ArrayList<ArrayList<String>> dictionary = new ArrayList<>();
    Map<String, List<Integer>> bagOfWords = new HashMap<>();
    Map<String, String> answers;

    public Chat() { //Phrase in bestandteile zerlegen und bei einzelnen wörtern auf Phrase zeigen

        try {
            reader = new BufferedReader(new FileReader("C:\\Users\\Johan\\IdeaProjects\\ChatBotOhneKI\\src\\main\\resources\\openthesaurus.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Methode random hallo
        answers = Map.of(
                "traurig", "Das tut mir leid zu hören",
                "hallo", "Hallo, wie geht es dir?",
                "vater", "Erzähl mir mehr von deiner Familie",
                "gut", "Das freut mich zu hören!",
                "wie geht's", "Mir geht es gut, danke der Nachfrage",
                "tschüs", "Auf wiedersehen, es hat mich gefreut mit dir Bekanntschaft zu machen!"
        );

        try {

            String zeile;

            while ((zeile = reader.readLine()) != null) {
                zeile = clearString(zeile);
                ArrayList<String> zeilenWorte = new ArrayList<>();
                dictionary.add(zeilenWorte);

                for (String word : zeile.split(";")) {

                    word = word.strip();
                    zeilenWorte.add(word);

                    if (bagOfWords.containsKey(word)) {
                        bagOfWords.get(word).add(dictionary.size() - 1);
                    } else {
                        ArrayList<Integer> places = new ArrayList<>();
                        bagOfWords.put(word, places); // wenn in dem Bag of words in der Spalte vom INT noch keine ArrayList ist, wird diese hinzugefügt
                        places.add(dictionary.size() - 1); //"add" fügt zu der ^ Liste hinzu
                    }

                }
            }

            //            System.out.println("Z51");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findSynonyms(String key) {

        if (bagOfWords.get(key) != null) {
            ArrayList<String> allSynonyms = new ArrayList<>();
            for (int i = 0; i < bagOfWords.get(key).size(); i++) {
                allSynonyms.addAll(dictionary.get(bagOfWords.get(key).get(i)));
            }
            System.out.println(findKeyWord(allSynonyms));
        } else {
            System.out.println("Leider ist mir dieser Ausdruck nicht bekannt");
        }
    }

    public void input() {

        while (true) {
            String scannerOutput = clearString(scanner.nextLine());
            if (!scannerOutput.isEmpty()) {
                input.add(scannerOutput);

                for (String key : input) {
                    findSynonyms(key);
                }
            }

            input.clear();
        }
    }

    public String findKeyWord(ArrayList<String> synonyms) {

        System.out.println(synonyms);


        for (String synonym : synonyms) {
            if (answers.containsKey(synonym)) {
                return answers.get(synonym);
            }
        }
        return "Das habe ich leider nicht verstanden";
    }

    public String clearString(String uncleared) {

        uncleared = uncleared.toLowerCase();
        uncleared = uncleared.replaceAll("\\([^)]*\\)", "");
        uncleared = uncleared.replaceAll("`|~|!|@|\\$|%|\\^|&|\\*|\\(|\\)|\\+|=|\\[|\\{|]|}|\\||\\\\|,|\\.|\\?|/|\"\"|:", "");
        uncleared = uncleared.strip();
        return uncleared;
    }
}
