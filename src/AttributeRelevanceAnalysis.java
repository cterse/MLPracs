package attributerelevanceanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AttributeRelevanceAnalysis {
    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        //System.out.print("Path to dataset: ");
        String datasetPath = "Dataset/ds1.csv";
        
        ParseDataset parse = new ParseDataset(datasetPath);
        //parse.printDataset();
        
        //Calculate the dataset entropy
        double datasetEntropy = parse.getDatasetEntropy();
        System.out.println("Entropy of dataset = "+datasetEntropy+"\n");
        
        //in 1st iteration, calculate entorpy and hence info gain of each attribute and store in map
        List<String> attributes = parse.getAttributesAsList();
        Map<String, Double> attributeInfogains = new HashMap<String, Double>();
        for(int i=0; i<attributes.size(); i++) {
            if( (i==0 && parse.hasIdAttribute()) || attributes.get(i).equalsIgnoreCase(parse.getDatasetClass()) )
                continue;   //don't calculate entropy of ID attribute if present or class
            double currentAttributeEntropy = parse.getAttributeEntropy(attributes.get(i));
            System.out.println("Entropy of "+attributes.get(i)+" = "+currentAttributeEntropy);
            double currentAttributeinfoGain = parse.getAttributeInfoGain(attributes.get(i));
            System.out.println("Info Gain of "+attributes.get(i)+" = "+currentAttributeinfoGain);
            attributeInfogains.put(attributes.get(i), currentAttributeinfoGain);
            System.out.println();
        }
        //System.out.println(attributeInfogains);
        
        //get the attribute with the highest infogain
        double max = Double.MIN_VALUE;
        String maxInfoGainAttr = "";
        Iterator<String> attributeInfogainsIt = attributeInfogains.keySet().iterator();
        while(attributeInfogainsIt.hasNext()) {
            String temp = attributeInfogainsIt.next();
            if(attributeInfogains.get(temp) > max) {
                max = attributeInfogains.get(temp);
                maxInfoGainAttr = temp;
            }
        }
        System.out.println("Attribute with max info gain = "+maxInfoGainAttr);
    }   
}