
package attributerelevanceanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AgglomerativeClustering {
    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        System.out.print("Enter the size of dataset: ");
        int n = t.nextInt();
        System.out.println("Enter the datapoints: ");
        List<Datapoint> dataset = new ArrayList<Datapoint>();
        for(int i=0; i<n; i++) {
            dataset.add(new Datapoint(t.nextDouble(), t.nextDouble()));
        }
        
        Map<Integer, List<Datapoint>> clusters = new HashMap<Integer, List<Datapoint>>();
        for(int iteration=0; iteration<n-1; iteration++) {
            System.out.println("Iteration number = "+(iteration+1));
            Map<List<Datapoint>, List<Double>> clusterDistances = new HashMap<List<Datapoint>, List<Double>>();

            //let individual dps be the initial clusters
            if(iteration == 0) {
                for(int i=0 ;i<dataset.size(); i++) {
                    clusters.put(i, new ArrayList<Datapoint>());
                    clusters.get(i).add(dataset.get(i));
                }
            }

            //Calculate distance between the clusters
            clusterDistances = calculateClusterDistances(clusters);
            //System.out.println(clusterDistances);

            Set<Integer> clusterKeys = clusters.keySet();
            Iterator<Integer> clusterKeysIt = clusterKeys.iterator();
            
            //Get the minimum distance and group the clusters togethers
            double minDistance = Double.MAX_VALUE;
            List<Datapoint> fromCluster = null, toCluster = null;
            while(clusterKeysIt.hasNext()) {
                int temp1 = clusterKeysIt.next();
                for(int j=0; j<clusterDistances.get(clusters.get(temp1)).size(); j++) {
                    double temp = clusterDistances.get(clusters.get(temp1)).get(j);
                    if(temp != 0 && temp < minDistance) {
                        minDistance = temp;
                        fromCluster = clusters.get(temp1);
                        Iterator<Integer> temp2It = clusters.keySet().iterator();
                        while(temp2It.hasNext()) {
                            int temp2 = temp2It.next();
                            if(singleLinkBetweenClusters(clusters.get(temp1), clusters.get(temp2)) == minDistance)
                                toCluster = clusters.get(temp2);
                        }
                    }
                }
            }
            System.out.println("Min distance = "+minDistance);
            System.out.println("Selected to form clusters: "+fromCluster+" and "+toCluster);
            
            //Cluster the above closest clusters together
            int fromClusterNumber = getClusterNumber(fromCluster, clusters);
            int toClusterNumber = getClusterNumber(toCluster, clusters);
            //System.out.println("FromClusterNo = "+(fromClusterNumber+1)+" || toClusterNo = "+(toClusterNumber+1));
            clusters.get(fromClusterNumber).addAll(clusters.get(toClusterNumber));
            clusters.remove(toClusterNumber);
            System.out.println("New clusters = "+clusters);
            System.out.println("----------------------------");
        } 
    }
    
    static int getClusterNumber(List<Datapoint> toCluster, Map<Integer, List<Datapoint>> clusters) {
        int number = -1;
        /*for(int i=0; i<clusters.size(); i++) {
            if(clusters.get(i).equals(toCluster)) {
                number = i;
                break;
            } 
        }*/
        Set<Integer> clusterKeys = clusters.keySet();
        Iterator<Integer> clusterKeysIt = clusterKeys.iterator();
        while(clusterKeysIt.hasNext()) {
            int i = clusterKeysIt.next();
            if(clusters.get(i).equals(toCluster)) {
                number = i;
                break;
            }
        }
        return number;
    }
    
    static Map<List<Datapoint>, List<Double>> calculateClusterDistances(Map<Integer, List<Datapoint>> clusters) {
        Map<List<Datapoint>, List<Double>> clusterDistances = new HashMap<List<Datapoint>, List<Double>>();
        
        //clusters in the 'clusters' map should be the keys of the clusterDistances map
        //let keys of clusterDistances map refer to values of clusters map
        /*for(int i=0; i<clusters.size(); i++) {
            clusterDistances.put(clusters.get(i), new ArrayList<Double>());
        }*/
        Set<Integer> clusterKeys = clusters.keySet();
        Iterator<Integer> clusterKeysIt = clusterKeys.iterator();
        while(clusterKeysIt.hasNext()) {
            clusterDistances.put(clusters.get(clusterKeysIt.next()), new ArrayList<Double>());
        }
        
        //Calculate distance of each cluster from another
        //Store the distances as appropriate maaping in clusterDistance map
        /*for(int i=0; i<clusters.size(); i++) {
            for(int j=0; j<clusters.size(); j++) {
                clusterDistances.get(clusters.get(i)).add(singleLinkBetweenClusters(clusters.get(i), clusters.get(j)));
            }
        }*/
        clusterKeys = clusters.keySet();
        clusterKeysIt = clusterKeys.iterator();
        while(clusterKeysIt.hasNext()) {
            Iterator<Integer> temp = clusterKeys.iterator();
            int i = clusterKeysIt.next();
            while(temp.hasNext()) {
                int j = temp.next();
                //System.out.println("i = "+i+" j = "+j);
                clusterDistances.get(clusters.get(i)).add(singleLinkBetweenClusters(clusters.get(i), clusters.get(j)));
            }
        }
        //System.out.println("Reached here");
        return clusterDistances;
    }
    
    static double singleLinkBetweenClusters(List<Datapoint> c1, List<Datapoint> c2) {
        if(c1==null) {
            System.out.println("singleLinkBetweenClusters: c1 = null");
            System.exit(0);
        }
        if(c2==null) {
            System.out.println("singleLinkBetweenClusters: c2 = null");
            System.exit(0);
        }
        double minDistance = Double.MAX_VALUE;
        for(int i=0; i<c1.size(); i++) {
            for(int j=0; j<c2.size(); j++) {
                double distance = c1.get(i).getEuclideanDistance(c2.get(j));
                if(distance < minDistance)
                    minDistance = distance;
            }
        }
        return minDistance;
    }
    
    static double completeLinkBetweenClusers(List<Datapoint> c1, List<Datapoint> c2) {
        double maxDistance = Double.MIN_VALUE;
        for(int i=0; i<c1.size(); i++) {
            for(int j=0; j<c2.size(); j++) {
                double distance = c1.get(i).getEuclideanDistance(c2.get(j));
                if(distance > maxDistance)
                    maxDistance = distance;
            }
        }
        return maxDistance;
    }
    
    static double averageLinkBetweenClusers(List<Datapoint> c1, List<Datapoint> c2) {
        double meanDistance = 0;
        int totalPaths = 0;
        for(int i=0; i<c1.size(); i++) {
            for(int j=0; j<c2.size(); j++) {
                meanDistance += c1.get(i).getEuclideanDistance(c2.get(j));
                totalPaths++;
            }
        }
        return meanDistance/totalPaths;
    }
}
