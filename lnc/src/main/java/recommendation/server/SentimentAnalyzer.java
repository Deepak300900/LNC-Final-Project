package recommendation.server;

import java.util.HashMap;
import java.util.Map;

public class SentimentAnalyzer {
    private Map<String, Double> sentimentLexicon;

    public SentimentAnalyzer() {
        this.sentimentLexicon = new HashMap<>();
        // Expanded sentiment lexicon
        sentimentLexicon.put("amazing", 2.0);
        sentimentLexicon.put("great", 1.5);
        sentimentLexicon.put("good", 1.0);
        sentimentLexicon.put("enjoyed", 1.0);
        sentimentLexicon.put("definitely", 1.0);
        sentimentLexicon.put("back", 0.5);
        sentimentLexicon.put("food", 0.5);
        sentimentLexicon.put("service", 0.5);
        sentimentLexicon.put("ambiance", 0.5);
        sentimentLexicon.put("bad", -1.5);
        sentimentLexicon.put("terrible", -2.0);
        sentimentLexicon.put("horrible", -2.5);
        sentimentLexicon.put("awful", -2.0);
        sentimentLexicon.put("okay", -0.5);
        sentimentLexicon.put("not", -1.0);
        sentimentLexicon.put("delicious", 1.5);
        sentimentLexicon.put("disgusting", -2.0);
        sentimentLexicon.put("loved", 1.5);
        sentimentLexicon.put("hate", -2.0);
        sentimentLexicon.put("nice", 1.0);
        sentimentLexicon.put("poor", -1.5);
        sentimentLexicon.put("delightful", 1.5);
        sentimentLexicon.put("wonderful", 1.5);
        sentimentLexicon.put("fantastic", 2.0);
        sentimentLexicon.put("mediocre", -1.0);
        sentimentLexicon.put("unpleasant", -1.5);
        sentimentLexicon.put("disappointing", -1.5);
    }

    public double analyzeSentiment(String text) {
        double sentimentScore = 0.0;
        int wordCount = 0;

        for (String word : text.split("\\s+")) {
            boolean isNegated = false;
            if (word.startsWith("NOT_")) {
                isNegated = true;
                word = word.substring(4);
            }
            if (sentimentLexicon.containsKey(word)) {
                double score = sentimentLexicon.get(word);
                sentimentScore += isNegated ? -score : score;
                wordCount++;
            }
        }

        // Consider the frequency of sentiment words
        double weightedScore = wordCount == 0 ? 0.0 : sentimentScore / wordCount;
        return weightedScore;
    }
}
