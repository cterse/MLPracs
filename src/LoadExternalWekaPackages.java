import weka.core.WekaPackageManager;
public class LoadExternalWekaPackages {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WekaPackageManager.loadPackages(false);
		J48Algorithm.main(null);
	}

}
