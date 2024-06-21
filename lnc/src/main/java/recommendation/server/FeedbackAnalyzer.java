package recommendation.server;

public class FeedbackAnalyzer {
        private TextPreprocessor preprocessor;
        private SentimentAnalyzer sentimentAnalyzer;
        private RatingEngine ratingEngine;
    
        public FeedbackAnalyzer() {
            this.preprocessor = new TextPreprocessor();
            this.sentimentAnalyzer = new SentimentAnalyzer();
            this.ratingEngine = new RatingEngine();
        }
    
        public double processFeedbacks(String longTextFeedbacks) {
            String[] feedbacks = longTextFeedbacks.split("\n");
            double totalRating = 0.0;
            int count = 0;
    
            for (String feedback : feedbacks) {
                String processedFeedback = preprocessor.preprocess(feedback);
                double sentimentScore = sentimentAnalyzer.analyzeSentiment(processedFeedback);
                double rating = ratingEngine.calculateRating(sentimentScore);
                totalRating += rating;
                count++;
            }
    
            return totalRating / count;
        }
    }
    