import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {

    public static int [][]overlap;
    public static double [][]prob;
    //public static PairClass[] allBestTour;
    public static ArrayList<int[]> allBestTour;
    public static ArrayList<PairClass[]> allBestTour1;
    public static int geneOrderList[][];
    public static int noOfTours;

    public static void readPairData(String filename)
    {
        int n=0;
        try{
            BufferedReader buff = new BufferedReader(new FileReader(filename));
            buff.readLine();
            buff.readLine();

            while(true)
            {
                String line = buff.readLine();
                if(line == null)break;
                n++;
            }
            //System.out.println(n);
            overlap=new int[n][n];

            buff = new BufferedReader(new FileReader(filename));
            buff.readLine();
            buff.readLine();

            int i=0;
            while(true)
            {
                int j=0;
                String line = buff.readLine();
                if(line == null)break;
                //System.out.println(line);
                for (String x :line.split(","))
                {
                    overlap[i][j]=Integer.parseInt(x);
                    overlap[j][i]=overlap[i][j];
                    j++;
                }
                i++;

            }

            for (int [] x: overlap) {
                //System.out.println(Arrays.toString(x));
            }
        }catch (Exception e)

        {
            System.out.println(e);
        }
    }

    public static void readGeneOrder(String filename){
        int n=0;
        try{
            BufferedReader buff = new BufferedReader(new FileReader(filename));
            buff.readLine();
            buff.readLine();

            while(true)
            {
                String line = buff.readLine();
                if(line == null)break;
                n++;
            }
            //System.out.println(n);
            geneOrderList=new int[n][n];

            buff = new BufferedReader(new FileReader(filename));

            int i=0;
            while(true)
            {
                int j=0;
                String line = buff.readLine();
                if(line == null)break;
                //System.out.println(line);
                //System.out.println(line);
                line.replaceAll("\\[","");
                System.out.println(line.length());
                System.out.println(line);

                int[] gene = new int[overlap.length];
                for (String x :line.split(","))
                {
                    System.out.print(x);
                    //overlap[i][j]=Integer.parseInt(x);
                    //overlap[j][i]=overlap[i][j];
                    //gene[j] = Integer.parseInt(x);
                    //j++;
                }
                System.out.println("");
                i++;

            }
        }catch (Exception e){
            System.out.println(e);
        }
        //System.out.println(allBestTour.size());
        for (int[] arr : allBestTour){
            //System.out.println(Arrays.toString(arr));
        }
    }

    public static void main(String[] args) {
        readPairData("../genfrag/x60189_6/matrix_raw.csv");
        noOfTours = 10;
        //Solution s1 = new Solution(12);
        //Solution s2 = new Solution(s1);

        //s2.getGene()[2]=10;

        //Solution newS = new Solution(overlap[0].length);
        //System.out.println(newS.calcFitness());
        //System.out.println(newS)
        //GeneticAlgorithm();

        //Ant Colony Optimization
        allBestTour = new ArrayList<>();
        allBestTour1 = new ArrayList<>();

        /*
        int[] gene1 = {28, 11, 34, 23, 2, 31, 37, 4, 17, 22, 26, 6, 14, 27, 13, 15, 16, 18, 12, 5, 25, 3, 10, 0, 33, 9, 7, 1, 21, 30, 36, 19, 24, 38, 29, 32, 8, 35, 8};
        int[] gene2 = {15, 16, 18, 2, 23, 34, 11, 28, 29, 38, 19, 24, 36, 30, 9, 33, 0, 3, 10, 25, 12, 5, 31, 37, 4, 17, 22, 26, 6, 14, 20, 8, 35, 1, 21, 7, 13, 27, 27};
        int [] gene3 = {20, 14, 6, 26, 22, 17, 4, 31, 37, 2, 18, 16, 15, 13, 27, 5, 12, 10, 3, 0, 33, 9, 23, 34, 28, 29, 38, 19, 24, 36, 30, 21, 1, 7, 8, 35, 32, 25, 35};
        int [] gene4 = {21, 30, 1, 7, 8, 35, 32, 9, 33, 0, 3, 10, 28, 11, 34, 23, 2, 31, 37, 4, 17, 22, 26, 6, 14, 20, 27, 13, 15, 16, 18, 12, 5, 36, 19, 24, 38, 29, 37};
        int [] gene5 = {25, 12, 5, 18, 16, 15, 13, 27, 14, 6, 20, 26, 22, 17, 4, 31, 37, 2, 23, 34, 11, 28, 29, 38, 19, 24, 36, 30, 21, 1, 7, 35, 32, 9, 33, 0, 3, 10, 26};
        int [] gene6 = {15, 16, 18, 2, 23, 34, 11, 29, 38, 19, 24, 36, 30, 21, 1, 7, 8, 35, 32, 9, 33, 0, 3, 10, 25, 12, 5, 31, 37, 4, 17, 22, 26, 6, 14, 20, 27, 13, 33};
        int [] gene7 = {0, 25, 12, 5, 31, 37, 4, 17, 22, 26, 6, 14, 20, 8, 35, 19, 24, 29, 38, 36, 30, 21, 1, 7, 32, 9, 33, 3, 10, 28, 11, 34, 23, 2, 18, 16, 15, 13, 27};
        int [] gene8 = {20, 14, 6, 26, 17, 4, 31, 37, 2, 23, 34, 11, 28, 29, 38, 19, 24, 36, 30, 21, 1, 7, 8, 35, 32, 9, 33, 0, 3, 10, 25, 12, 5, 18, 16, 13, 27, 15, 2};
        int [] gene9 = {24, 19, 30, 21, 1, 8, 35, 32, 7, 36, 38, 29, 11, 34, 23, 2, 31, 37, 4, 17, 22, 26, 6, 20, 27, 13, 15, 16, 18, 12, 5, 25, 3, 10, 0, 33, 9, 28, 12};
        int [] gene10 ={8, 35, 32, 1, 21, 30, 7, 19, 24, 38, 29, 11, 34, 23, 2, 31, 37, 4, 17, 22, 26, 6, 14, 20, 27, 13, 15, 16, 18, 12, 5, 25, 3, 10, 0, 33, 9, 36, 25};
        allBestTour.add(gene1);
        allBestTour.add(gene2);
        allBestTour.add(gene3);
        allBestTour.add(gene4);
        allBestTour.add(gene5);
        allBestTour.add(gene6);
        allBestTour.add(gene7);
        allBestTour.add(gene8);
        allBestTour.add(gene9);
        allBestTour.add(gene10);
        */

        AntColonyOpt();
        LocalSearch localSearch = new LocalSearch(allBestTour);
        localSearch.startLocalSearch();
        //LocalSearch localSearch = new LocalSearch(allBestTour1,1);
        //localSearch.startLocalSearchUpdate();
        //LocalSearch localSearch = new LocalSearch();
        //localSearch.startLocalSearch(noOfTours);
        //for(int[] x :allBestTour){

        //}
        //readGeneOrder("besttour.txt");
        //Graph Algorithm
        //runGraph();
    }



    public static void AntColonyOpt(){
        /*
        for (int i=0;i<noOfTours;i++){
            System.out.println("Attempt #" + i);
            AntColonyOptimization antColonyOptimization = new AntColonyOptimization(overlap.length);
            //PairClass[] bestTour = new PairClass[overlap.length];
            PairClass[] bestTour =  antColonyOptimization.startAntOptimization();
            //System.out.println(Arrays.toString(bestTour));
            int[] geneOrder = new int[overlap.length];
            geneOrder[0] = bestTour[0].row;
            int k = 1;
            for (int j = 0;j<bestTour.length;j++,k++){
                if(k!=overlap.length)
                    geneOrder[k] = bestTour[j].column;
            }
            System.out.println(Arrays.toString(geneOrder));
            allBestTour.add(geneOrder);
        }
        */
        int numberOfAnts =30;
        IntStream.range(0, noOfTours)
                .forEach(i -> {
                    System.out.println("Attempt #" + i);
                    AntColonyOptimization antColonyOptimization = new AntColonyOptimization(overlap.length,numberOfAnts);
                    //PairClass[] bestTour = new PairClass[overlap.length];
                    PairClass[] bestTour =  antColonyOptimization.startAntOptimization();
                    allBestTour1.add(bestTour);
                    //System.out.println(Arrays.toString(bestTour));
                    int[] geneOrder = new int[overlap.length];
                    geneOrder[0] = bestTour[0].row;
                    int k = 1;
                    for (int j = 0;j<bestTour.length;j++,k++){
                        if(k!=overlap.length)
                            geneOrder[k] = bestTour[j].column;
                    }
                    int tempVal = 0;
                    for(int j=1;j<geneOrder.length;j++){
                        tempVal+=overlap[geneOrder[j-1]][geneOrder[j]];
                    }
                    System.out.println("Overlap Value: "+tempVal);

                    System.out.println(Arrays.toString(geneOrder));
                    allBestTour.add(geneOrder);

                    try {
                        //../genfrag/x60189_4/matrix_raw.csv
                        write("besttour_x60189_4.txt", geneOrder);
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                });
            //AntColonyOptimization antColonyOptimization = new AntColonyOptimization(overlap.length);
            //antColonyOptimization.startAntOptimization();
    }

    public static void write (String filename, int[]x) throws IOException {
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename,true));
        /*
        for (int i = 0; i < x.length; i++) {
            // Maybe:
            outputWriter.write(x[i]+"");
            // Or:
            outputWriter.write(Integer.toString(x[i]);
            outputWriter.newLine();
        }
        */
        outputWriter.write(Arrays.toString(x));
        outputWriter.newLine();
        outputWriter.flush();
        outputWriter.close();
    }


    public static void  runGraph(){
        for (int i=0;i<1;i++) {
            GraphAlgo graphAlgo = new GraphAlgo();
            System.out.println("Attempt: "+(i+1));
            graphAlgo.start();
        }
    }

    public static Solution GeneticAlgorithm()
    {
        Solution best=null;
        int POP_SIZE=10;
        int MAX_GEN=10;
        int gen_count=0;
        int num_fragments = overlap.length;
        // initialize population
        ArrayList<Solution> population = new ArrayList<>();
        for (int i = 0; i <POP_SIZE ; i++) {
            population.add(new Solution(num_fragments));
        }

        for (Solution x:population) {
            System.out.println(x.calcFitness());

        }
        while(gen_count++<MAX_GEN)
        {
            // mutation

            //

        }

        return best;
    }
}
