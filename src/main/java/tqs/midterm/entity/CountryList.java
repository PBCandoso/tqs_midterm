package tqs.midterm.entity;

import java.util.List;

public class CountryList {
    String status;
    List<Country> data;

    public CountryList(){

    }

    public CountryList(List<Country> data){
        this.data=data;
    }

    public CountryList(String status, List<Country> data){
        this.status=status;
        this.data=data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Country> getData() {
        return data;
    }

    public void setData(List<Country> data) {
        this.data = data;
    }
}
