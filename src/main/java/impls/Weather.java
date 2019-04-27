package impls;

import interfaces.RecievedMSG;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Weather implements RecievedMSG {

    private Update update;
    private String city;
    private String unformatted;


    @Override
    public SendMessage action() {

        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(parse(unformatted));

    }

    public Weather(Update update, String unformatted, String city) {
        this.update = update;
        this.city = city;
        this.unformatted = unformatted;
    }

    private String parse(String unformatted) {

        JSONObject myResponse = new JSONObject(unformatted);

        JSONArray jArray = new JSONArray(myResponse.get("list").toString());

        myResponse = (JSONObject) jArray.get(7);

        JSONObject main = (JSONObject) myResponse.get("main");
        String tempStr = main.get("temp").toString();
        double tempD = Double.parseDouble(tempStr);

        JSONArray weather = new JSONArray(myResponse.getJSONArray("weather").toString());
        JSONObject t = (JSONObject) weather.get(0);
        String description = t.get("description").toString();

        String date = myResponse.get("dt_txt").toString();

        return "In " + city + " " + description + " & " + tempD + "°С at " + date;

    }
}
