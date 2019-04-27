package impls;

import interfaces.RecievedMSG;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public class Inst implements RecievedMSG {

    private Update update;
    private String unformatted;

    @Override
    public SendMessage action() throws Exception {

        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(getCaption(unformatted));
    }

    public Inst(Update update, String unformatted){
        this.update = update;
        this.unformatted = unformatted;
    }


    private static String getCaption( String response){

        JSONObject myResponse = new JSONObject(response);

        myResponse = (JSONObject) myResponse.get("graphql");

        myResponse = (JSONObject) myResponse.get("shortcode_media");

        myResponse = (JSONObject) myResponse.get("edge_media_to_caption");

        JSONArray jArray = new JSONArray(myResponse.get("edges").toString());

        myResponse = jArray.getJSONObject(0);

        myResponse = (JSONObject) myResponse.get("node");

        String my = myResponse.get("text").toString();

        return my.concat("\n ↑ caption from Pump's inst");
    }
}

