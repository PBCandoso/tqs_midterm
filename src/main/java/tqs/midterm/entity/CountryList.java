package tqs.midterm.entity;

import java.util.List;

public class CountryList {

    List<Country> results;

    public CountryList(){}

    public CountryList(List<Country> countries){
        this.results=countries;
    }

    public List<Country> getResults() {
        return this.results;
    }

    public void setResults(List<Country> results) {
        this.results = results;
    }
}
