package recommendation.client;

public class ChefCommandFactory {
    public ChefCommand getCommand(String choice) {
        switch (choice) {
            case "1":
                return new ChefBasicCommand();
            case "2":
                return new ChefBasicCommand(); 
            case "3":
                return new AddFoodItemToWillPrepareCommand();
            case "4":
                return new ChefBasicCommand();
            case "5":
                return new AddFoodItemToPreparedCommand();
            case "6":
                return new ChefBasicCommand(); 
            case "7":
                return new ChefBasicCommand(); 
            default:
                return new DefaultCommand();
        }
    }
}
