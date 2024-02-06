// Implement ant colony algorithm solving travelling a salesman problem

// Algorithm: Ant Colony Optimization (ACO) for Traveling Salesman Problem (TSP)
// Time Complexity: O(numAnts * numCities^2)
// Space Complexity: O(numCities^2 + numAnts * numCities)


package Task5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TSP {

    private final int numAnts;
    private final int numCities;
    private final double[][] distanceMatrix;
    private double[][] pheromoneMatrix;
    private final double alpha;
    private final double beta;
    private final double evaporationRate;
    private final double initialPheromone;

    private List<Integer> bestTour;
    private double bestTourLength;

    // Constructor
    public TSP(int numAnts, int numCities, double[][] distanceMatrix,
               double alpha, double beta, double evaporationRate, double initialPheromone) {
        this.numAnts = numAnts;
        this.numCities = numCities;
        this.distanceMatrix = distanceMatrix;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.initialPheromone = initialPheromone;

        this.pheromoneMatrix = new double[numCities][numCities];
        initializePheromones();

        this.bestTour = new ArrayList<>();
        this.bestTourLength = Double.POSITIVE_INFINITY;
    }

    // Initialize pheromones
    private void initializePheromones() {
        for (int i = 0; i < numCities; i++) {
            Arrays.fill(pheromoneMatrix[i], initialPheromone);
        }
    }

    // Find the best tour using ant colony optimization
    public List<Integer> findBestTour(int maxIterations) {
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            List<List<Integer>> antTours = generateAntTours();
            updatePheromones(antTours);
            updateBestTour(antTours);

            evaporatePheromones();
        }

        return bestTour;
    }

    // Generate ant tours
    private List<List<Integer>> generateAntTours() {
        List<List<Integer>> antTours = new ArrayList<>();
        for (int ant = 0; ant < numAnts; ant++) {
            int startCity = new Random().nextInt(numCities);
            antTours.add(constructTour(startCity));
        }
        return antTours;
    }

    // Construct a tour for a given ant
    private List<Integer> constructTour(int startCity) {
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[numCities];
        int currentCity = startCity;

        tour.add(startCity);
        visited[startCity] = true;

        for (int step = 1; step < numCities; step++) {
            int nextCity = selectNextCity(currentCity, visited);
            tour.add(nextCity);
            visited[nextCity] = true;
            currentCity = nextCity;
        }

        return tour;
    }

    // Select the next city for an ant
    private int selectNextCity(int currentCity, boolean[] visited) {
        List<Integer> candidateCities = new ArrayList<>();

        for (int city = 0; city < numCities; city++) {
            if (!visited[city]) {
                candidateCities.add(city);
            }
        }

        double[] probabilities = calculateProbabilities(currentCity, candidateCities, visited);
        return selectByProbability(probabilities);
    }

    // Calculate probabilities for selecting each candidate city
    private double[] calculateProbabilities(int currentCity, List<Integer> candidateCities, boolean[] visited) {
        double[] probabilities = new double[candidateCities.size()];
        double totalProbability = 0.0;

        for (int i = 0; i < candidateCities.size(); i++) {
            int nextCity = candidateCities.get(i);
            double pheromone = Math.pow(pheromoneMatrix[currentCity][nextCity], alpha);
            double visibility = Math.pow(1.0 / distanceMatrix[currentCity][nextCity], beta);
            probabilities[i] = pheromone * visibility;
            totalProbability += probabilities[i];
        }

        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= totalProbability;
        }

        return probabilities;
    }

    // Select a city based on probabilities
    private int selectByProbability(double[] probabilities) {
        double randomValue = new Random().nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (randomValue <= cumulativeProbability) {
                return i;
            }
        }

        return probabilities.length - 1;
    }

    // Update pheromones based on ant tours
    private void updatePheromones(List<List<Integer>> antTours) {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i != j) {
                    pheromoneMatrix[i][j] *= (1 - evaporationRate);
                }
            }
        }

        for (List<Integer> tour : antTours) {
            double pheromoneIncrement = 1.0 / calculateTourLength(tour);
            for (int i = 0; i < numCities - 1; i++) {
                int cityA = tour.get(i);
                int cityB = tour.get(i + 1);
                pheromoneMatrix[cityA][cityB] += pheromoneIncrement;
                pheromoneMatrix[cityB][cityA] += pheromoneIncrement;
            }
        }
    }

    // Update the best tour based on ant tours
    private void updateBestTour(List<List<Integer>> antTours) {
        for (List<Integer> tour : antTours) {
            double tourLength = calculateTourLength(tour);
            if (tourLength < bestTourLength) {
                bestTour = new ArrayList<>(tour);
                bestTourLength = tourLength;
            }
        }
    }

    // Evaporate pheromones
    private void evaporatePheromones() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i != j) {
                    pheromoneMatrix[i][j] *= (1 - evaporationRate);
                }
            }
        }
    }

    // Calculate the length of a tour
    private double calculateTourLength(List<Integer> tour) {
        double length = 0.0;
        for (int i = 0; i < numCities - 1; i++) {
            int cityA = tour.get(i);
            int cityB = tour.get(i + 1);
            length += distanceMatrix[cityA][cityB];
        }
        return length;
    }

    // Main method for example usage
    public static void main(String[] args) {
        int numCities = 5;
        double[][] distanceMatrix = {
            {0, 2, 3, 4, 5},
            {2, 0, 6, 7, 8},
            {3, 6, 0, 9, 10},
            {4, 7, 9, 0, 11},
            {5, 8, 10, 11, 0}
        };

        TSP tsp = new TSP(10, numCities, distanceMatrix, 1.0, 2.0, 0.5, 0.1);
        List<Integer> bestTour = tsp.findBestTour(1000);

        System.out.println("Best Tour: " + bestTour);
        System.out.println("Best Tour Length: " + tsp.bestTourLength);
    }
}

