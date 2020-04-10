package tqs.midterm.entity;

import java.util.Objects;

public class Measurement {

    private String parameter;
    private double value;
    private String unit;
    private String lastUpdated;

    public Measurement() {}

    public Measurement(String parameter, double value, String unit, String lastUpdated) {
        this.parameter = parameter;
        this.value = value;
        this.unit = unit;
        this.lastUpdated = lastUpdated;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return Double.compare(that.value, value) == 0 &&
                Objects.equals(parameter, that.parameter) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter, value, unit, lastUpdated);
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "parameter='" + parameter + '\'' +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
