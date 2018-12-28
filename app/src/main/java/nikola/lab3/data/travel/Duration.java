package nikola.lab3.data.travel;

import com.google.gson.annotations.SerializedName;

public class Duration {
    @SerializedName("value")
    private int value;

    public Duration(int value) {
        this.value = value;
    }

    public Duration(){}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "value=" + value +
                '}';
    }
}
