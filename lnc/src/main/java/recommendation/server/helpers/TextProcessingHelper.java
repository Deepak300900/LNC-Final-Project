package recommendation.server.helpers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TextProcessingHelper {
    private Set<String> stopWords;
    private Set<String> negationWords;

    public TextProcessingHelper() {
        this.stopWords = new HashSet<>(Arrays.asList(
            "a", "an", "and", "the", "is", "in", "at", "of", "on", "for", "with", "to", "from", "i", "it", "was", "but", "this", "that"
        ));
        this.negationWords = new HashSet<>(Arrays.asList(
            "not", "never", "no", "none", "nobody", "nothing", "neither", "nowhere", "hardly", "barely", "scarcely"
        ));
    }

    public String preprocess(String text) {
        // Convert to lower case
        text = text.toLowerCase();

        // Remove punctuation
        text = text.replaceAll("[^a-zA-Z\\s]", "");

        // Tokenization, Stopwords removal, and Negation handling
        StringBuilder processedText = new StringBuilder();
        boolean negate = false;
        for (String word : text.split("\\s+")) {
            if (negationWords.contains(word)) {
                negate = true;
            } else {
                if (!stopWords.contains(word)) {
                    if (negate) {
                        processedText.append("NOT_").append(word).append(" ");
                        negate = false;
                    } else {
                        processedText.append(word).append(" ");
                    }
                }
            }
        }

        return processedText.toString().trim();
    }
}
