import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Jama.Matrix;

public class MultipleRegression {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner t = new Scanner(System.in);
		System.out.print("Enter size of dataset: ");
		int size = t.nextInt();
		System.out.println("Enter the data in the [IA1_marks IA2_marks ESE_marks] format:");
		List<Integer> predictor1 = new ArrayList<Integer>();
		List<Integer> predictor2 = new ArrayList<Integer>();
		List<Integer> response = new ArrayList<Integer>();
		for(int i=0; i<size; i++) {
			predictor1.add(t.nextInt());
			predictor2.add(t.nextInt());
			response.add(t.nextInt());
		}
		
		/*System.out.println(predictor1);
		System.out.println(predictor2);
		System.out.println(response);*/
		
		int sumPred1 = 0, sumPred2 = 0, sumResponse = 0, sumPred1Square = 0, sumPred2Square = 0;
		int sumPred1ResponseProd = 0, sumPred2ResponseProd = 0, sumPred1Pred2Prod = 0;
		
		sumPred1 = sumList(predictor1);
		sumPred2 = sumList(predictor2);
		sumResponse = sumList(response);
		
		sumPred1Square = sumListSquare(predictor1);
		sumPred2Square = sumListSquare(predictor2);
		
		sumPred1ResponseProd = sumProductOfLists(predictor1, response);
		sumPred2ResponseProd = sumProductOfLists(predictor2, response);
		sumPred1Pred2Prod = sumProductOfLists(predictor1, predictor2);
		
		/*
		System.out.println(sumPred1+" "+sumPred2+" "+sumResponse);
		System.out.println(sumPred1Square+" "+sumPred2Square);
		System.out.println(sumPred1ResponseProd+" "+sumPred2ResponseProd+" "+sumPred1Pred2Prod);
		*/
		
		System.out.println("\nThe three equations are: ");
		System.out.println(sumResponse+" = "+size+"a + "+sumPred1+"b + "+sumPred2+"c");
		System.out.println(sumPred1ResponseProd+" = "+sumPred1+"a + "+sumPred1Square+"b + "+sumPred1Pred2Prod+"c");
		System.out.println(sumPred2ResponseProd+" = "+sumPred2+"a + "+sumPred1Pred2Prod+"b + "+sumPred2Square+"c");
		
		double[][] rhsArray = {{size,sumPred1,sumPred2}, 
								{sumPred1, sumPred1Square, sumPred1Pred2Prod}, 
								{sumPred2, sumPred1Pred2Prod, sumPred2Square} };
		double[] lhsArray = {sumResponse, sumPred1ResponseProd, sumPred2ResponseProd};
		
		Matrix rhs = new Matrix(rhsArray);
		Matrix lhs = new Matrix(lhsArray, 3);
		
		Matrix answer = rhs.solve(lhs);
		
		System.out.println("\na = "+answer.get(0, 0));
		System.out.println("b = "+answer.get(1, 0));
		System.out.println("c = "+answer.get(2, 0));
		
		System.out.print("\nThe equation of the line of regression becomes: ");
		System.out.println("y = "+new DecimalFormat(".###").format(answer.get(0, 0))+" + "+new DecimalFormat(".###").format(answer.get(1, 0))+" * x1 + "+new DecimalFormat(".###").format(answer.get(2, 0))+" * x2");
		
		System.out.print("\nEnter the marks in IA1 and IA2: ");
		int marks1 = t.nextInt();
		int marks2 = t.nextInt();
		double eseMarks = answer.get(0, 0) + answer.get(1, 0)*marks1 + answer.get(2, 0)*marks2;
		System.out.print("The marks predicted in ESE are: "); 
		System.out.println(new DecimalFormat(".###").format(eseMarks));
	}

	private static int sumProductOfLists(List<Integer> list1, List<Integer> list2) {
		// TODO Auto-generated method stub
		int sum = 0;
		for(int i=0; i<list1.size(); i++) {
			sum += list1.get(i) * list2.get(i);
		}
		return sum;
	}

	private static int sumListSquare(List<Integer> list) {
		// TODO Auto-generated method stub
		int sum = 0;
		for(int i=0; i<list.size(); i++) {
			sum += list.get(i) * list.get(i); 
		}
		return sum;
	}

	private static int sumList(List<Integer> list) {
		// TODO Auto-generated method stub
		int sum = 0;
		for(int i=0; i<list.size(); i++) {
			sum += list.get(i);
		}
		return sum;
	}

}
