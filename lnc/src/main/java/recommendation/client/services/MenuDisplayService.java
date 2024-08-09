package recommendation.client.services;

public class MenuDisplayService {
    public static void showChefMenu() {
        System.out.println("\n1. Show Food Menu Items");
        System.out.println("2. Show Recommended Food");
        System.out.println("3. Suggest Foods for Tomorrow");
        System.out.println("4. Show Voting on Suggested Foods for Tomorrow");
        System.out.println("5. Select Food Item for Tomorrow");
        System.out.println("6. Show Discardable Food");
        System.out.println("7. Show Destroyed Food Feedback");
        System.out.println("Type 'exit' to exit.");
        System.out.print("Enter your choice: ");
    }

    public static void showEmployeeMenu() {
        System.out.println("\nEmployee Service Menu:");
        System.out.println("1. Feedback for Today");
        System.out.println("2. Vote for Tomorrow");
        System.out.println("3. Discardable Items Feedback");
        System.out.println("4. Show Notifications");
        System.out.println("5. Update Profile");
        System.out.println("Type 'EXIT' to quit");
        System.out.print("Select an option: ");
    }

    public static void showAdminMenu() {
        System.out.println("\n1. ADD_MENU_ITEM");
        System.out.println("2. UPDATE_MENU_ITEM");
        System.out.println("3. DELETE_MENU_ITEM");
        System.out.println("4. SHOW_MENU");
        System.out.println("5. VIEW_USER_ACTIVITY");
        System.out.println("6. EXIT");
        System.out.print("Enter your choice: ");
    }
}
