import java.util.Scanner;

public class MissionariesAndCannibalsGame {
    private int leftM, leftC;
    private int rightM, rightC;
    private boolean boatLeft;
    private int crosses;
    private String lastError;
    private int totalM;
    private int totalC;

    public void play() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("MISSIONARIES AND CANNIBALS GAME");
        
        // Get initial configuration
        while (true) {
            System.out.print("Enter initial number of missionaries: ");
            totalM = scanner.nextInt();
            System.out.print("Enter initial number of cannibals: ");
            totalC = scanner.nextInt();
            
            if (totalM < 0 || totalC < 0) {
                System.out.println("Error: Numbers cannot be negative!");
                continue;
            }
            if (totalM > 0 && totalM < totalC) {
                System.out.println("Error: Cannibals can't outnumber missionaries initially!");
                continue;
            }
            break;
        }
        
        System.out.println("\nGoal: Get all " + totalM + " missionaries and " + totalC + " cannibals to the right Side");
        System.out.println("Rules:");
        System.out.println("1. Boat can carry 1-2 people");
        System.out.println("2. Cannibals must never outnumber missionaries on either Side");
        
        resetGame();
        
        while (true) {
            displayState();
            
            if (isGameOver()) {
                handleGameEnd(scanner);
                continue;
            }
            
            int[] move = getPlayerMove(scanner);
            if (move == null) continue;
            
            if (validateMove(move[0], move[1])) {
                if (executeMove(move[0], move[1])) {
                    crosses++;
                } else {
                    System.out.println(" Error: " + lastError);
                }
            } else {
                System.out.println(" Invalid move! " + lastError);
            }
        }
    }

    private void resetGame() {
        leftM = totalM;
        leftC = totalC;
        rightM = 0;
        rightC = 0;
        boatLeft = true;
        crosses = 0;
        lastError = "";
    }

    private boolean executeMove(int m, int c) {
        int origLeftM = leftM;
        int origLeftC = leftC;
        int origRightM = rightM;
        int origRightC = rightC;
        boolean origBoat = boatLeft;

        if (boatLeft) {
            leftM -= m;
            leftC -= c;
            rightM += m;
            rightC += c;
        } else {
            leftM += m;
            leftC += c;
            rightM -= m;
            rightC -= c;
        }
        boatLeft = !boatLeft;

        if (leftM < 0 || leftC < 0 || rightM < 0 || rightC < 0) {
            lastError = "Cannot have negative people";
            rollback(origLeftM, origLeftC, origRightM, origRightC, origBoat);
            return false;
        }

        if ((leftM > 0 && leftC > leftM) || (rightM > 0 && rightC > rightM)) {
            lastError = "Missionaries in danger!";
            rollback(origLeftM, origLeftC, origRightM, origRightC, origBoat);
            return false;
        }

        return true;
    }

    private void rollback(int lm, int lc, int rm, int rc, boolean boat) {
        leftM = lm;
        leftC = lc;
        rightM = rm;
        rightC = rc;
        boatLeft = boat;
    }

    private boolean validateMove(int m, int c) {
        lastError = "";
        
        if (m < 0 || c < 0) {
            lastError = "Negative values not allowed";
            return false;
        }
        
        int total = m + c;
        if (total < 1 || total > 2) {
            lastError = "Boat must carry 1-2 people";
            return false;
        }
        
        if (boatLeft) {
            if (m > leftM || c > leftC) {
                lastError = "Not enough people on left Side";
                return false;
            }
        } else {
            if (m > rightM || c > rightC) {
                lastError = "Not enough people on right Side";
                return false;
            }
        }
        
        return true;
    }

    private void displayState() {
        System.out.println("\nCurrent State (" + crosses + " crosses)");
        System.out.println("--------------------------------");
        System.out.println(" Left Side:  M=" + leftM + " C=" + leftC);
        System.out.println(" Right Side: M=" + rightM + " C=" + rightC);
        System.out.println(" Boat Position: " + (boatLeft ? "LEFT" : "RIGHT"));
    }

    private int[] getPlayerMove(Scanner scanner) {
        System.out.println("\nAvailable moves (format: M C):");
        if (boatLeft) {
            System.out.println("- From LEFT to RIGHT");
        } else {
            System.out.println("- From RIGHT to LEFT");
        }
        System.out.println("Possible combinations:");
        System.out.println(" 1 0 - Move 1 missionary");
        System.out.println(" 0 1 - Move 1 cannibal");
        System.out.println(" 1 1 - Move 1 of each");
        System.out.println(" 2 0 - Move 2 missionaries");
        System.out.println(" 0 2 - Move 2 cannibals");
        System.out.print("Enter move (M C) or 'q' to quit: ");
        
        if (!scanner.hasNextInt()) {
            if (scanner.next().equalsIgnoreCase("q")) {
                System.out.println("Game ended!");
                System.exit(0);
            }
            lastError = "Invalid input format";
            scanner.nextLine();
            return null;
        }
        
        int m = scanner.nextInt();
        int c = scanner.nextInt();
        scanner.nextLine(); 
        
        return new int[]{m, c};
    }

    private boolean isGameOver() {
        if (rightM == totalM && rightC == totalC) return true;
        
        return (leftM > 0 && leftC > leftM) || (rightM > 0 && rightC > rightM);
    }

    private void handleGameEnd(Scanner scanner) {
        if (rightM == totalM && rightC == totalC) {
            System.out.println("\nCONGRATULATIONS! You won in " + crosses + " moves!");
        } else {
            System.out.println("\nGAME OVER! Missionaries were outnumbered!");
        }
        
        System.out.print("Play again? (y/n): ");
        String choice = scanner.next();
        if (choice.equalsIgnoreCase("y")) {
            resetGame();
        } else {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new MissionariesAndCannibalsGame().play();
    }
}