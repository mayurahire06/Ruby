import java.util.*;

class State {
    int m, c;
    boolean boat;

    public State(int m, int c, boolean boat) {
        this.m = m;
        this.c = c;
        this.boat = boat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return m == state.m && c == state.c && boat == state.boat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m, c, boat);
    }

    public String toString() {
        return "M:" + m + " C:" + c + " B:" + (boat ? "LEFT" : "RIGHT");
    }
}

public class MissionariesAndCannibals {
    private static int totalM;
    private static int totalC;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter initial missionaries: ");
        totalM = sc.nextInt();
        System.out.print("Enter initial cannibals: ");
        totalC = sc.nextInt();
        sc.close();

        if (totalM < 0 || totalC < 0) {
            System.out.println("Negative numbers not allowed");
            return;
        }
        if (totalM > 0 && totalM < totalC) {
            System.out.println("Cannibals can't outnumber missionaries initially");
            return;
        }

        solve(totalM, totalC);
    }

    public static void solve(int totalM, int totalC) {
        Queue<List<State>> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();
        State start = new State(totalM, totalC, true);
        State goal = new State(0, 0, false);

        // Modified initialization without singletonList()
        List<State> initialPath = new ArrayList<>();
        initialPath.add(start);
        queue.add(initialPath);
        visited.add(start);

        while (!queue.isEmpty()) {
            List<State> path = queue.poll();
            State current = path.get(path.size() - 1);

            if (current.equals(goal)) {
                printSolution(path);
                return;
            }

            for (State next : getNextStates(current)) {
                if (!visited.contains(next)) {
                    List<State> newPath = new ArrayList<>(path);
                    newPath.add(next);
                    visited.add(next);
                    queue.add(newPath);
                }
            }
        }
        System.out.println("No solution exists");
    }

    // Rest of the methods remain unchanged
    private static List<State> getNextStates(State s) {
        List<State> next = new ArrayList<>();
        int[][] moves = {{1,0}, {2,0}, {0,1}, {0,2}, {1,1}};

        for (int[] move : moves) {
            if (s.boat) {
                State newState = new State(s.m - move[0], s.c - move[1], false);
                if (isValid(newState)) next.add(newState);
            } else {
                State newState = new State(s.m + move[0], s.c + move[1], true);
                if (isValid(newState)) next.add(newState);
            }
        }
        return next;
    }

    private static boolean isValid(State s) {
        if (s.m < 0 || s.c < 0 || s.m > totalM || s.c > totalC) return false;
        if (s.m > 0 && s.m < s.c) return false;
        int rightM = totalM - s.m, rightC = totalC - s.c;
        return rightM == 0 || rightM >= rightC;
    }

    private static void printSolution(List<State> path) {
        System.out.println("\nSolution (" + (path.size()-1) + " steps):");
        for (int i = 0; i < path.size(); i++) {
            System.out.println("Step " + i + ": " + path.get(i));
        }
    }
}