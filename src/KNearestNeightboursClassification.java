
package attributerelevanceanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class KNearestNeightboursClassification {
    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        System.out.print("Enter the size of dataset: ");
        int n = t.nextInt();
        System.out.println("Enter the datapoints and their class: ");
        List<Datapoint> dataset = new ArrayList<Datapoint>();
        Map<Integer, List<Datapoint>> classes = new HashMap<Integer, List<Datapoint>>();
        for(int i=0; i<n; i++) {
            Datapoint temp = new Datapoint(t.nextDouble(), t.nextDouble());
            dataset.add(temp);
            int dpClass = t.nextInt();
            if(classes.containsKey(dpClass)) {
                classes.get(dpClass).add(temp);
            } else {
                classes.put(dpClass, new ArrayList<Datapoint>());
                classes.get(dpClass).add(temp);
            }
        }
        //System.out.println(classes);
        System.out.print("Enter the datapoint to be classified: ");
        Datapoint dpToClassify = new Datapoint(t.nextDouble(), t.nextDouble());
        System.out.print("Enter the value of k: ");
        int k = t.nextInt();
        
        //Compute distance of dpToClassify from every dp in dataset
        Map<Datapoint, Double> distanceFromDatapoint = new HashMap<Datapoint, Double>();
        Iterator<Datapoint> datasetIt = dataset.iterator();
        while(datasetIt.hasNext()) {
            Datapoint temp = datasetIt.next();
            distanceFromDatapoint.put(temp, dpToClassify.getEuclideanDistance(temp));
        }
        //System.out.println(distanceFromDatapoint);
        
        //Find k nearest classes
        Map<Integer, Integer> classCount = new HashMap<Integer, Integer>();
        Iterator<Integer> classIt = classes.keySet().iterator();
        while(classIt.hasNext()) {
            classCount.put(classIt.next(), 0);
        }
        for(int i=0; i<k; i++) {
            Datapoint nextNearestDp = getNearestDatapoint(distanceFromDatapoint);
            //System.out.println("next nearest dp = "+nextNearestDp);
            distanceFromDatapoint.remove(nextNearestDp);
            int nearestClass = getDpClassNumber(nextNearestDp, classes);
            //System.out.println("nearest dp class = "+nearestClass);
            int temp = classCount.get(nearestClass);
            classCount.put(nearestClass, ++temp);
        }
        System.out.println("Nearest class count = "+classCount);
        
        //Pick the class with the highest nearest count
        int maxCount = Integer.MIN_VALUE, dpClass = -1;
        Iterator<Integer> classCountIt = classCount.keySet().iterator();
        while(classCountIt.hasNext()) {
            int temp = classCountIt.next();
            if(classCount.get(temp) > maxCount) {
                maxCount = classCount.get(temp);
                dpClass = temp;
            }
        }
        System.out.println("Class of datapoint "+dpToClassify+" = "+dpClass);
    }
    
    static int getDpClassNumber(Datapoint dp, Map<Integer, List<Datapoint>> classes) {
        int classNo = -1;
        Iterator<Integer> it = classes.keySet().iterator();
        while(it.hasNext()) {
            int temp = it.next();
            if(classes.get(temp).contains(dp)) {
                classNo = temp;
                break;
            }
        }
        return classNo;
    }
    
    static Datapoint getNearestDatapoint(Map<Datapoint, Double> distanceFromDatapoint) {
        Datapoint nearestDp = null;
        double minDistance = Double.MAX_VALUE;
        Iterator<Datapoint> it = distanceFromDatapoint.keySet().iterator();
        while(it.hasNext()) {
            Datapoint temp = it.next();
            if(distanceFromDatapoint.get(temp) < minDistance) {
                minDistance = distanceFromDatapoint.get(temp);
                nearestDp = temp; 
            }
        }
        return nearestDp;
    }
}
