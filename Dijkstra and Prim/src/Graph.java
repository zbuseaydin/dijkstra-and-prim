import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Graph {
	
    private final int numVertices;
    public HashMap<String, HashMap<String, Integer>> vertices = new HashMap<>();
    private HashMap<String, Integer> distances = new HashMap<>();
    private HashSet<String> known = new HashSet<>();
    private HashMap<String, String> parents = new HashMap<>();

    //time that Mecnun spent on his way to Leyla
    public int time = 0;

    //if Mecnun cannot reach Leyla in the limited time, reached is false
    public boolean reached = true;

    public Graph(int numVertices){
        this.numVertices = numVertices;
    }

    public void addVertex(String vertex){
        vertices.put(vertex, new HashMap<>());
    }

    //in order to find the shortest path and MST, all of the distances of vertices are initially maximum value
    public void initializeDistances(){
        for(String vertex: vertices.keySet())
            distances.put(vertex, Integer.MAX_VALUE);
    }

    public void addEdge(String v1, String v2, int weight){
        vertices.get(v1).put(v2, weight);
    }

    public void addUndirectedEdge(String v1, String v2, int weight){
        if(v1.equals(v2))
            return;

        if(vertices.get(v1).containsKey(v2)){
            if(vertices.get(v1).get(v2) > weight){
                vertices.get(v1).replace(v2, weight);
                vertices.get(v2).replace(v1, weight);
            }
        }else{
            vertices.get(v1).put(v2, weight);
            vertices.get(v2).put(v1, weight);
        }
    }

    //returns the shortest path that was found, as a string
    public String shortestPath(String vertex, String start){

        if(!known.contains(vertex) || vertex.equals(start)){
            return vertex;
        }
        return shortestPath(parents.get(vertex), start) + " " + vertex;
    }

    //finds the shortest path using Dijkstra's algorithm and prints the path on the out file
    public void findTheShortestPath(String startVertex, String endVertex, PrintStream out){
        initializeDistances();

        PriorityQueue<String> processQueue = new PriorityQueue<>(Comparator.comparingInt(a -> distances.get(a)));
        distances.replace(startVertex, 0);
        processQueue.add(startVertex);

        while(!processQueue.isEmpty()){
            String processing = processQueue.poll();
            if(known.contains(processing))
                continue;
            known.add(processing);

            for(String v: vertices.get(processing).keySet()){
                if(known.contains(v) || !distances.containsKey(v))
                    continue;
                if(distances.get(v) == Integer.MAX_VALUE || distances.get(v) > distances.get(processing) + vertices.get(processing).get(v)){
                    distances.replace(v, distances.get(processing) + vertices.get(processing).get(v));
                    parents.put(v, processing);
                }
                processQueue.add(v);
            }
        }
        if(distances.get(endVertex)==Integer.MAX_VALUE)
            this.time = 0;
        else
            this.time = distances.get(endVertex);

        if(!parents.containsKey(endVertex)){
            reached = false;
            out.println(-1);
        }
        else
            out.println(shortestPath(endVertex, startVertex));
    }

    //finds the minimum spanning tree of the graph using Prim's algorithm
    //if there's no spanning tree, prints -2, else prints the sum of the edges in the spanning tree on the out file
    public void findTheMST(String startVertex, PrintStream out){
        initializeDistances();

        int costSum = 0;
        int count = 0;
        PriorityQueue<String> processQueue = new PriorityQueue<>(Comparator.comparingInt(a -> distances.get(a)));

        distances.replace(startVertex, 0);
        processQueue.add(startVertex);

        while(!processQueue.isEmpty() && count<=numVertices){
            String processing = processQueue.poll();
            if(known.contains(processing))
                continue;
            if(vertices.get(processing).isEmpty())
                break;
            count++;
            costSum += (distances.get(processing)*2);

            known.add(processing);

            for(String v: vertices.get(processing).keySet()){
                if(known.contains(v))
                    continue;
                processQueue.remove(v);
                if(distances.get(v) == Integer.MAX_VALUE || distances.get(v) > vertices.get(processing).get(v)){
                    distances.replace(v, vertices.get(processing).get(v));
                    parents.put(v, processing);
                }
                processQueue.add(v);
            }
        }
        if(count==numVertices)
            out.println(costSum);
        else
            out.println(-2);
    }
}
