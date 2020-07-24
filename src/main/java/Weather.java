import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    //4c8d3aa984c3ace4ea1a51efd7ae593f
    //http://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=4c8d3aa984c3ace4ea1a51efd7ae593f
    public static String getWeather(String message, Model model) throws MalformedURLException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message
                + "&units=metric&appid=4c8d3aa984c3ace4ea1a51efd7ae593f");

        String result = "";
        try(Scanner in = new Scanner((InputStream)url.getContent())){
            while (in.hasNext()){
                result += in.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(result);

        model.setName(jsonObject.getString("name"));

        JSONObject main = jsonObject.getJSONObject("main");

        model.setHumidity(main.getDouble("humidity"));
        model.setTemp(main.getDouble("temp"));

        JSONArray jsonArray = jsonObject.getJSONArray("weather");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            model.setIcon(obj.getString("icon"));
            model.setMain(obj.getString("main"));
        }

        return "City: " + model.getName() + "\n" + "Weather: " + model.getMain() + " " + picture(model.getIcon()) + "\nTemperature: " + model.getTemp()
                + "°C" + "\nHumidity: " + model.getHumidity() + "%\n";
    }

    private static String picture(String name){
        switch (name){
            case "01d":
                return "☀️";
            case "02d":
                return "\uD83C\uDF24";
            case "03d":
                return "\uD83C\uDF25";
            case "04d":
                return "☁️";
            case "09d":
                return "\uD83C\uDF27";
            case "10d":
                return "\uD83C\uDF26";
            case "11d":
                return "\uD83C\uDF29";
            case "13d":
                return "❄️";
            case "50d":
                return "\uD83C\uDF2B";
            case "01n":
                return "\uD83C\uDF15";
            case "02n":
                return "\uD83C\uDF24";
            case "03n":
                return "\uD83C\uDF25";
            case "04n":
                return "☁️";
            case "09n":
                return "\uD83C\uDF27";
            case "10n":
                return "\uD83C\uDF26";
            case "11n":
                return "\uD83C\uDF29";
            case "13n":
                return "❄️";
            case "50n":
                return "\uD83C\uDF2B";
            default:
                return "\uD83E\uDD8A";
        }
    }
}
