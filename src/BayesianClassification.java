
package attributerelevanceanalysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BayesianClassification {
    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        //String pathToDataset = "Dataset/ds2_carStolen.csv";
        //String pathToDataset = "Dataset/ds1_buysComputer.csv";
        String pathToDataset = "Dataset/ds3_playTennis.csv";
        
        ParseDataset parse = new ParseDataset(pathToDataset);
        System.out.println("INPUT DATASET:");
        parse.printDataset();
        System.out.println();
        //System.out.println(parse.hasIdAttribute());
        
        //store probabilites of classes
        List<String> classTypes = parse.getTypesOfClasses();
        Map<String, Double> classProbabilities = new HashMap<String, Double>();
        for(Iterator<String> it = classTypes.iterator(); it.hasNext(); ) {
            String classType = it.next();
            //System.out.println(classType+" count = "+parse.numberOfValuesOfAttribute(classType, parse.getDatasetClass()));
            classProbabilities.put(classType, parse.numberOfValuesOfAttribute(classType, parse.getDatasetClass())/(double)parse.getRecordsCount() );
        }
        //System.out.println(classProbabilities);
        
        //initialize map to store probabilities of each values of every attribute against each class
        //MAP3 contains this!!!
        Map<String, Map<String, Map<String, Double>>> map3 = new HashMap<String, Map<String, Map<String, Double>>>(); //maps color to red and yellow
        List<String> attributes = parse.getAttributesAsList();
        for(int i=0; i<attributes.size(); i++) {
            if( (parse.hasIdAttribute() && i==0) || attributes.get(i).equalsIgnoreCase(parse.getDatasetClass()) )
                continue;
            List<String> currentAttrValues = parse.getAttributeValuesAsList(attributes.get(i));
            List<String> currentAttrTypes = parse.getAttributeTypes(attributes.get(i));
            
            Map<String, Double> map1 = new HashMap<String, Double>(); //maps yes and no to probs
            Map<String, Map<String, Double>> map2 = new HashMap<String, Map<String, Double>>(); //maps red to yes and no
            
            for(int j=0; j<classTypes.size(); j++) {
                map1.put(classTypes.get(j), 0.0);
            }
            
            for(int j=0; j<currentAttrTypes.size(); j++) {
                map2.put(currentAttrTypes.get(j), new HashMap<String, Double>(map1));
            }
            
            map3.put(attributes.get(i), new HashMap<String, Map<String, Double>>(map2));  
        }
        //System.out.println(map3);
        
        //store probabilities of each values of every attribute against each class in MAP3!!!
        Iterator<String> map3It = map3.keySet().iterator();
        while(map3It.hasNext()) {
            String attr = map3It.next();
            Map<String, Map<String, Integer>> map4 = parse.getAttributeValuesClassCount(attr);
            //System.out.println(map4);
            //System.out.println(map4);
            Iterator<String> map4It = map4.keySet().iterator();
            while(map4It.hasNext()) {
                String attrValue = map4It.next();
                Iterator<String> map1It = map3.get(attr).get(attrValue).keySet().iterator();
                while(map1It.hasNext()) {
                    String classType = map1It.next();
                    double count = map4.get(attrValue).get(classType);
                    map3.get(attr).get(attrValue).put(classType, count/parse.numberOfValuesOfAttribute(classType, parse.getDatasetClass()));
                }
            }
        }
        //System.out.println(map3);
        //MAP3!!! Never forget.
        
        //Take the query and give output
        Map<String, String> query = new HashMap<String, String>();
        for(Iterator<String> it = parse.getAttributesAsList().iterator(); it.hasNext(); ) {
            String attrName = it.next();
            if( (parse.hasIdAttribute() && attrName.equalsIgnoreCase(parse.getIdAttribute())) || attrName.equalsIgnoreCase(parse.getDatasetClass()) )
                continue;
            System.out.print("Enter query for attribute: "+attrName+" from ");
            System.out.print(parse.getAttributeTypes(attrName)+" : ");
            query.put(attrName, t.next());
            //check if given query is proper - remaining
        }
        //System.out.println(query);
        
        Map<String, Double> classTypeProbCount = new HashMap<String, Double>(); //map to store the final prob coun for each class
        for(Iterator<String> it = parse.getTypesOfClasses().iterator(); it.hasNext(); ) {
            classTypeProbCount.put(it.next(), -1.0);
        }
        //System.out.println(classTypeProbCount);
        
        for(Iterator<String> classIt = parse.getTypesOfClasses().iterator(); classIt.hasNext(); ) {
            String currentClass = classIt.next();
            double count = 1;
            for(Iterator<String> queryIt = query.keySet().iterator(); queryIt.hasNext(); ) {
               String currentQueryAttr = queryIt.next();
               count *= map3.get(currentQueryAttr).get(query.get(currentQueryAttr)).get(currentClass);
            }
            classTypeProbCount.put(currentClass, count);
        }
        System.out.println("\nFinal probabilities of classes: "+classTypeProbCount);
        
        //Get the class type with highest probability
        double max = Double.MIN_VALUE; String response = "";
        for(Iterator<String> it = classTypeProbCount.keySet().iterator(); it.hasNext(); ) {
            String temp = it.next();
            if(classTypeProbCount.get(temp) > max) {
                max = classTypeProbCount.get(temp);
                response = temp;
            }
        }
        System.out.println("Response = "+response);
    }
}
