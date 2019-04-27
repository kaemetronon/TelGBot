package impls;

import interfaces.RecievedMSG;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;


public class Keyboard implements RecievedMSG {

    private String text;
    private long chat_id;

    public Keyboard(String text, long chat_id) {
        this.chat_id = chat_id;
        this.text = text;
    }

    @Override
    public SendMessage action(){
        return makeKeyboard();
    }

    private SendMessage makeKeyboard(){
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("inf");
        row.add("inst");
        row.add("wea London");
        row.add("type your city");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }


}
