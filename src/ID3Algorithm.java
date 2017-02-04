
public class ID3Algorithm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ParseDataset pd = new ParseDataset();
		pd.parseDataset("Datasets/ds1.csv");
		
		System.out.println(pd.getNumberOfClasses());
		System.out.println(pd.getNumberOfAttributes());
		System.out.println(pd.getNumberOfRecords());
		System.out.println(pd.getAttributesList());
		System.out.println(pd.getClassesAsList());
		System.out.println(pd.getAttributeValuesAsList("age"));
	}

}
