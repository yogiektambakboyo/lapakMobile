package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Value {
    String name,target,actual;

    public Data_Value(String name, String target, String actual) {
        this.name = name;
        this.target = target;
        this.actual = actual;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    @Override
    public String toString() {
        return "Data_Value{" +
                "name='" + name + '\'' +
                ", target='" + target + '\'' +
                ", actual='" + actual + '\'' +
                '}';
    }
}