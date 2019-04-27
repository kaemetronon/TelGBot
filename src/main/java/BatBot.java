import handlers.IsMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class BatBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            IsMessage isMessage = new IsMessage(update);
            SendMessage sendMessage = null;
            try {
                sendMessage = isMessage.detect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else if (update.hasMessage() && update.getMessage().hasPhoto()){

            long chat_id = update.getMessage().getChatId();

            List<PhotoSize> photo = update.getMessage().getPhoto();

            String f_id = photo.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();

            SendPhoto message = new SendPhoto()
                    .setChatId(chat_id)
                    .setPhoto(f_id)
                    .setCaption("она самая");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    private String getAccessToken() {
        Properties prop = new Properties();
        String accessToken = null;

        try {
            prop.load(new FileInputStream("src/main/resources/botconfig.properties"));
            accessToken = prop.getProperty("token");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файда конфигурации");
        }
        return accessToken;
    }

    private String getUsername() {
        Properties prop = new Properties();
        String username = null;

        try {
            prop.load(new FileInputStream("src/main/resources/botconfig.properties"));
            username = prop.getProperty("username");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файда конфигурации");
        }
        return username;
    }

    @Override
    public String getBotUsername() {
        return getUsername();
    }

    @Override
    public String getBotToken() {
        return getAccessToken();
    }

}
