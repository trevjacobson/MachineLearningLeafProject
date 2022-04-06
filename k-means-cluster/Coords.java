import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 
public class Coord {
 
    private double value1 = 0;
    private double value2 = 0;
    private int total_clusters = 0;
 
    public Coord(double value1, double value2)
    {
        this.setValue1(value1);
        this.setValue2(value2);
    }       
    public void setValue2(double value2) {
        this.value2 = value2;
    }    
    public double getValue2() {
        return this.value2;
    }    
    public void setCluster(int n) {
        this.total_clusters = n;
    }   
    public int getCluster() {
        return this.total_clusters;
    }    
     public void setValue1(double value1) {
        this.value1 = value1;
    }   
    public double getValue1()  {
        return this.value1;
    }
    public String toString() {
    	return "("+x+","+y+")";
    }
    protected static List createRandomPoints(int min, int max, int number) {
    	List points = new ArrayList(number);
    	for(int i = 0; i &lt; number; i++) {
    		points.add(createRandomPoint(min,max));
    	}
    	return points;
    }
    //Calculates the distance between two points.
    protected static double distance(Point p, Point centroid) {
        return Math.sqrt(Math.pow((centroid.getValue2() - p.getValue2()), 2) + Math.pow((centroid.getValue1() - p.getValue1()), 2));
    }
    //Creates random point
    protected static Point createRandomPoint(int min, int max) {
    	Random r = new Random();
    	double value1 = min + (max - min) * r.nextDouble();
    	double value2 = min + (max - min) * r.nextDouble();
    	return new Point(x,y);
    }

}