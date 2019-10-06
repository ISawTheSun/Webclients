package com.company;

public class Weather {
    String mainWeather;
    String description;

    String temp;
    String pressure;
    String temp_min;
    String temp_max;

    String speed;


    public Weather(String mainWeather, String description, String temp, String pressure, String temp_min, String temp_max, String speed) {
        this.mainWeather = mainWeather;
        this.description = description;
        this.temp = temp;
        this.pressure = pressure;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return  "weather:\n"+
                " main: " + mainWeather + ", \n" +
                " description: " + description + ";   \n" +
                "main:\n"+
                " temp: " + temp + ", \n" +
                " pressure: " + pressure + ", \n" +
                " temp_min: " + temp_min + ", \n" +
                " temp_max: " + temp_max + ";   \n" +
                "wind:\n"+
                " speed: " + speed+";   ";
    }
}
