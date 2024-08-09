package recommendation.server.models;

public class FoodItem {
    private int id;
    private String name;
    private float price;
    private float averageRating;
    private String category;
    private String test;

    public FoodItem(int id, String name, float price, float averageRating, String category, String test) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.averageRating = averageRating;
        this.category = category;
        this.test = test;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public String getCategory() {
        return category;
    }

    public String getTest() {
        return test;
    }
}
