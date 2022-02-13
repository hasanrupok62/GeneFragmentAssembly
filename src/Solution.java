import java.util.Arrays;
import java.util.Hashtable;

public class Solution {
    private int gene[];

    public Solution(int n) {
        gene = new int[n];
        generateRandomly();
    }

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        this.gene = gene;
    }

    public Solution(Solution s) {
        this.gene=Arrays.copyOf(s.getGene(),s.getGene().length);
    }


    @Override
    public String toString() {
        return "Solution{" +
                "gene=" + Arrays.toString(gene) +
                '}';
    }
    public int calcFitness()
    {
        int [][]overlap= Main.overlap;
        int val=0;
        for(int i=0;i<gene.length-1;i++)
        {
            val+=overlap[gene[i]][gene[i+1]];
        }
        return  val;
    }

    public Solution mutateSwap(int i,int j)
    {
        Solution temp = new Solution(this);
        int t = temp.gene[i];
        temp.gene[i]=temp.gene[j];
        temp.gene[j]=t;
        return temp;
    }

    

    public int calcFitnessUpdateOnSwap(int i, int j) // update on swap i,j
    {
        int [][]overlap= Main.overlap;
        int val=0;
        // delete current ones
        // add new ones
        if(i!=0)
        {
            val -= overlap[gene[i - 1]][gene[i]];
            val += overlap[gene[i - 1]][gene[j]];
        }
        if(j!=0)
        {
            val -= overlap[gene[j - 1]][gene[j]];
            val += overlap[gene[j - 1]][gene[i]];

        }
        if(i!=gene.length-1)
        {
            val -= overlap[gene[i]][gene[i + 1]];
            val += overlap[gene[j]][gene[i + 1]];

        }
        if(j!=gene.length-1)
        {
            val -= overlap[gene[j]][gene[j + 1]];
            val += overlap[gene[i]][gene[j + 1]];
        }
        return  val;
    }
    private void generateRandomly()
    {
        Hashtable<Integer,Boolean> map = new Hashtable<Integer, Boolean>();
        int i=0;
        while(true)
        {
            int g=(int)(Math.random()*gene.length);
            if(map.get(g)==null)
            {
                gene[i]=g;
                map.put(g,true);
                i++;
            }

            if(i>=gene.length)
                break;
            /*else
            {
                System.out.println("Duplicate");
            }*/
        }
    }
}
