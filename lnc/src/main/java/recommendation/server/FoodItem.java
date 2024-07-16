package recommendation.server;

public class FoodItem {
    private int id;
    private String name;
    private float price;
    private String category;
    private String test;

    public FoodItem(int id, String name, float price, String category, String test) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public String getCategory() {
        return category;
    }

    public String getTest() {
        return test;
    }
}
