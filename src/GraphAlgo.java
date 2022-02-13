import java.util.*;
import java.util.stream.IntStream;

public class GraphAlgo {
    private double graph[][];
    private double trails[][];
    private boolean visited[][] = new boolean[Main.overlap.length+1][Main.overlap.length];
    private PairClass parent[][] = new PairClass[Main.overlap.length+1][Main.overlap.length];
    private Queue<PairClass> queue1;
    private ArrayList<PairClass> queue = new ArrayList<>();
    private long seed = 100;
    private Random random = new Random();
    private double fragSum = 0;
    private double randomFactor = 0.8;
    private int rowLength ;
    private int colLength;
    private int allowedMoves = 4;
    private int maxIteration =1000;

    private class PairClass{
        int row;
        int column;
        double selectionProbability;

        PairClass(int row,int column){
            this.row = row;
            this.column = column;
        }
    }

    public void start(){
        graph = convertMatrix(Main.overlap);
        bfs();
    }

    public void bfs(){
        int sourceRow = Math.abs(random.nextInt(rowLength));
        int sourceCol = Math.abs(random.nextInt(colLength));
        System.out.println(sourceRow+" "+sourceCol);
        PairClass p = new PairClass(sourceRow , sourceCol);
        queue.add(p);
        System.out.println(queue.size());
        visited[p.row][p.column] = true;
        parent[p.row][p.column] = new PairClass(-1,-1);

        int lastRow = 0;
        int lastCol = 0;
        int it = 0;

        while(it < maxIteration){
            //System.out.println(queue.size());
            //if(it == maxIteration)
              //  break;

            if(queue.size() > 0) {
                p = queue.get(0);
                queue.remove(0);
            }
            //if(graph[p.row][p.column] == -1)
            //  break;

            //fragSum+=graph[p.row][p.column];

            lastRow = p.row;
            lastCol = p.column;

            for(int i =0;i<allowedMoves;i++){
                sourceRow = Math.abs(random.nextInt(rowLength-1));
                sourceCol = Math.abs(random.nextInt(colLength-1));
                if(!visited[sourceRow][sourceCol]) {
                    parent[sourceRow][sourceCol] = new PairClass(p.row, p.column);
                    PairClass n = new PairClass(sourceRow, sourceCol);
                    queue.add(n);
                    visited[n.row][n.column] = true;
                }
            }
            it++;
        }
        System.out.println("Sum : "+fragSum);

        while(lastRow != -1 && lastCol != -1){
            System.out.println("Value: "+graph[lastRow][lastCol]+" Row: "+lastRow + "Column: "+lastCol);
            PairClass pa = parent[lastRow][lastCol];
            lastRow = pa.row;
            lastCol = pa.column;
        }
    }

    public double[][]convertMatrix(int overlap[][]){
        double g[][] = new double[overlap.length+1][overlap.length];

        rowLength = overlap.length+1;
        colLength = overlap.length;
        IntStream.range(0,overlap.length)
                .forEach(i-> IntStream.range(0,overlap.length)
                        .forEach(j->g[i][j] = overlap[i][j]));


        IntStream.range(overlap.length,overlap.length+1)
                .forEach(i->IntStream.range(0,overlap.length)
                    .forEach(j->g[i][j]=-1)
                );

        return g;
    }
}
