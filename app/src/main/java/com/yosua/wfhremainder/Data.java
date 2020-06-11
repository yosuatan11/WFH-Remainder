package com.yosua.wfhremainder;


public class Data {
    private Integer id;
    private String time;
    private String activity;
    private boolean status;


     public Data(){

     }
     public Data(Integer id,String time, String activity, boolean status){
         this.id = id;
         this.time = time;
         this.activity = activity;
         this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
