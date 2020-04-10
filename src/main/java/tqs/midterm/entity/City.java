package tqs.midterm.entity;

import java.util.Objects;

public class City
{
    private String country; // country_code
    private String name;

    public City(){}

    public City(String country,String name){
        this.country=country;
        this.name=name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(country, city.country) &&
                Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, name);
    }

    @Override
    public String toString() {
        return "City{" +
                "country='" + country + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
