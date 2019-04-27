package handlers;

import impls.GetInfo;
import impls.Inst;
import impls.Keyboard;
import impls.Weather;
import interfaces.RecievedMSG;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IsMessage {

    private Update update;
    private final String UNHANDLED_ERROR = "Unhandled";
    private final String CONNECTION_ERROR = "Connection error";
    private SendMessage typeOfMessage;
    private long chat_id;
    private String text;


    public IsMessage(Update update) {
        this.update = update;
        chat_id = update.getMessage().getChatId();
        text = handl(update.getMessage().getText());
    }

    public SendMessage detect() throws Exception {
        if (text.equals("inf")) {

            RecievedMSG recievedMSG = new GetInfo(update);
            typeOfMessage = recievedMSG.action();

        } else if (text.equals("inst")) {
            String[] resp = getFromUrl("https://www.instagram.com/p/BwSxkXrnGmI/?__a=1");
            if (Integer.parseInt(resp[1]) == 200) {
                String unformatted = resp[0];
                RecievedMSG recievedMSG = new Inst(update, unformatted);
                typeOfMessage = recievedMSG.action();
            } else {
                SendMessage message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(CONNECTION_ERROR);
            }
        } else if (text.equals("key")) {

            RecievedMSG recievedMSG = new Keyboard("+", chat_id);
            typeOfMessage = recievedMSG.action();

        } else if (text.contains("wea ")) {
            String[] words = text.split("\\s");
            String API_CALL_TEMPLATE = "https://api.openweathermap.org/data/2.5/forecast?q=";
            String API_KEY_TEMPLATE = "&units=metric&APPID=92dd49259b47f763ecb5d94b6fd36fa3";
            String url = API_CALL_TEMPLATE + words[1] + API_KEY_TEMPLATE;

            String[] resp = getFromUrl(url);
            if (Integer.parseInt(resp[1]) == 200) {
                RecievedMSG recievedMSG = new Weather(update, resp[0], words[1]);
                typeOfMessage = recievedMSG.action();
            } else {
                typeOfMessage = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(CONNECTION_ERROR);
            }
        } else {
            typeOfMessage = new SendMessage()
                    .setChatId(chat_id)
                    .setText(UNHANDLED_ERROR);
        }

        return typeOfMessage;
    }

    private String handl(String string) {
        if (string.equals("inst") || string.equals("inf")
                || string.equals("key")
                || string.contains("wea"))
            return string;
        else
            return UNHANDLED_ERROR;
    }

    private String[] getFromUrl(String url) throws IOException {

        String[] resp = new String[2];
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            resp[0] = response.toString();
            resp[1] = String.valueOf(responseCode);
            return resp;
        } else {
            resp[0] = "null";
            resp[1] = String.valueOf(responseCode);
            return resp;
        }

    }
}
