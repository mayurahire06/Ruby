import java.util.*;

class Node {
    String name;
    int heuristic;
    Node parent;

    Node(String name, int heuristic, Node parent) {
        this.name = name;
        this.heuristic = heuristic;
        this.parent = parent;
    }
}

public class SimpleBestFirstSearch {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> heuristics = new HashMap<>();
        Map<String, List<String>> graph = new HashMap<>();

        // Input nodes
        System.out.print("Number of nodes: ");
        int count = scanner.nextInt();
        scanner.nextLine();
        
        for (int i = 0; i < count; i++) {
            System.out.print("Node name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Heuristic for " + name + ": ");
            heuristics.put(name, scanner.nextInt());
            scanner.nextLine();
            graph.put(name, new ArrayList<>());
        }

        // Input connections
        for (String node : graph.keySet()) {
            System.out.print("Neighbors for " + node + ": ");
            String[] neighbors = scanner.nextLine().trim().split("\\s*,\\s*");
            graph.get(node).addAll(Arrays.asList(neighbors));
        }

        // Get start and goal
        System.out.print("Start node: ");
        String start = scanner.nextLine().trim();
        System.out.print("Goal node: ");
        String goal = scanner.nextLine().trim();

        // Find path
        List<String> path = findPath(graph, heuristics, start, goal);
        System.out.println(path != null ? "Path: " + String.join(" -> ", path) : "No path found");
    }

    private static List<String> findPath(Map<String, List<String>> graph, 
                                       Map<String, Integer> heuristics, 
                                       String start, String goal) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.heuristic));
        Set<String> visited = new HashSet<>();
        queue.add(new Node(start, heuristics.get(start), null));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.name.equals(goal)) return buildPath(current);
            if (visited.add(current.name)) {
                for (String neighbor : graph.get(current.name)) {
                    if (!visited.contains(neighbor)) {
                        queue.add(new Node(neighbor, heuristics.get(neighbor), current));
                    }
                }
            }
        }
        return null;
    }

    private static List<String> buildPath(Node node) {
        LinkedList<String> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node.name);
            node = node.parent;
        }
        return path;
    }
}