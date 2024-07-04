package recommendation.server;

public class RatingEngine {
        public double calculateRating(double sentimentScore) {
            double normalizedScore = (sentimentScore + 2.5) / 5.0 * 98.99 + 1.00;
            return Math.round(normalizedScore * 100.0) / 100.0;
        }
    }
    