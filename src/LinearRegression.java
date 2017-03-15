package attributerelevanceanalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LinearRegression {
    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        System.out.print("Enter the size of dataset: ");
        int n = t.nextInt();
        System.out.println("Enter the datapoints: ");
        List<Datapoint> dataset = new ArrayList<Datapoint>();   //let us store input in Datapoint, where x = input and y = response
        for(int i=0; i<n; i++) {
            dataset.add(new Datapoint(t.nextDouble(), t.nextDouble()));
        }
        
        Datapoint mean = Datapoint.getMean(dataset);
        
        double beta_num = 0, beta_denom = 0;
        for(int i=0; i<dataset.size(); i++) {
            beta_num += (dataset.get(i).getX() - mean.getX()) * (dataset.get(i).getY() - mean.getY());
            beta_denom += Math.pow((dataset.get(i).getX() - mean.getX()), 2);
        }
        double beta = beta_num / beta_denom;
        double alpha = mean.getY() - beta * mean.getX();
        //System.out.println(beta+" "+aplha);
        
        System.out.println("The regression equation is: y = "+alpha+" + "+beta+"*x");
        
        System.out.print("Enter query: ");
        double query = t.nextDouble();       
        double response = alpha + beta * query;
        System.out.println("Response = "+response);
    }
}
