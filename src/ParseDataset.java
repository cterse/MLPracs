package attributerelevanceanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ParseDataset {
    private Map<String, List<String>> values = new HashMap<String, List<String>>();    
    private int numOfRecords;
    private String[] attributes;
    private String datasetPath; 
    private boolean hasIdAttribute; //true of the first column is an ID attribute
    
    ParseDataset(String datasetPath) {
        this.datasetPath = datasetPath;
        values = parseDataset();    //also sets attributes[], numOfRecords attribute        
        checkIdAttribute();     //sets hasIdAttribute attribute
    }
    
    public boolean hasIdAttribute() {
        return hasIdAttribute;
    }
    
    public String getIdAttribute() {
        return attributes[0];
    }
    
    private void checkIdAttribute() {
        //check if the first column is an ID attr
        //it must be unique ints
        int uniqueCount = 0;
        Iterator<String> tempIt = values.get(attributes[0]).iterator();
        while( tempIt.hasNext() ) {
            try {
                int tempInt = Integer.parseInt(tempIt.next());
                if( !values.get(attributes[0]).contains(tempInt) ) 
                    uniqueCount++;
            } catch(NumberFormatException e) {
                hasIdAttribute = false;
                return;
            }
        }
        if(uniqueCount == numOfRecords)
            hasIdAttribute = true;
        else hasIdAttribute = false;
    }
    
    public Map<String, List<String>> getParsedDataset() {
        if( values == null ) {
            System.out.println("set mapping first. Returning null.");
        }
        return values;
    }
    
    private Map<String, List<String>> parseDataset() {
        if( this.datasetPath == null ) {
            System.out.println("Enter the dataset path first.\nReturning null.");
            return null;
        }
        File dataset = new File(this.datasetPath);
        Scanner t = null;
        try {
            t = new Scanner(dataset);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //get names of attributes
        String attributeCSV = t.nextLine();
        //System.out.println("Attributes: "+attributeCSV);
        this.attributes = attributeCSV.split(",");
        //the array "attributes" now contain the attributes
        
        //Storage of values in Map
        //First, create mapping in Map by adding attributes as map keys and creating Arraylists as elements.
        for(int i=0; i<attributes.length; i++) {
            values.put(attributes[i], new ArrayList());
        }
        //Store the values if the attributes
        while( t.hasNextLine() ) {
            String[] temp = t.nextLine().split(",");
            for(int i=0; i<temp.length; i++) {
                    values.get(attributes[i]).add(temp[i]);
            }
        }
        
        numOfRecords = values.get(attributes[0]).size();
        
        return values;
    }
    
    public List<String> getAttributesAsList() {
        //Get attributes as an ArrayList
        List<String> attributesList = null;
        if( attributes!=null ) {
            attributesList = new ArrayList<String>();
            for(int i=0; i<attributes.length; i++)
                attributesList.add(attributes[i]);
        }
        return attributesList;
    }
    
    public List<String> getAttributeValuesAsList(String attrName) {
        List<String> attrValues = null;
        if(values != null) {
            if(values.containsKey(attrName)) {
                //AttrName found in map
                attrValues = values.get(attrName);
            } else {
                //AttrName not found in map
                System.out.println("Attribute name not found. Closing program");
                System.exit(0);
            }
        } else {
            //Values map not initialized
            System.out.println("Initialize mapping first. Returning null");
        }
        return attrValues;
    }
    
    public String getDatasetClass() {
        String datasetClass = null;
        if( attributes!=null ) {
            datasetClass = attributes[attributes.length-1];
        }
        return datasetClass;
    }
    
    public List<String> getTypesOfClasses() {
        //get different types of class values
        if( values == null ) {
            System.out.println("Set mapping first. Returning null");
            return null;
        }
        List<String> classValues = new ArrayList<String>();
        classValues.add( values.get(attributes[attributes.length-1]).get(0) );
        for(int i=0; i<numOfRecords; i++) {
                if( !classValues.contains( values.get(attributes[attributes.length-1]).get(i) ) ) {
                        classValues.add( values.get(attributes[attributes.length-1]).get(i) );
                }
        }
        return classValues;
    }
    
    public void printDataset() {
        for(int i=0; i<attributes.length; i++) {
            if(i == attributes.length-1)
                    System.out.print("CLASS:");
            System.out.println(attributes[i]+" = "+values.get(attributes[i]));
        }
    }
    
    public int getRecordsCount() {
        int recordsCount = -1;
        if(values!=null) {
            return values.get(attributes[0]).size();
        }
        return recordsCount;
    }
    
    public double getDatasetEntropy() {
        double entropy = -1;
        if(values!=null) {
            entropy = 0;
            List<String> classesTypes = this.getTypesOfClasses();
            Map<String, Integer> classesCount = new HashMap<String, Integer>();
            for(int i=0; i<classesTypes.size(); i++) {
                classesCount.put(classesTypes.get(i), 0);
            }
            for(int i=0; i<this.getRecordsCount(); i++) {
                String currentClass = values.get(this.getDatasetClass()).get(i);
                int temp = classesCount.get( currentClass );
                classesCount.put(currentClass, ++temp);
            }
            for(int i=0; i<classesTypes.size(); i++) {
                double temp1 = classesCount.get(classesTypes.get(i));
                double temp2 = this.getRecordsCount();
                double temp = temp1/temp2;
                entropy += -(temp)*(Math.log(temp) / Math.log(2));
            }
        }
        return entropy;
    }
    
    public Map<String, Map<String, Integer>> getAttributeValuesClassCount(String attr) {
        //check if the specified attribute is present in dataset
        if(!this.getAttributesAsList().contains(attr)) {
            System.out.println("getAttributeValuesClassCount() : specified attribute not in dataset.");
            System.exit(0);
        }
        
        //initialize the map to store the class count
        Map<String, Map<String, Integer>> attrValuesClassCount = new HashMap<String, Map<String, Integer>>();
        //the above map maps attribute valueTypes to a second map, which maps classValueTypes to count of attrValueType to that classvalueType
        Iterator<String> classTypesIt = this.getTypesOfClasses().iterator();
        Iterator<String> valuesTypesIt = this.getAttributeTypes(attr).iterator();
        Map<String, Integer> tempMap = new HashMap<String, Integer>();
        while(classTypesIt.hasNext()) {
            tempMap.put(classTypesIt.next(), 0);
        }
        while(valuesTypesIt.hasNext()) {   
            attrValuesClassCount.put(valuesTypesIt.next(), new HashMap<String, Integer>(tempMap));
        }
        //System.out.println(attr+" = "+attrValuesClassCount);
        
        //populate classCount map
        List<String> attrValues = this.getAttributeValuesAsList(attr);
        List<String> classValues = this.getAttributeValuesAsList(this.getDatasetClass());
        if(attrValues.size() != classValues.size()) {
            //Just a proof check
            System.out.println("getAttributeValuesClassCount() : attrvalues and classValues size not same.");
            System.exit(0);
        } else {
            for(int i=0; i<attrValues.size(); i++) {
                int prevCount = attrValuesClassCount.get(attrValues.get(i)).get(classValues.get(i));
                attrValuesClassCount.get(attrValues.get(i)).put(classValues.get(i), ++prevCount);
            }
        }
        //System.out.println(attr+" = "+attrValuesClassCount);
        
        return attrValuesClassCount;
    }
    
    public double getAttributeEntropy(String attr) {
        double entropy = 0;
        //check if the specified attribute is present in dataset
        if(!this.getAttributesAsList().contains(attr)) {
            System.out.println("getAttributeEntropy() : specified attribute not in dataset.");
            System.exit(0);
        }
        
        //get the classCount for the variable values
        Map<String, Map<String, Integer>> attrValuesClassCount = this.getAttributeValuesClassCount(attr);
        //System.out.println(attr+" = "+attrValuesClassCount);
        
        //calculate entropy
        // -(n/total_records)*( (class1Count/n)*(logbase2((class1Count/n))) + other terms )
        List<Integer> classCounts = null;
        Iterator<String> mainIt = attrValuesClassCount.keySet().iterator();
        while(mainIt.hasNext()) {
            int n = 0;
            classCounts = new ArrayList<Integer>();
            String attrValue = mainIt.next();
            Iterator<String> secIt = attrValuesClassCount.get(attrValue).keySet().iterator();
            while(secIt.hasNext()) {
                String classValue = secIt.next();
                int classValueCount = attrValuesClassCount.get(attrValue).get(classValue);
                n += classValueCount;
                classCounts.add(classValueCount);
            }
            double sum = 0;
            for(int i=0; i<classCounts.size(); i++) {
                if(classCounts.get(i) == 0)
                    continue;
                double temp = (classCounts.get(i)/(double)n);
                double res = temp * Math.log(temp) / Math.log(2);
                sum += res;
            }
            entropy += -((double)n/this.numOfRecords) * sum;
        }
        
        return entropy;
    }
    
    public double getAttributeInfoGain(String attr) {
        double infoGain = 0;
        //check if the specified attribute is present in dataset
        if(!this.getAttributesAsList().contains(attr)) {
            System.out.println("getAttributeInfoGain() : specified attribute not in dataset.");
            System.exit(0);
        }
        
        infoGain = this.getDatasetEntropy() - this.getAttributeEntropy(attr);
        
        return infoGain;
    }
    
    public List<String> getAttributeTypes(String currentAttribute) {
        //Get the different types of values that the specified attribute can take
        List<String> attrTypes;
        if(values == null) {
            System.out.println("Set mapping. returning null");
            return null;
        }
        if( !values.containsKey(currentAttribute) ) {
            System.out.println("Attribute not found. Returning null.");
            return null;
        } else {
            attrTypes = new ArrayList<String>();
            Iterator<String> valuesIt = values.get(currentAttribute).iterator();
            while( valuesIt.hasNext() ) {
                String temp = valuesIt.next();
                if( !attrTypes.contains(temp) )
                    attrTypes.add(temp);
            }
        }
        return attrTypes;
    }

    public int numberOfValuesOfAttribute(String value, String attr) {
        //check if the specified attribute is present in dataset
        if(!this.getAttributesAsList().contains(attr)) {
            System.out.println("getAttributeEntropy() : specified attribute not in dataset.");
            System.exit(0);
        }
        
        int count = 0;
        List<String> attrValues = this.getAttributeValuesAsList(attr);
        for(int i=0; i<attrValues.size(); i++)
            if(attrValues.get(i).equals(value))
                count++;
        
        return count;
    }
    
    /*
    public static void main(String[] args) {
            // TODO Auto-generated method stub
            File dataset = new File("Dataset/ds1.csv");
            Scanner t = null;
            try {
                    t = new Scanner(dataset);
            } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }

            //get names of attributes
            String attributeCSV = t.nextLine();
            System.out.println("Attributes: "+attributeCSV);
            String[] attributes = attributeCSV.split(",");
            //the array "attributes" now contain the attributes

            //Storage of the values in the dataset
            Map<String, List<String>> values = new HashMap<String, List<String>>();
            for(int i=0; i<attributes.length; i++) {
                    values.put(attributes[i], new ArrayList());
            }
            while( t.hasNextLine() ) {
                    String[] temp = t.nextLine().split(",");
                    for(int i=0; i<temp.length; i++) {
                            values.get(attributes[i]).add(temp[i]);
                    }
            }
            for(int i=0; i<attributes.length; i++) {
                    if(i == attributes.length-1)
                            System.out.print("CLASS:");
                    System.out.println(attributes[i]+" = "+values.get(attributes[i]));
            }
            int numOfRecords = values.get(attributes[0]).size();
            System.out.println("Number of total records = "+numOfRecords);

            //get different types of class values
            List<String> classValues = new ArrayList<String>();
            classValues.add( values.get(attributes[attributes.length-1]).get(0) );
            for(int i=0; i<numOfRecords; i++) {
                    if( !classValues.contains( values.get(attributes[attributes.length-1]).get(i) ) ) {
                            classValues.add( values.get(attributes[attributes.length-1]).get(i) );
                    }
            }
            System.out.println("Different types of classes = "+classValues+" ("+classValues.size()+")");

            //print records classification
            for(int i=0; i<classValues.size(); i++) {
                    System.out.print("The class value is "+ classValues.get(i) +" for RID = ");
                    for(int j=0; j<values.get(attributes[1]).size(); j++) {
                            if( values.get(attributes[attributes.length-1]).get(j).equalsIgnoreCase( classValues.get(i) ) ) {
                                    System.out.print( values.get(attributes[0]).get(j) +" ");
                            }
                    }
                    System.out.println();
            }

    }
    */
}
