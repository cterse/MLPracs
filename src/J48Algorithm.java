
import weka.core.WekaPackageManager;
import weka.classifiers.trees.*;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class J48Algorithm {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
		//Define a new source of data and
		//Create instances from the source. We work with instances in Weka
		DataSource source = null;
		try {
			source = new DataSource("Datasets/ds1.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Instances data = null;
		try {
			data = source.getDataSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Set options and apply filter
		//Store new instance in newData
		Instances newData = null;
		try {
			String[] options = weka.core.Utils.splitOptions("-R 1");
			Remove remove = new Remove();
			remove.setOptions(options);
			remove.setInputFormat(data);
			newData = Filter.useFilter(data, remove);
			newData.setClassIndex(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Set options for classifier and build classifier
		J48 tree = new J48();
		try {
			String[] classifierOptions = weka.core.Utils.splitOptions("-U");
			tree.setOptions(classifierOptions);
			tree.buildClassifier(newData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(tree);
		
		//Id3 id3 = new Id3();
		
	}

}
