package tqs.midterm.entity;

import java.util.List;
import java.util.Objects;

public class Location {

    private String location;
    private List<Measurement> measurements;

    public Location() {}

    public Location(String location, List<Measurement> measurements) {
        this.location = location;
        this.measurements = measurements;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location1 = (Location) o;
        return Objects.equals(location, location1.location) &&
                Objects.equals(measurements, location1.measurements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, measurements);
    }

    @Override
    public String toString() {
        return "Location{" +
                "location='" + location + '\'' +
                ", measurements=" + measurements +
                '}';
    }
}
