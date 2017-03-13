package attributerelevanceanalysis;

import java.util.List;

public class Datapoint {
    private double x, y;
    private int dimension;
    
    Datapoint(double a, double b) {
        x = a; y = b;
        dimension = 2;
    }
    
    Datapoint(double a) {
        x = a; y = 0;
        dimension = 1;
    }
    
    Datapoint() {
        x = 0; y = 0;
        dimension = 0;
    }
    
    public void displayDatapoint() {
        int xi = (int)x;
        int yi = (int)y;
        if(xi == x && yi == y)
            System.out.println("{"+xi+", "+yi+"}");
        else if(xi == x && yi != y)
            System.out.println("{"+xi+", "+y+"}");
        else if(xi != x && yi == y)
            System.out.println("{"+x+", "+yi+"}");
        else
            System.out.println("{"+x+", "+y+"}");
    }
    
    public double getEuclideanDistance(Datapoint p) {
        double distance = 0;
        distance = Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2);
        distance = Math.sqrt(distance);
        return distance;
    }
    
    public static Datapoint getMean(List<Datapoint> dp) {
        double mean_x = 0, mean_y = 0;
        for(int i=0; i<dp.size(); i++) {
            mean_x += (double)dp.get(i).x;
            mean_y += (double)dp.get(i).y;
        }
        mean_x = mean_x / dp.size();
        mean_y = mean_y / dp.size();
        Datapoint mean = new Datapoint(mean_x, mean_y);
        return mean;
    }
    
    public String toString() {
        int xi = (int)x;
        int yi = (int)y;
        if(xi == x && yi == y)
            return "{"+xi+", "+yi+"}";
        else if(xi == x && yi != y)
            return "{"+xi+", "+y+"}";
        else if(xi != x && yi == y)
            return "{"+x+", "+yi+"}";
        else
            return "{"+x+", "+y+"}";
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
}
