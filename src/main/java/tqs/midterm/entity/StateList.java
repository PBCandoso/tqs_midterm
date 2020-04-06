package tqs.midterm.entity;

import java.util.List;

public class StateList {

    String status;
    List<State> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<State> getData() {
        return data;
    }

    public void setData(List<State> data) {
        this.data = data;
    }
}
