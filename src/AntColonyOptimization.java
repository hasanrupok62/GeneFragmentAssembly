

import java.util.*;
import java.util.stream.IntStream;

public class AntColonyOptimization {

    //The value of c needs to be set by the nearest neighbour heuristic.
    //However we set it to 1 for simplicity.
    private double c = 1.0;
    private double alpha = 0.1;
    private double beta = 2;
    private double evaporation = 0.1;
    private double Q = 500;
    private double antFactor = 0.8;
    private double q0 = 0.9;
    private double localUpdatingConstant = 0.1;
    private double globalUpdatingConstant = 0.1;

    private int maxIterations = 2;

    private int numberOfCities;
    private int numberOfAnts;
    private double graph[][];
    private double pheromoneArray[][];
    private List<Ant> ants = new ArrayList<>();
    private Random random = new Random();
    private double probabilities[];

    public int completedAnts = 0;
    int it1=0;
    int it2 =0;
    private int currentIndex;

    private int[] bestTourOrder;
    private PairClass[] bestTourOrder1;
    private PairClass[] allBestTour;
    private int[] allBestTourLength;
    private double bestTourLength;
    private int iterationsToConverge;

    public AntColonyOptimization(int noOfCities,int numberOfAnts) {
        //graph = generateRandomMatrix(noOfCities);
        graph = convertMatrix(Main.overlap);
        iterationsToConverge = 0;
        //graph = Arrays.stream(Main.overlap).asDoubleStream().toArray();
        //graph = Main.overlap;
        numberOfCities = graph.length;
        bestTourLength = 0;
        //numberOfAnts = (int) (numberOfCities * antFactor);
        this.numberOfAnts = numberOfAnts;
        //Work of pheromoneArray?
        pheromoneArray = new double[numberOfCities][numberOfCities];
        //Work of probablities array?
        probabilities = new double[numberOfCities];

        for(int i = 0;i<noOfCities;i++){
            for(int j =0 ;j<noOfCities;j++){
                pheromoneArray[i][j] =1.0;
            }
        }
        bestTourOrder1 = new PairClass[noOfCities];
    }

    /**
     *
     */
    public double[][]convertMatrix(int overlap[][]){
        double g[][] = new double[overlap.length][overlap.length];
        IntStream.range(0,overlap.length)
                .forEach(i-> IntStream.range(0,overlap.length)
                        .forEach(j->g[i][j] = overlap[i][j]));
        return g;
    }

    /**
     * Generate initial solution
     */
    public double[][] generateRandomMatrix(int n) {
        double[][] randomMatrix = new double[n][n];
        IntStream.range(0, n)
                .forEach(i -> IntStream.range(0, n)
                        .forEach(j -> randomMatrix[i][j] = Math.abs(random.nextInt(100) + 1)));
        return randomMatrix;
    }

    /**
     * Perform ant optimization
     */
    public PairClass[] startAntOptimization() {
        //Reviewed
        /*
        IntStream.range(1, 2)
                .forEach(i -> {
                    System.out.println("Attempt #" + i);
                    solve();
                });
        */
        solve();
        return bestTourOrder1;
    }

    /**
     * Use this method to run the main logic
     */
    public PairClass[] solve() {
        clearTrails();
        for(int i=0;i<maxIterations;i++){
            setupAnts();
            completedAnts = numberOfAnts;
            while(completedAnts != 0){
               // System.out.println("Completed Ants: "+completedAnts);
                moveAnts();
                //System.out.println("Here2");
                updateBest();
            }
            //System.out.println("Out");
            for(Ant ant :ants){
                //System.out.println(Arrays.toString(ant.trail1));
            }

            //System.out.println("Best Tour Length: "+bestTourLength);
            //System.out.println("Best Tour Order: "+ Arrays.toString(bestTourOrder1));

            HashMap<Integer,Integer> hashMap = new HashMap<>();
            for(int j=0;j<bestTourOrder1.length-1;j++){
                PairClass p = bestTourOrder1[j];
                //System.out.println("Row: "+p.row+" Column: "+p.column);
                if(p !=null)
                    hashMap.put(p.row,p.column);
            }

            globalUpdatingPheromone(hashMap);
            completedAnts = 0;

            //System.out.println("Done");
            //return null;
            //System.out.println("Iteration: "+it);
            //break;
          //  updateTrails();
            //updateBest();
        }
        //return  null;
        /*
        IntStream.range(0, maxIterations)
                .forEach(i -> {
                    moveAnts();
                    updateTrails();
                    updateBest();
                });
        */
        System.out.println("Best tour length: " + (bestTourLength));
        //System.out.println("Best tour order: " + Arrays.toString(bestTourOrder1));
        System.out.println("Best tour order: ");
//        for (int i=0;i<bestTourOrder1.length-1;i++){
  //          PairClass p = bestTourOrder1[i];
    //        System.out.println(p.row+"->"+p.column);
      //  }
        return bestTourOrder1.clone();
    }

    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() {
        //Reviewed
        IntStream.range(0, numberOfAnts)
                .forEach(i -> ants.add(new Ant(numberOfCities,i)));
        for(int i=0;i<numberOfAnts;i++){
            ants.get(i).clear();
            int source = random.nextInt(numberOfCities);
            int destination = random.nextInt(numberOfCities);
           // System.out.println("Source: "+source);
            //System.out.println("Destination: "+destination);
            //System.out.println("Ant:"+i+" Source: "+source+"Dest: "+destination+"Value: "+graph[source][destination]);
            //System.out.println("Size: "+ants.get(i).notVisited.size());
            ants.get(i).visitCity(source,destination,(int)graph[source][destination]);
            //System.out.println("Size: "+ants.get(i).notVisited.size());
            localUpdatePheromone(source,destination);
        }
        /*
        IntStream.range(0, numberOfAnts)
                .forEach(i -> {
                    ants.forEach(ant -> {
                        ant.clear();
                        int source = random.nextInt(numberOfCities);
                        int destination = random.nextInt(numberOfCities);
                        System.out.println("Ant:"+i+" Source: "+source+"Dest: "+destination+"Value: "+graph[source][destination]);
                        ant.visitCity(-1, source,destination,(int)graph[source][destination]);
                    });
                });
               */
        currentIndex = 0;
    }

