package attributerelevanceanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class NearestNeighbourClustering {
    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        System.out.print("Enter the size of dataset: ");
        int n = t.nextInt();
        System.out.println("Enter the datapoints: ");
        List<Datapoint> dataset = new ArrayList<Datapoint>();
        for(int i=0; i<n; i++) {
            dataset.add(new Datapoint(t.nextDouble(), t.nextDouble()));
        }
        System.out.println("Enter threshold value: ");
        double threshold = t.nextDouble();
        
        //Create distance matrix
        double[][] distance = new double[n][n];
        for(int i=0; i<n; i++) {
            for(int j=i; j<n; j++) {
                distance[i][j] = dataset.get(i).getEuclideanDistance(dataset.get(j));
            }
        }
        //displayDistanceMatrix(distance);
        
        Map<Integer, List<Datapoint>> clusters = new HashMap<Integer, List<Datapoint>>();
        int k = 0;
        clusters.put(k, new ArrayList<Datapoint>());
        clusters.get(k).add(dataset.get(0));
        for(int j=1; j<n; j++) {    //columns
            int nearestDatapoint = -1;
            for(int i=0; i<j; i++) {    //rows
                if(distance[i][j] <= threshold)
                    if(nearestDatapoint == -1)
                        nearestDatapoint = i;
                    else{
                        if(distance[i][j] < distance[nearestDatapoint][j])
                            nearestDatapoint = i;
                    }
            }
            //System.out.println("For point "+(j+1)+" nearest dp = "+nearestDatapoint);
            if(nearestDatapoint == -1) {
                //Create a new cluster
                clusters.put(++k, new ArrayList<Datapoint>());
                clusters.get(k).add(dataset.get(j));
            } else {
                //Add to the cluster containing the datapoint numbered nearestDatapoint
                int nearsetClusterNumber = getClusterNumber(dataset.get(nearestDatapoint), clusters);
                //System.out.println("Obtained cluster no. = "+nearsetClusterNumber);
                clusters.get(nearsetClusterNumber).add(dataset.get(j));
            }
        }
        printClusters(clusters);
    }
    
    static void printClusters(Map<Integer, List<Datapoint>> clusters) {
        Iterator<Integer> it = clusters.keySet().iterator();
        while(it.hasNext()) {
            int temp = it.next();
            System.out.println("Cluster "+(temp+1)+": ");
            Iterator<Datapoint> itDp = clusters.get(temp).iterator();
            while(itDp.hasNext()) {
                itDp.next().displayDatapoint();
            }
        }
    }
    
    static void displayDistanceMatrix(double[][] a) {
        for(int i=0; i<a.length; i++) {
            for(int j=0; j<a[0].length; j++) {
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }
    }

    private static int getClusterNumber(Datapoint p, Map<Integer, List<Datapoint>> clusters) {
        int number = -1;
        Iterator<Integer> it = clusters.keySet().iterator();
        while(it.hasNext()) {
            number = it.next();
            if(clusters.get(number).contains(p)) {
                return number;
            }
        }
        return number;
    }
}