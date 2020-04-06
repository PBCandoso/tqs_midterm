package tqs.midterm.entity;

public class City
{
    String city;

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city=city;
    }

    @Override
    public String toString() {
        return this.city;
    }
}
