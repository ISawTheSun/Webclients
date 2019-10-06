package com.company;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {

    Scene scene = null;

    public static void main(String[] args) {
        Service s = new Service("Poland");
        String weatherJson = s.getWeather("Warsaw");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();
        // ...
        // część uruchamiająca GUI

        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        window.setTitle("Webclients");
        window.setResizable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setVgap(10);

        Label countryLabel = new Label("Country: ");
        GridPane.setConstraints(countryLabel, 0,0);

        TextField country = new TextField();
        GridPane.setConstraints(country, 1,0);

        Label cityLabel = new Label("City: ");
        GridPane.setConstraints(cityLabel, 0,1);

        TextField city = new TextField();
        GridPane.setConstraints(city, 1,1);

        Label currencyLabel = new Label("Currency: ");
        GridPane.setConstraints(currencyLabel, 0,2);

        TextField currency = new TextField();
        GridPane.setConstraints(currency, 1,2);

        Button showInfo = new Button();
        showInfo.setText("Show info");
        showInfo.setOnAction( e -> {
          showInfo(country.getText(), city.getText(), currency.getText(), window, scene);
            country.setText("");
            city.setText("");
            currency.setText("");
        });

        Button cancel = new Button();
        cancel.setText("cancel");
        cancel.setOnAction( e ->{
            country.setText("");
            city.setText("");
            currency.setText("");
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(showInfo, cancel);

        GridPane.setConstraints(buttons, 1, 3);

        grid.getChildren().addAll(countryLabel, country, cityLabel, city, currencyLabel, currency, buttons);


        scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.show();
    }

    public static void showInfo(String country, String city, String kod_waluty, Stage stage, Scene mainScene){

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10));

        HBox hBox = new HBox();
        hBox.setSpacing(20);

        Service service = new Service(country);
        Label info = new Label(city+", "+country);

        Button back = new Button("Back to form");
        back.setOnAction(e->{
            stage.setScene(mainScene);
            stage.show();
        });

        hBox.getChildren().addAll(info, back);

        String Weather = service.getWeather(city);
        TextField weather = new TextField(Weather);
        weather.setEditable(false);

        Double rateFor = service.getRateFor(kod_waluty);
        Label rate1 = new Label("Rate for "+service.getCurrency()+" to "+ kod_waluty + ": "+rateFor);

        Double NBPRate = service.getNBPRate();
        Label rate2 = new Label("PLN to "+ service.getCurrency()+": "+NBPRate);

        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.load("https://en.wikipedia.org/wiki/"+city);

        vBox.getChildren().addAll(hBox, weather, rate1, rate2, webView);

        Scene infoScene = new Scene(vBox, 1150, 600);

        stage.setScene(infoScene);
        stage.show();
    }

}