import java.util.*;

class Node {
    String name;
    int heuristic;
    Node parent;

    public Node(String name, int heuristic, Node parent) {
        this.name = name;
        this.heuristic = heuristic;
        this.parent = parent;
    }
}

public class DynamicBestFirstSearch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> heuristics = new HashMap<>();
        Map<String, List<String>> graph = new HashMap<>();

        // Get graph structure from user
        System.out.print("Enter number of nodes: ");
        int nodeCount = scanner.nextInt();
        scanner.nextLine();  // Clear buffer

        for (int i = 0; i < nodeCount; i++) {
            System.out.print("Enter node name: ");
            String name = scanner.nextLine().trim().toUpperCase();
            
            System.out.print("Enter heuristic value for " + name + ": ");
            int h = scanner.nextInt();
            scanner.nextLine();  // Clear buffer
            
            heuristics.put(name, h);
            graph.put(name, new ArrayList<>());
        }

        // Get connections
        for (String node : graph.keySet()) {
            System.out.print("Enter neighbors for " + node + " (comma-separated): ");
            String[] neighbors = scanner.nextLine().trim().toUpperCase().split(",");
            
            for (String neighbor : neighbors) {
                neighbor = neighbor.trim();
                if (graph.containsKey(neighbor)) {
                    graph.get(node).add(neighbor);
                }
            }
        }

        // Get start and goal
        System.out.print("Enter start node: ");
        String start = scanner.nextLine().trim().toUpperCase();
        
        System.out.print("Enter goal node: ");
        String goal = scanner.nextLine().trim().toUpperCase();

        // Run search
        List<String> path = bestFirstSearch(graph, heuristics, start, goal);
        
        if (path != null) {
            System.out.println("Path found: " + String.join(" -> ", path));
        } else {
            System.out.println("No path exists!");
        }

        scanner.close();
    }

    public static List<String> bestFirstSearch(Map<String, List<String>> graph, 
                                        Map<String, Integer> heuristics, String start, String goal) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.heuristic));
        Set<String> visited = new HashSet<>();
        
        queue.add(new Node(start, heuristics.get(start), null));
        visited.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            if (current.name.equals(goal)) {
                return reconstructPath(current);
            }
            
            for (String neighbor : graph.get(current.name)) {
                if (!visited.contains(neighbor) && heuristics.containsKey(neighbor)) {
                    visited.add(neighbor);
                    queue.add(new Node(neighbor, heuristics.get(neighbor), current));
                }
            }
        }
        return null;
    }

    private static List<String> reconstructPath(Node node) {
        LinkedList<String> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node.name);
            node = node.parent;
        }
        return path;
    }
}