package nikola.lab3.data.travel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static nikola.lab3.data.travel.DistanceResult.LIMIT_IN_SECONDS;

public class RowResult {
    @SerializedName("elements")
    List<ElementResult> elements;

    public RowResult(List<ElementResult> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public RowResult(){
        elements = new ArrayList<>();
    }

    public List<ElementResult> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public void setElements(List<ElementResult> elements) {
        this.elements = new ArrayList<>(elements);
    }

    @Override
    public String toString() {
        return "RowResult{" +
                "elements=" + elements +
                '}';
    }

    public int getWalkingTime(){
        if (elements.size() > 0){
            return elements.get(0).getWalkingTime();
        }
        return LIMIT_IN_SECONDS + 1;
    }
}
