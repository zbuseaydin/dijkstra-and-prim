import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class project3main {

	public static void main(String[] args) throws IOException {


        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        PrintStream out = new PrintStream(new FileOutputStream(args[1]));

        int timeLim = Integer.parseInt(in.readLine());
        int numCities = Integer.parseInt(in.readLine());

        String[] line1 = in.readLine().split(" ");
        String mecnunID = line1[0];
        String leylaID = line1[1];

        String leyla = "";
        for(int i=1; i<leylaID.length(); i++)
            leyla += leylaID.charAt(i);

        int numPart1 = Integer.parseInt(leyla);

        Graph reaching = new Graph(numPart1);
        Graph honeymoon = new Graph(numCities - numPart1 + 1);

        //creating the directed graph in which Mecnun tries to reach Leyla
        for(int l=1; l<=numPart1; l++)
            reaching.addVertex("c" + l);

        for(int i=0; i<numPart1-1; i++){
            String[] line = in.readLine().strip().split(" ");

            for(int j=1; j<line.length-1; j+=2){
                reaching.addEdge(line[0], line[j], Integer.parseInt(line[j+1]));
            }
        }

        //creating the undirected graph
        honeymoon.addVertex(leylaID);
        for(int k=1; k<=numCities-numPart1; k++)
            honeymoon.addVertex("d" + k);

        for(int m=0; m <= numCities-numPart1; m++){
            String[] ln = in.readLine().strip().split(" ");

            for(int n=1; n<ln.length-1; n+=2){
                String vertexID = ln[n];
                int edgeWeight = Integer.parseInt(ln[n+1]);
                honeymoon.addUndirectedEdge(ln[0], vertexID, edgeWeight);
            }
        }

        reaching.findTheShortestPath(mecnunID, leylaID, out);

        //if the time limit is exceeded, prints -1
        if(reaching.time>timeLim || !reaching.reached)
            out.println(-1);
        else
            honeymoon.findTheMST(leylaID, out);

        in.close();
        out.close();
    

	}

}
