package recommendation.client.factories;

import recommendation.client.commands.ChefGeneralCommand;
import recommendation.client.commands.DefaultCommand;
import recommendation.client.commands.DiscardableFoodProcessCommand;
import recommendation.client.commands.SaveSelectedFoodCommand;
import recommendation.client.commands.SaveSuggestedFoodCommand;
import recommendation.client.interfaces.ChefCommand;

public class ChefCommandFactory {
    public ChefCommand getCommand(String choice) {
        switch (choice) {
            case "1":
                return new ChefGeneralCommand();
            case "2":
                return new ChefGeneralCommand(); 
            case "3":
                return new SaveSuggestedFoodCommand();
            case "4":
                return new ChefGeneralCommand();
            case "5":
                return new SaveSelectedFoodCommand();
            case "6":
                return new DiscardableFoodProcessCommand(); 
            case "7":
                return new ChefGeneralCommand(); 
            default:
                return new DefaultCommand();
        }
    }
}
