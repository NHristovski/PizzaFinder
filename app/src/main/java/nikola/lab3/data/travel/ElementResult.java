package nikola.lab3.data.travel;

import com.google.gson.annotations.SerializedName;

import static nikola.lab3.data.travel.DistanceResult.LIMIT_IN_SECONDS;

public class ElementResult {
    @SerializedName("status")
    private String status;

    @SerializedName("duration")
    private Duration duration;

    public ElementResult(String status, Duration duration) {
        this.status = status;
        this.duration = duration;
    }

    public ElementResult(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ElementResult{" +
                "status='" + status + '\'' +
                ", duration=" + duration +
                '}';
    }

    public int getWalkingTime() {
        if (duration != null){
            return duration.getValue();
        }
        return LIMIT_IN_SECONDS + 1;
    }
}
