
public class PairClass {
    int row;
    int column;
    int[] geneOrder;
    int overlap;

    PairClass(int row,int column){
        this.row = row;
        this.column = column;
    }

    PairClass(int[] geneOrder,double overlap){
        this.geneOrder = new int[geneOrder.length];
        this.overlap = (int) overlap;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
}
