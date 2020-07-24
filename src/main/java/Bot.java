import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
//import sun.jvm.hotspot.debugger.remote.ppc64.RemotePPC64Thread;

import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {

    private ArrayList<UserInfo> info = new ArrayList<>();
    private ArrayList<Long> id = new ArrayList<>();
    private long userIndex = 0L;

    public static void main(String args[]){
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try{
           telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg (Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try{
            setButton(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButton(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboardRowArrayList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Добавить новый город"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();

        for (String s: info.get((int) userIndex).cities) {
            keyboardSecondRow.add(new KeyboardButton(s));
        }

        if (info.get((int) userIndex).cities.size() != 0){
            keyboardFirstRow.add(new KeyboardButton("Удалить город"));
        }

        keyboardRowArrayList.add(keyboardFirstRow);
        keyboardRowArrayList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowArrayList);
    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();

        if(!id.contains(message.getChatId())){
            id.add(message.getChatId());
            userIndex = id.indexOf(message.getChatId());
            info.add(new UserInfo(userIndex));
        }

        if (message != null && message.hasText()){
            String str = message.getText();
            if(info.get((int) userIndex).newCityFlag){
                if (info.get((int) userIndex).cities.size() < 4 ) {
                    if(!info.get((int) userIndex).cities.contains(message.getText().intern())) {
                        info.get((int) userIndex).cities.add(message.getText());
                        sendMsg(message, "Добавлено.");
                    } else {
                        sendMsg(message, "Этот город уже в избраном.");
                    }
                } else {
                    sendMsg(message, "В избранном может находится максимум 4 города.");
                }
                info.get((int) userIndex).newCityFlag = false;
            } else if(info.get((int) userIndex).removeCityFlag){
                info.get((int) userIndex).cities.remove(message.getText());
                sendMsg(message, "Удалено.");
                info.get((int) userIndex).removeCityFlag = false;
            } else {
                switch (str) {
                    case "Добавить новый город":
                        addCity(message);
                        break;
                    case "Удалить город":
                        removeCity(message);
                        break;
                    case "/start":
                        sendMsg(message, "Введите город:");
                        break;
                    default:
                        try {
                            sendMsg(message, Weather.getWeather(message.getText(), model));
                        } catch (Exception e) {
                            sendMsg(message, "Такого города нет.");
                        }
                }
            }
        }
    }

    private void addCity(Message message){
        sendMsg(message, "Введите город, который хотите сделать избранным:");
        info.get((int) userIndex).newCityFlag = true;
    }

    private void removeCity(Message message){
        sendMsg(message, "Введите город, который хотите удалить из избранного:");
        info.get((int) userIndex).removeCityFlag = true;
    }

    public String getBotUsername() {
        return "MyJavaBot";
    }

    public String getBotToken() {
        return "1291958873:AAEFxlUTk1aze2ztjeq-eYKj7KUKXSO31hY";
    }
}
