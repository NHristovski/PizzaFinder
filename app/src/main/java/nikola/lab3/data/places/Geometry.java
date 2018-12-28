package nikola.lab3.data.places;

public class Geometry {
    private Location location;

    public Geometry(){}

    public Geometry(Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getLat(){
        return location.getLat();
    }

    public double getLng(){
        return location.getLng();
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "location=" + location +
                '}';
    }
}
