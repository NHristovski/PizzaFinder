package nikola.lab3.data.places;

import com.google.gson.annotations.SerializedName;

import nikola.lab3.data.places.Geometry;
import nikola.lab3.data.places.OpeningHours;

public class PlaceItem {
    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("name")
    private String name;

    @SerializedName("opening_hours")
    OpeningHours openingHours;

    public PlaceItem(Geometry geometry, String name, OpeningHours openingHours) {
        this.geometry = geometry;
        this.name = name;
        this.openingHours = openingHours;
    }

    public PlaceItem(){}

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen(){
        return openingHours.isOpenNow();
    }

    public double getLat(){
        return geometry.getLat();
    }

    public double getLng(){
        return geometry.getLng();
    }

    @Override
    public String toString() {
        return "PlaceItem{" +
                "geometry=" + geometry +
                ", name='" + name + '\'' +
                ", openingHours=" + openingHours +
                '}';
    }
}
