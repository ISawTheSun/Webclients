package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Service {
    String country;
    String currency;
    Map<String, String> currencies= new HashMap<>();
    Map<String, Double> rates = new HashMap<>();

    public Service(String country) {
        this.country = country;
        getCountriesCur();
        currency = currencies.get(country);
    }

    public String getCurrency() {
        return currency;
    }

    public void getCountriesCur(){
        Locale en = new Locale("en");
        Locale [] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            try {
                Currency cur = Currency.getInstance(locales[i]);
                currencies.put(locales[i].getDisplayCountry(en), cur.toString());
            }catch (IllegalArgumentException e){

            }
        }
    }

    public String getWeather(String city) {

        String key = "94064b99abc150b066f8c52f8dcf5b85";
        URL url = null;
        URLConnection connection = null;
        StringBuilder res = new StringBuilder();

        try {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric");
            connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                res.append(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String result = "";

        JSONParser parser = new JSONParser();

        try {


            JSONObject jsonObject = (JSONObject) parser.parse(res.toString());


            JSONArray jsonArray = (JSONArray) jsonObject.get("weather");
            String mainWeather = "";
            String description = "";
            for (Object obj: jsonArray) {
                JSONObject weather = (JSONObject) obj;
                mainWeather = (String) weather.get("main");
                description = (String) weather.get("description");
            }

            JSONObject main = (JSONObject) jsonObject.get("main");
            String temp = String.valueOf(main.get("temp"));
            String pressure = String.valueOf(main.get("pressure"));
            String temp_min = String.valueOf(main.get("temp_min"));
            String temp_max = String.valueOf(main.get("temp_max"));

            JSONObject wind = (JSONObject) jsonObject.get("wind");
            String speed = String.valueOf(wind.get("speed"));


            Weather WEATHER = new Weather(mainWeather, description, temp, pressure, temp_min, temp_max, speed);
            result = WEATHER.toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(result);

        return result;
    }

    public Double getRateFor(String kod_waluty){
        URL url = null;
        try {
            url = new URL("https://api.exchangeratesapi.io/latest?base="+currency+"&symbols="+kod_waluty);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        double rate = 0;

        try {
            String inputLine;
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((inputLine = in.readLine()) != null)
            {
                //System.out.println(inputLine);

                Pattern p = Pattern.compile("(:[\\d.]+)");
                Matcher m = p.matcher(inputLine);

                while (m.find()) {
                    String tmp = m.group(1).replace(":", "");
                    rate = Double.parseDouble(tmp);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(rate);

        return rate;
    }

    public Double getNBPRate(){

        getRates("http://www.nbp.pl/kursy/xml/a058z190322.xml");
        getRates("http://www.nbp.pl/kursy/xml/b012z190320.xml");

//        for (String kod: rates.keySet()) {
//            System.out.println(kod+ " -- " +rates.get(kod));
//        }

        double rate;

        if(currency.equals("PLN"))
            rate = 1;
        else
            rate = rates.get(currency);

        System.out.println(rate);

        return rate;
    }



    public void getRates(String url){

        URL xmlURL = null;
        try {
            xmlURL = new URL(url);
            InputStream xml = xmlURL.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xml);


            NodeList kod_waluty = doc.getElementsByTagName("kod_waluty");
            NodeList kurs_sredni = doc.getElementsByTagName("kurs_sredni");

            for (int i = 0; i < kod_waluty.getLength(); i++) {

                if (kod_waluty.item(i).getNodeType() == Node.ELEMENT_NODE && kurs_sredni.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element kod = (Element) kod_waluty.item(i);
                    Element kurs = (Element) kurs_sredni.item(i);

                    rates.put(kod.getTextContent(), Double.valueOf(kurs.getTextContent().replace(",", ".")));
                }
            }

            xml.close();
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
