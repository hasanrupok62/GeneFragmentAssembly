

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class LocalSearch {
    protected ArrayList<int[]> bestTours;
    protected ArrayList<PairClass[]> bestTours1;
    protected int[][] overlap;
    protected int maxIteration;
    protected Random rand;
    protected ArrayList<int[]> updatedTours;
    protected ArrayList<Integer> updatedTours1;

    LocalSearch(){
        this.bestTours = new ArrayList<>();

        int len = Main.overlap.length;
        this.overlap = new int[len][len];
        this.overlap = Main.overlap;

        rand = new Random();

        updatedTours = new ArrayList<>();
    }

    LocalSearch(ArrayList<int[]> bestTours){
        this.bestTours = new ArrayList<>();
        this.bestTours = bestTours;

        int len = Main.overlap.length;
        this.overlap = new int[len][len];
        this.overlap = Main.overlap;

        rand = new Random();

        updatedTours = new ArrayList<>();
    }

    LocalSearch(ArrayList<PairClass[]> bestTours1,int i){
        this.bestTours1 = new ArrayList<>();
        this.bestTours1 = bestTours1;

        int len = Main.overlap.length;
        this.overlap = new int[len][len];
        this.overlap = Main.overlap;

        rand = new Random();

        updatedTours = new ArrayList<>();
    }

    public void startLocalSearch(int numberOfTours){
        generateTours(numberOfTours);
        startLocalSearch();
    }

    public void generateTours(int numberOfTours) {
        int numberOfGenes = overlap.length;

        for(int i = 0 ; i < numberOfTours ;i++){
            int[] gene1 = new int[overlap.length];
            for (int j = 0;j<overlap.length;j++){
                int g1 = rand.nextInt(numberOfGenes);
                gene1[j] = g1;
            }
            bestTours.add(gene1);
        }
    }

    public void startLocalSearch(){
        int i=0;
        maxIteration = 100000;
        int sum =0;
        for(int[] geneOrder : bestTours){
            System.out.println("----------------------------------------------------------");
            System.out.println("Attempt on gene "+i);
            System.out.println(Arrays.toString(geneOrder));
            System.out.println(calculateOverlap(geneOrder));
            geneOrder = swapper(geneOrder);
            System.out.println(Arrays.toString(geneOrder));
            System.out.println(calculateOverlap(geneOrder));
            sum+= calculateOverlap(geneOrder);
            updatedTours.add(geneOrder);
            i++;
        }
        double avg = sum/10;
        System.out.println("Avg: "+avg);
    }

    public void startLocalSearchUpdate(){
        int i=0;
        maxIteration = 100;
        for(PairClass[] geneOrder : bestTours1){
            System.out.println("----------------------------------------------------------");
            System.out.println("Attempt on gene "+i);
            //System.out.println(Arrays.toString(geneOrder));
            //System.out.println(calculateOverlap(geneOrder));
            int overlapValue = swapperUpdate(geneOrder);
            //System.out.println(Arrays.toString(geneOrder));
            //System.out.println(calculateOverlap(geneOrder));
            updatedTours1.add(overlapValue);
            i++;
        }
    }

    private int[] swapper(int[] geneOrder1){
        int overlapValue = calculateOverlap(geneOrder1);
        System.out.println(overlapValue);
        int it = 0;
        int[] newGeneOrder = new int[geneOrder1.length];

        for(int i=0;i<geneOrder1.length;i++){
            newGeneOrder[i] = geneOrder1[i];
        }


        int newOverlapValue = overlapValue;

        while(it++ < maxIteration){
            int gene1, gene2;
            gene1 = gene2 = 1;

            while(gene1 == gene2) {
                gene1 = generateGene(geneOrder1.length);
                gene2 = generateGene(geneOrder1.length);
            }

            //System.out.println("Gene1: "+gene1+" Gene2: "+gene2);
            int temp = newGeneOrder[gene1];
            newGeneOrder[gene1] = newGeneOrder[gene2];
            newGeneOrder[gene2] = temp;
            newOverlapValue = calculateOverlap(newGeneOrder);

            //System.out.println(Arrays.toString(newGeneOrder));
            //System.out.println(Arrays.toString(geneOrder1));
            //System.out.println(newOverlapValue+" "+overlapValue);

            if(newOverlapValue > overlapValue){
                overlapValue = newOverlapValue;

                for(int i=0;i<geneOrder1.length;i++)
                    geneOrder1[i] = newGeneOrder[i];

                //System.out.println("In");
                //System.out.println(calculateOverlap(newGeneOrder));
                //System.out.println(calculateOverlap(geneOrder1));
            }
            else {
                //System.out.println("In2");
                for(int i =0;i<geneOrder1.length;i++)
                    newGeneOrder[i] = geneOrder1[i];
            }
        }
        //System.out.println(Arrays.toString(geneOrder1));
        return geneOrder1;
    }

    private int swapperUpdate(PairClass[] geneOrder1){
        int it =0;
        HashMap<Integer,Integer> hashMap = new HashMap<Integer,Integer>();
        for(PairClass p : geneOrder1)
            hashMap.put(p.row,p.column);

        while(it++ < maxIteration){
            int edge1 = generateGene(geneOrder1.length);
            int edge2  = generateGene(geneOrder1.length);

            int v = hashMap.get(edge1);




        }

        return 0;
    }

    private int calculateOverlap(PairClass[] geneOrder1){
        int value =0;
        for(int i=1;i<geneOrder1.length;i++){
            PairClass p = geneOrder1[i];
            int source = p.row;
            int dest = p.column;
            value+=overlap[source][dest];
        }
        return value;
    }

    private int calculateOverlap(int[] geneOrder1){
        int value =0;
        for(int i=1;i<geneOrder1.length;i++)
            value+=overlap[geneOrder1[i-1]][geneOrder1[i]];
        return value;
    }

    private int generateGene(int numberOfGenes){
        return rand.nextInt(numberOfGenes);
    }
}
