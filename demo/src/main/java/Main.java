import facade.AdminFacade;
import facade.MatchmakingFacade;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MatchmakingFacade clientFacade = new MatchmakingFacade();
        AdminFacade adminFacade = new AdminFacade();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("   VIDEO GAME MATCHMAKING FACADE SYSTEM DEMO      ");
        System.out.println("==================================================");

        while (true) {
            System.out.println("\nSelect an action:");
            System.out.println("1. Run Happy Path (Success: A -> B -> C)");
            System.out.println("2. Trigger Step B Failure (High Ping -> Level 1 Compensation: Undo A)");
            System.out.println("3. Trigger Step C Failure (Cheat Detected -> Level 2 Compensation: Undo B -> Undo A)");
            System.out.println("4. Inspect Persisted State (Admin Facade)");
            System.out.println("5. Reset Persisted Store (Clear Disk Files)");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            if (!scanner.hasNextInt()) {
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            if (choice == 0) {
                System.out.println("Exiting matchmaking system demo. Goodbye!");
                break;
            }

            String partyId = "PARTY_888";

            switch (choice) {
                case 1 -> {
                    System.out.println("\n[DEMO] Executing Happy Path...");
                    boolean ok = clientFacade.startMatchmaking(partyId, 2500, 40, false);
                    System.out.println("Matchmaking result: " + (ok ? "SUCCESS" : "FAILED"));
                }
                case 2 -> {
                    System.out.println("\n[DEMO] Triggering Step B failure (Ping = 250ms)...");
                    boolean ok = clientFacade.startMatchmaking(partyId, 2500, 250, false);
                    System.out.println("Matchmaking result: " + (ok ? "SUCCESS" : "FAILED"));
                }
                case 3 -> {
                    System.out.println("\n[DEMO] Triggering Step C failure (Cheat flag = true)...");
                    boolean ok = clientFacade.startMatchmaking(partyId, 2500, 40, true);
                    System.out.println("Matchmaking result: " + (ok ? "SUCCESS" : "FAILED"));
                }
                case 4 -> adminFacade.inspectState(partyId);
                case 5 -> adminFacade.resetAllData();
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}