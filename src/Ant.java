import java.util.HashSet;

public class Ant {
    protected int trailSize;
    protected int trail[];
    protected PairClass trail1[];
    protected double presentLength;
    protected boolean visited[];
    protected HashSet<Integer> notVisited = new HashSet<>();
    protected  int lastDest;
    protected int id;
    protected int currentIndex;
    protected boolean allVisited;


    public Ant(int tourSize,int id) {
        this.trailSize = tourSize;
        this.trail = new int[tourSize];
        this.trail1 = new PairClass[tourSize];
        this.visited = new boolean[tourSize];
        for(int i=0;i<tourSize;i++)
            notVisited.add(i);
        this.id = id;
        this.allVisited = false;
        this.presentLength = 0;
        this.currentIndex = 0;
    }

    protected void visitCity(int source,int destination,int overlapValue) {
        //Add the parameter for keep track of the overlap value
        //trail[currentIndex] = source;
        trail1[currentIndex] = new PairClass(source,destination);
        //trail[currentIndex + 1] = overlapValue;
        visited[source] = true;
        visited[destination] = true;
        lastDest = destination;
        notVisited.remove(source);
        notVisited.remove(destination);
        //this.currentIndex = currentIndex+1;
        presentLength+=overlapValue;
    }

    protected boolean visited(int i) {
        return visited[i];
    }

    protected double trailLength(double graph[][]) {
        double length = graph[trail[trailSize - 1]][trail[0]];
        for (int i = 0; i < trailSize - 1; i++) {
            length += graph[trail[i]][trail[i + 1]];
        }
        /*
        double length = 0;
        for (int i = 0; i < trailSize ; i++) {
            length += trail[i];
        }
        */
        return length;
    }

    protected void clear() {
        for (int i = 0; i < trailSize; i++)
            visited[i] = false;
    }
}