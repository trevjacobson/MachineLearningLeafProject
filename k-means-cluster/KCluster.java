import java.util.ArrayList;
import java.util.List;
import Coords
 
public class KMeans {

    private static final int MIN_COORD = 0;
    private static final int MAX_COORD = 3;
	//Number of ClusterGroups. This metric should be related to the number ofCoords
    private int NUM_CLUSTERGROUPS = 36;
    //Number ofCoords
    private int NUM_COORDS = 1500;
    //Min and Max X and Y

    
    private ListCoords;
    private List ClusterGroups;
    
    public KMeans() {
    	this.coords = new ArrayList();
    	this.ClusterGroups = new ArrayList();    	
    }
    
    public static void main(String[] args) {
    	
    	KMeans kmeans = new KMeans();
    	kmeans.init();
    	kmeans.compute();
    }
        private void computeCentroids() {
        for(Cluster cluster : ClusterGroups) {
            double sumX = 0;
            double sumY = 0;
            List list = cluster.getcoords();
            int n_coords = list.size();
            
            for(Point point : list) {
            	sumX += point.getX();
                sumY += point.getY();
            }
            
            Point centroid = cluster.getCentroid();
            if(n_coords &gt; 0) {
            	double newX = sumX / n_coords;
            	double newY = sumY / n_coords;
                centroid.setX(newX);
                centroid.setY(newY);
            }
        }
    }
      private void nameCluster() {
        double max = Double.MAX_VALUE;
        double min = max;
        int cluster = 0;
        double distance = 0.0;

        for(Point point :Coords) {
        	min = max;
            for(int i = 0; i &lt; NUM_ClusterGroups; i++) {
            	Cluster c = ClusterGroups.get(i);
                distance = Point.distance(point, c.getCentroid());
                if(distance &lt; min){
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            ClusterGroups.get(cluster).addPoint(point);
        }
    }


    //Initializes the process
    public void init() {
    	//CreateCoords
    	coords = Point.createRandomcoords(MIN_COORDINATE,MAX_COORDINATE,NUM_coords);
    	
    	//Create ClusterGroups
    	//Set Random Centroids
    	for (int i = 0; i &lt; NUM_ClusterGroups; i++) {
    		Cluster cluster = new Cluster(i);
    		Point centroid = Point.createRandomPoint(MIN_COORDINATE,MAX_COORDINATE);
    		cluster.setCentroid(centroid);
    		ClusterGroups.add(cluster);
    	}
    	
    	//Print Initial state
    	plotClusterGroups();
    }
 
	private void plotClusterGroups() {
    	for (int i = 0; i &lt; NUM_CLUSTERGROUPS; i++) {
    		Cluster c = ClusterGroups.get(i);
    		c.plotCluster();
    	}
    }
    
	//The process to compute the K Means, with iterating method.
    public void compute() {
        boolean finish = false;
        int iteration = 0;
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(!finish) {
        	//Clear cluster state
        	clearClusterGroups();
        	
        	List lastCentroids = getCentroids();
        	
        	//AssignCoords to the closer cluster
        	nameCluster();
            
            //compute new centroids.
        	computeCentroids();
        	
        	iteration++;
        	
        	List currentCentroids = getCentroids();
        	
        	//computes total distance between new and old Centroids
        	double distance = 0;
        	for(int i = 0; i &lt; lastCentroids.size(); i++) {
        		distance += Point.distance(lastCentroids.get(i),currentCentroids.get(i));
        	}
        	System.out.println("#################");
        	System.out.println("Iteration: " + iteration);
        	System.out.println("Centroid distances: " + distance);
        	plotClusterGroups();
        	        	
        	if(distance == 0) {
        		finish = true;
        	}
        }
    }
    
    private void clearClusterGroups() {
    	for(Cluster cluster : ClusterGroups) {
    		cluster.clear();
    	}
    }
    
    private List getCentroids() {
    	List centroids = new ArrayList(NUM_ClusterGroups);
    	for(Cluster cluster : ClusterGroups) {
    		Point aux = cluster.getCentroid();
    		Point point = new Point(aux.getX(),aux.getY());
    		centroids.add(point);
    	}
    	return centroids;
    }
    

}