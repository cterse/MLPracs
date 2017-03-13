import java.util.*;
import java.text.*;

class LinearRegression {
	
	static int getSum(int[] a) {
		int sum = 0;
		for(int i=0;i<a.length;i++)
			sum += a[i];
		return sum;
	}

	static double getResponse(int query,int[] ds1,int[] ds2) {
		double ds1_bar = getSum(ds1) / (double)ds1.length;
		double ds2_bar = getSum(ds2) / (double)ds1.length;
		/*System.out.println("\nx_bar= "+ds1_bar+"  y_bar= "+ds2_bar+"\n");*/
		
		double[] ds1Diff = new double[ds1.length];
		double[] ds2Diff = new double[ds1.length];
		for(int i=0; i<ds1.length; i++) {
			ds1Diff[i] = ds1[i] - ds1_bar;
			ds2Diff[i] = ds2[i] - ds2_bar; 
		}
		
		double beta = 0;
		double num = 0; double denom = 0;
		for(int i=0; i<ds1.length; i++) {
			num += ds1Diff[i] * ds2Diff[i];
			denom += ds1Diff[i] * ds1Diff[i]; 
		}
		beta = num / denom;
		double alpha = ds2_bar - beta * ds1_bar;
		/*
		System.out.println("beta = "+beta);
		System.out.println("aplha = "+alpha);
		*/
		System.out.println("\nEquation of line of regression: y = "+new DecimalFormat(".###").format(alpha)+" + "+new DecimalFormat(".###").format(beta)+"x");
		double response = alpha + beta * query;
		return response;	
	}	
	
	public static void main(String[] args) {
		Scanner t = new Scanner(System.in);
		System.out.print("Size of data set: ");
		int dsSize = t.nextInt();
		
		int[] ds1 = new int[dsSize];
		int[] ds2 = new int[dsSize];
		System.out.println("Enter the data set in [Experience(in years) Salary(in Rs.K)] format:");
		for(int i=0; i<dsSize; i++) {
			ds1[i] = t.nextInt();
			ds2[i] = t.nextInt();
		}
		
		//for(int z:ds1)
		//	System.out.println(z);
		
		System.out.print("Enter Experience (in years): ");
		int query = t.nextInt();
		System.out.println("\nThe corresponding salary = Rs. "+new DecimalFormat(".###").format(getResponse(query,ds1,ds2))+"k.");
		
	}
}
