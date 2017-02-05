
import java.util.Scanner;

import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class ID3Algorithm {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
		//Define a new source of data and
		//Create instances from the source. We work with instances in Weka
		DataSource source = null;
		Instances data = null;
		try {
			source = new DataSource("Datasets/ds1.csv");
			data = source.getDataSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(data+"\n");
		
		/*for(int i=0; i<data.numAttributes(); i++)
			System.out.println(isAttributeNumeric(data, i));
		*/
		//Set options and apply filter
		//Store new instance in newData
		Instances newData = null;
		try {
			if( isAttributeNumeric(data, 0) ) {
				String[] options = weka.core.Utils.splitOptions("-R 1");
				Remove remove = new Remove();
				remove.setOptions(options);
				remove.setInputFormat(data);
				newData = Filter.useFilter(data, remove);
			} else {
				newData = data;
			}
			newData.setClassIndex(newData.numAttributes() - 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(newData+"\n");
		
		//Set options for C4.5 classifier and build classifier
		/*J48 tree = new J48();
		try {
			String[] classifierOptions = weka.core.Utils.splitOptions("-U");
			tree.setOptions(classifierOptions);
			tree.buildClassifier(newData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//System.out.println(tree);
		
		//Set options for ID3 classifier and build classifier
		Id3 id3 = new Id3();
		try {
			String[] classifierOptions = weka.core.Utils.splitOptions("-U");
			id3.setOptions(classifierOptions);
			id3.buildClassifier(newData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-----------------\n"+id3);
		
	}

	private static boolean isAttributeNumeric(Instances data, int i) {
		// TODO Auto-generated method stub
		String temp = data.attributeStats(i).toString();
		Scanner t = new Scanner(temp);
		while(t.hasNext()) {
			if( t.next().equalsIgnoreCase("NUM") )
				return true;
		}
		return false;
	}

}
