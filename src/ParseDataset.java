import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ParseDataset {
	private File dataset = null;
	private String[] attributes = null;
	private Map<String, List<String>> values = new HashMap<String, List<String>>();
	private int numOfRecords;
	private List<String> classValues = new ArrayList<String>();
	
	public int getNumberOfRecords() {
		if( attributes != null ) {
			this.numOfRecords = values.get(attributes[0]).size();
			return this.numOfRecords;
		}
		this.numOfRecords = 0;
		return this.numOfRecords;
	}
	
	public int getNumberOfClasses() {
		//Gives the possible number of classes
		//Classes = last column
		if( classValues != null )
			return classValues.size();
		return 0;
	}
	
	public List<String> getClassesAsList() {
		if( classValues != null ) {
			return classValues;
		}
		return null;
	}
	
	public int getNumberOfAttributes() {
		if( attributes != null )
			return attributes.length;
		return 0;
	}
	
	public List<String> getAttributesList() {
		List<String> attrList = new ArrayList<String>();
		if( attributes != null ) {
			for(int i=0; i<attributes.length; i++) {
				attrList.add(attributes[i]);
			}
			return attrList;
		}
		return null;
	}
	
	public List<String> getAttributeValuesAsList(String attrName) {
		if( checkAttributeExists(attrName) ) {
			attrName = attrName.toUpperCase();
			return values.get(attrName);
		} else {
			System.out.println("The specified attribute doesn't exist.");
		}
		return null;
	}
	
	private boolean checkAttributeExists(String name) {
		for(int i=0; i<attributes.length; i++)
			if( attributes[i].equalsIgnoreCase(name) )
				return true;
		return false;
	}
	
	public void parseDataset(String fileName) {
		dataset = new File(fileName);
		Scanner t = null;
		try {
			t = new Scanner(dataset);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Get attributes in attributes array
		String attributeCSV = t.nextLine();
		//System.out.println("Attributes: "+attributeCSV);
		attributes = attributeCSV.split(",");
		for(int i=0; i<attributes.length; i++) {
			attributes[i] = attributes[i].toUpperCase();
		}	
		
		//Storage of values in the dataset in lists
		for(int i=0; i<attributes.length; i++) {
			values.put(attributes[i], new ArrayList());
		}
		while( t.hasNextLine() ) {
			String[] temp = t.nextLine().split(",");
			for(int i=0; i<temp.length; i++) {
				values.get(attributes[i]).add(temp[i]);
			}
		}
		/*for(int i=0; i<attributes.length; i++) {
			if(i == attributes.length-1)
				System.out.print("CLASS:");
			System.out.println(attributes[i]+" = "+values.get(attributes[i]));
		}*/
		
		numOfRecords = values.get(attributes[0]).size();
		
		classValues.add( values.get(attributes[attributes.length-1]).get(0) );
		for(int i=0; i<numOfRecords; i++) {
			if( !classValues.contains( values.get(attributes[attributes.length-1]).get(i) ) ) {
				classValues.add( values.get(attributes[attributes.length-1]).get(i) );
			}
		}
		
		System.out.println("Successful parse.");
		//return this;
	}
}
