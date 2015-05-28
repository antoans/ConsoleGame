package classes;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Vertex implements Comparable<Vertex>
{
	private int x;
	private int y;
    public ArrayList<Edge> adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(int x, int y) { 
		this.x=x;
		this.y=y;
    }
    public int getX() {
    	return this.x;
    }
    public int getY() {
    	return this.y;
    }
    public String toString() { return String.format("%d %d", x , y); }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}

class Edge
{
    public final Vertex target;
    public final double weight = 1;
    public Edge(int x, int y)
    { target = new Vertex(x, y); }
}

public class PathFinder
{
    public static void computePaths(Vertex source , List<Vertex> vertexList)
    {
        source.minDistance = 0;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
      	vertexQueue.add(source);

	while (!vertexQueue.isEmpty()) {
	    Vertex u = vertexQueue.poll();
	    u = vertexList.get( u.getX()*10 + u.getY() );
            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex v = vertexList.get(e.target.getX()*10 + e.target.getY());
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
				    vertexQueue.remove(v);
				    v.minDistance = distanceThroughU ;
				    v.previous = u;
				    vertexQueue.add(v);
				}
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target)
    {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
}