    /**
     * At each iteration, move ants
     */
    private void moveAnts() {
        //reviewed
        for(Ant ant : ants) {
            if (ant.notVisited.size() > 0) {
                int source = ant.lastDest;
                //System.out.println("Source: " + source);
                int destination = selectNextCity(ant);
                //System.out.println("Destination: " + destination);
                //System.out.println("IT1: "+it1+" IT2: "+it2);
                if (destination != -1) {
                    ant.visitCity(source, destination, (int) graph[source][destination]);
                    ant.currentIndex++;
                    localUpdatePheromone(source,destination);
                }
            }
            else{
                if(!ant.allVisited){
                    ant.allVisited = true;
                    completedAnts--;
                }
            }
        }
        //currentIndex++;
        /*
        IntStream.range(currentIndex, numberOfCities - 1)
                .forEach(i -> {
                    ants.forEach(ant -> ant.visitCity(currentIndex, selectNextCity(ant)));
                    //ants.forEach(ant -> {
                      //      ant.dummy();
                        //    it++;
                    //});
                    currentIndex++;
                });
        */
    }

    /**
     * Local Updating Rules for the pheromone
     */
    public void localUpdatePheromone(int source, int destination){
        pheromoneArray[source][destination] = (1-localUpdatingConstant) * pheromoneArray[source][destination]+localUpdatingConstant*c;
    }

    public void globalUpdatingPheromone(HashMap<Integer,Integer> bestTour){
        //System.out.println("Size of Map: "+bestTour.size());
        //System.out.println(bestTour.toString());
        for(int i =0 ;i<numberOfCities;i++){
            if(bestTour.get(i) != null) {
                int dest = bestTour.get(i);
                for (int j = 0; j < numberOfCities; j++) {
                    double delT = 0;
                    if (j == dest)
                        delT = bestTourLength;
                    pheromoneArray[i][j] = (1 - globalUpdatingConstant) * pheromoneArray[i][j] + globalUpdatingConstant * delT;

                }
            }
        }
    }


    /**
     * Select next city for each ant
     */
    private int selectNextCity(Ant ant) {
        //int t = random.nextInt(numberOfCities - currentIndex);
        //int t = random.nextInt(numberOfCities);
        //System.out.println("City Generated: "+t+" for Ant: "+ant.id);
        double q = random.nextDouble();
        int selectedCity = -1;
        //System.out.println(ant.notVisited.toString());
        //Exploitation
        if (q < q0) {
            //System.out.println("In1");
            //System.out.println("In");
            //selectedCity = exploitMaximumUpdate(ant);
            selectedCity = exploitMaximum(ant);
            //it1++;
            //return selectedCity;
            // OptionalInt cityIndex = IntStream.range(0, numberOfCities)
            //        .filter(i -> i == t && !ant.visited(i))
              //      .findFirst();
            //if (cityIndex.isPresent()) {
            //    return cityIndex.getAsInt();
            //}
        }
        //Exploration
        else{
            //selectedCity = biasedExplorationUpdates(ant);
            selectedCity = biasedExploration(ant);
            //System.out.println("In2");
            //it2++;
            //return selectedCity;
        }
        /*
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r) {
                return i;
            }
        }
        */
        return selectedCity;
        //throw new RuntimeException("There are no other cities");
    }

