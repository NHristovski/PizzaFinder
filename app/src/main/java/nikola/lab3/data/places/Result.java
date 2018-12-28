package nikola.lab3.data.places;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nikola.lab3.data.places.PlaceItem;

public class Result {

    @SerializedName("results")
    private List<PlaceItem> places;

    @SerializedName("status")
    private String status;

    @SerializedName("next_page_token")
    private String nextPageToken;

    @Override
    public String toString() {
        StringBuilder placesResultString = new StringBuilder(status);
        placesResultString.append("\n");
        placesResultString.append("Next page token: ").append(nextPageToken).append("\n");

        for(PlaceItem item : places){
            placesResultString.append(item).append("\n");
        }

        return placesResultString.toString();
    }

    public List<PlaceItem> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceItem> places) {
        this.places = places;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
