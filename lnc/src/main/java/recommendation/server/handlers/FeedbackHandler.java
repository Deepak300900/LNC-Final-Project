package recommendation.server.handlers;

import recommendation.server.helpers.RatingCalculationHelper;
import recommendation.server.helpers.SentimentAnalysisHelper;
import recommendation.server.helpers.TextProcessingHelper;

public class FeedbackHandler {
        private TextProcessingHelper preprocessor;
        private SentimentAnalysisHelper sentimentAnalyzer;
        private RatingCalculationHelper ratingEngine;
    
        public FeedbackHandler() {
            this.preprocessor = new TextProcessingHelper();
            this.sentimentAnalyzer = new SentimentAnalysisHelper();
            this.ratingEngine = new RatingCalculationHelper();
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
    