package impls;

import interfaces.RecievedMSG;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.lang.reflect.Field;
import java.util.List;

public class GetInfo implements RecievedMSG {

    private Update update;
    private Message message;
    private User user;
    private String name;
    private String userName;
    private List<PhotoSize> photoSize;


    public GetInfo(Update update) {
        this.update = update;
    }


    private String getName_and_UserName() {
        try {

            Field fieldId = update.getClass().getDeclaredField("message");
            fieldId.setAccessible(true);
            message = (Message) fieldId.get(update);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field fieldId = message.getClass().getDeclaredField("from");
            fieldId.setAccessible(true);
            user = (User) fieldId.get(message);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field fieldId = user.getClass().getDeclaredField("userName");
            fieldId.setAccessible(true);
            userName = (String) fieldId.get(user);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field fieldId = user.getClass().getDeclaredField("firstName");
            fieldId.setAccessible(true);
            name = (String) fieldId.get(user);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return name.concat(" ".concat(userName));
    }

    @Override
    public SendMessage action() {
        return new SendMessage().setChatId(update.getMessage().getChatId())
                .setText(getName_and_UserName());
    }
}
