package interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface RecievedMSG {
    SendMessage action() throws Exception;
}
