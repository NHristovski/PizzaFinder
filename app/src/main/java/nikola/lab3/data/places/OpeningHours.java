package nikola.lab3.data.places;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {
    @SerializedName("open_now")
    private boolean openNow;

    public OpeningHours(boolean openNow) {
        this.openNow = openNow;
    }

    public OpeningHours(){}

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
                "openNow=" + openNow +
                '}';
    }
}
