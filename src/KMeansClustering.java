package attributerelevanceanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class KMeansClustering {
    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        System.out.print("Enter the size of dataset: ");
        int n = t.nextInt();
        System.out.println("Enter the datapoints: ");
        List<Datapoint> dataset = new ArrayList<Datapoint>();
        for(int i=0; i<n; i++) {
            dataset.add(new Datapoint(t.nextDouble(), t.nextDouble()));
        }
        System.out.println("Enter k value: ");
        int k = t.nextInt();
        
        //Select k random means
        //here, we select the first k datapoints as random means
        Map<Integer, Datapoint> means = new HashMap<Integer, Datapoint>();
        for(int i=0; i<k; i++) {
            means.put(i, dataset.get(i));
        }
        
        //Calculate distance of every datapoint from each mean
        //Save the dp in the cluster with min corresponding mean distance
        Map<Integer, List<Datapoint>> clusters = null;
        
        //1st iteration
        boolean nextIteration = true;
        Map<Integer, List<Datapoint>> prevClusters = null;
        do {
            if(clusters != null) {
                prevClusters = new HashMap<Integer, List<Datapoint>>(clusters);
                //Update means
                means = getMeans(prevClusters);
            }
            clusters = new HashMap<Integer, List<Datapoint>>();
            for(int i=0; i<k; i++)
                clusters.put(i, new ArrayList<Datapoint>());
            for(int i=0; i<dataset.size(); i++) {
                int nearestMean = -1;
                double minDistance = Double.MAX_VALUE;
                Iterator<Integer> meansIt = means.keySet().iterator();
                while(meansIt.hasNext()) {
                    int temp = meansIt.next();
                    double distance = dataset.get(i).getEuclideanDistance(means.get(temp));
                    if(distance < minDistance) {
                        minDistance = distance;
                        nearestMean = temp;
                    }
                }
                clusters.get(nearestMean).add(dataset.get(i));
            }
            if(prevClusters != null && compareClusters(prevClusters, clusters))
                nextIteration = false;
            //System.out.println("Clusters after an iteration:");
            //printClusters(clusters);
        }while(nextIteration);
        printClusters(clusters);
    }
    
    static boolean compareClusters(Map<Integer, List<Datapoint>> prevClusters, Map<Integer, List<Datapoint>> clusters) {
        Set<Integer> prevClusterKeys = prevClusters.keySet();
        Set<Integer> clusterKeys = clusters.keySet();
        if(!clusterKeys.equals(prevClusterKeys))
            return false;
        
        Iterator<Integer> it1 = prevClusterKeys.iterator();
        while(it1.hasNext()) {
            int n1 = it1.next();
            List<Datapoint> l1 = prevClusters.get(n1);
            if(!l1.equals(clusters.get(n1)))
                return false;
        }
        return true;
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

    private static Map<Integer, Datapoint> getMeans(Map<Integer, List<Datapoint>> clusters) {
        Map<Integer, Datapoint> means = new HashMap<Integer, Datapoint>();
        Set<Integer> temp2 = clusters.keySet();
        Iterator<Integer> temp2It = temp2.iterator();
        while(temp2It.hasNext()) {
            int temp4 = temp2It.next();
            Datapoint meanDp = Datapoint.getMean(clusters.get(temp4));
            means.put(temp4, meanDp);
        }
        return means;
    }
    
}
