package com.example.finalexam;

public class Actions {
    private String action_business;
    private String action_name;
    private int action_package;
    private String action_describe;
    private String time;

    public Actions(String action_business, String action_name, int action_package, String action_describe, String time) {
        this.action_business = action_business;
        this.action_name = action_name;
        this.action_package = action_package;
        this.action_describe = action_describe;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Actions{" +
                "action_business='" + action_business + '\'' +
                ", action_name='" + action_name + '\'' +
                ", action_package=" + action_package +
                ", action_describe='" + action_describe + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getAction_business() {
        return action_business;
    }

    public void setAction_business(String action_business) {
        this.action_business = action_business;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public int getAction_package() {
        return action_package;
    }

    public void setAction_package(int action_package) {
        this.action_package = action_package;
    }

    public String getAction_describe() {
        return action_describe;
    }

    public void setAction_describe(String action_describe) {
        this.action_describe = action_describe;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