    public int exploitMaximumUpdate(Ant ant){

        int selectedCity = -1;
        //System.out.println(ant.notVisited.size());
        //System.out.println("In1");
        while (true) {
            selectedCity = random.nextInt(numberOfCities);
            //System.out.println("City1: "+selectedCity);
            //System.out.println(ant.notVisited.toString());
            if(ant.visited(selectedCity) == false)
                break;
            //System.out.println("Hello");
        }
        return selectedCity;
    }

    public int exploitMaximum(Ant ant){
        double maxArg = -1;
        int city = -10;
        for(int i=0;i<numberOfCities;i++) {
            if(!ant.visited(i)) {
                double pheromone = pheromoneArray[ant.lastDest][i];
                double distanceHeuristic = Math.pow(graph[ant.lastDest][i],beta);
                double product = pheromone * distanceHeuristic;
                if(maxArg<product){
                    maxArg = product;
                    city = i;
                }
            }
        }
        return city;
    }

    public int biasedExplorationUpdates(Ant ant) {
        int selectedCity = -1;
        //System.out.println("In2");

        while (true) {
            //System.out.println(ant.notVisited.size());
            selectedCity = random.nextInt(numberOfCities);
            //System.out.println("City2: "+selectedCity);
            //System.out.println(ant.notVisited.toString());
            if(ant.visited(selectedCity) == false)
                break;
            //System.out.println("Hello2");
        }
        return selectedCity;
    }


    public int biasedExploration(Ant ant){
        double total = calculateTotal(ant);
        //System.out.println("Total: "+total);
        //double[] cumulativeProbability = new double[numberOfCities];
        for(int i = 0;i< numberOfCities;i++) {
            if (!ant.visited(i)) {
                if(i-1<0)
                    probabilities[i] = (pheromoneArray[ant.lastDest][i]* Math.pow(graph[ant.lastDest][i],beta))/total;
                else
                    probabilities[i] = probabilities[i-1]+ (pheromoneArray[ant.lastDest][i]* Math.pow(graph[ant.lastDest][i],beta))/total;
            }
            else{
                probabilities[i] = 0.0;
            }
        }
        //for(int i=0;i<numberOfCities;i++)
          //  System.out.print(cumulativeProbability[i]+" ");
        //System.out.println(" ");
        double randomProb = random.nextDouble();

        //System.out.println("Random Prob: "+randomProb);

        int selectedCity = -1;
        double t = 0;
        for(int i=0;i<numberOfCities;i++){
            t+=probabilities[i];
            //System.out.println("T: "+t);
            if(randomProb<=t){
                selectedCity = i;
                break;
            }
        }
        return selectedCity;

    }

    public double calculateTotal(Ant ant){
        double total = 0.0;
        for (int i= 0;i<numberOfCities;i++) {
            if(!ant.visited(i)){
                total+=(pheromoneArray[ant.lastDest][i]* Math.pow(graph[ant.lastDest][i],beta));
            }
        }
        return total;
    }

    /**
     * Calculate the next city picks probabilities
     */
    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l)) {
                //System.out.println("L: "+l);
                pheromone += Math.pow(pheromoneArray[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);
            }
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(pheromoneArray[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    /**
     * Update pheromoneArray that ants used
     */
    private void updateTrails() {
        //System.out.println("I am here");
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                pheromoneArray[i][j] *= evaporation;
            }
        }
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
               // System.out.print(pheromoneArray[i][j]+" ");
            }
            //System.out.println(" ");
        }

        for (Ant a : ants) {
            double contribution = Q / a.trailLength(graph);
            for (int i = 0; i < numberOfCities - 1; i++) {
                pheromoneArray[a.trail[i]][a.trail[i + 1]] += contribution;
            }
            pheromoneArray[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }

    /**
     * Update the best solution
     */
    private void updateBest() {
        if (bestTourOrder1 == null) {
            bestTourOrder1 = ants.get(0).trail1;
            bestTourLength = ants.get(0).presentLength;
            //System.out.println("Update Best");
        }
        for (Ant a : ants) {
            if (a.presentLength > bestTourLength) {
                bestTourLength = a.presentLength;
                bestTourOrder1 = a.trail1.clone();
                //System.out.println("Update Best2");
            }
        }
        /*
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0)
                    .trailLength(graph);
        }
        for (Ant a : ants) {
            if (a.trailLength(graph) < bestTourLength) {
                bestTourLength = a.trailLength(graph);
                bestTourOrder = a.trail.clone();
            }
        }
        */
    }

    /**
     * Clear pheromoneArray after simulation
     */
    private void clearTrails() {
        IntStream.range(0, numberOfCities)
                .forEach(i -> {
                    IntStream.range(0, numberOfCities)
                            .forEach(j -> pheromoneArray[i][j] = c);
                });
    }
}