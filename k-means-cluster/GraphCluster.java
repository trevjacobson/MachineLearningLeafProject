import java.util.ArrayList;
import java.util.List;

public class GraphCluster {

	public List coords;
	public Point centroid;
	public int name;

	//Creates a new GraphCluster
	public GraphCluster(int name) {
		this. name = name;
		this.coords = new ArrayList();
		this.centroid = null;
	}

	public List getCoords() {
		return coords;
	}

	public void addPoint(Point point) {
		coords.add(point);
	}

	public void setCoords(List coords) {
		this.coords = coords;
	}

	public Point getCentroid() {
		return centroid;
	}

	public void setCentroid(Point centroid) {
		this.centroid = centroid;
	}

	public int getId() {
		return name;
	}

	public void clear() {
		coords.clear();
	}

	public void plotGraphCluster() {
		System.out.println("[GraphCluster: " + name+"]");
		System.out.println("[Centroid: " + centroid + "]");
		System.out.println("[coords: \n");
		for(Point p : coords) {
			System.out.println(p);
		}
		System.out.println("]");
	}

}