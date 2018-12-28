package nikola.lab3.data.travel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DistanceResult {

    public static int LIMIT_IN_SECONDS = 600;

    @SerializedName("status")
    private String status;

    @SerializedName("rows")
    private List<RowResult> rows;

    public DistanceResult(String status, List<RowResult> rows) {
        this.status = status;
        this.rows = rows;
    }

    public DistanceResult(){
        rows = new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RowResult> getRows() {
        return rows;
    }

    public void setRows(List<RowResult> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "DistanceResult{" +
                "status='" + status + '\'' +
                ", rows=" + rows +
                '}';
    }

    public int getWalkingTime(){
        if (rows.size() > 0){
            return rows.get(0).getWalkingTime();
        }
        return LIMIT_IN_SECONDS + 1;
    }
}
