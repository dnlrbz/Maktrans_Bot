import com.sun.org.apache.xerces.internal.dom.AttrNSImpl;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import BotDB.*;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private UserDBManager userDBManager = new UserDBManager();
    private static ArrayList<String> cities = new ArrayList<>();
    private static final String CANCEL_COMMAND = "/stop";


    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        cities.add(Emoji.ROUND_PUSHPIN+ BotHelper.Answers.KIEV_CITY.text);
        cities.add(Emoji.ROUND_PUSHPIN+ BotHelper.Answers.ODESSA_CITY.text);
        cities.add(Emoji.ROUND_PUSHPIN+BotHelper.Answers.KHARKOV_CITY.text);
        cities.add(Emoji.ROUND_PUSHPIN+BotHelper.Answers.LVOV_CITY.text);
        try {
            telegramBotsApi.registerBot((LongPollingBot) new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }


    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        User user = null;
        String userID = message.getFrom().getId().toString();
        ArrayList<String> answeroptions = BotHelper.getMainMenuOptions();

        if (message != null && (message.hasText() || message.hasContact())) {
            String messageText = null;
            if (message.hasText()) {
                messageText = message.getText().toString();
                System.out.println(update.getMessage().getText());
            } else {
                messageText = "";
            }

            /**
             * Начальное сообщение
             */
            if (messageText.equals("/start")) {
                //System.out.println("/start");
                //String phoneNumber = userDBManager.getUserById(userID).getPhoneNumber();
                //if (phoneNumber==null || phoneNumber.equals("")) {
                    user = new User(message.getFrom().getId().toString(), "/start", "_", "");
                //} else {
                //    user = new User(message.getFrom().getId().toString(), "/start", phoneNumber, "");
               // }

                handleUser(user);
                sendMsg(message, BotHelper.Answers.GREETING.text + update.getMessage().getFrom().getFirstName()
                        + "!\n\n" + BotHelper.Answers.SELECT_CITY.text, cities);
            }
            /**
             * Cообщение выбора ближайшего города
             */
            else if (messageText.substring(2).equals(BotHelper.Answers.KHARKOV_CITY.text) || messageText.substring(2).equals(BotHelper.Answers.KIEV_CITY.text)
                    || messageText.substring(2).equals(BotHelper.Answers.ODESSA_CITY.text) || messageText.substring(2).equals(BotHelper.Answers.LVOV_CITY.text)) {
                System.out.println(messageText.substring(2));
                user = new User(userID, "/city", userDBManager.getUserById(userID).getPhoneNumber(), messageText.substring(2));
                handleUser(user);

                sendMsg(message, BotHelper.Answers.CITY_CHOSEN.text + messageText, answeroptions);
            }
            /**
             * Сообщение, в котром пользователь делится номером телефона
             */
            else if (message.hasContact()) {
                user = new User(message.getFrom().getId().toString(), "/phone",
                        message.getContact().getPhoneNumber(),
                        userDBManager.getUserById(message.getFrom().getId().toString()).getCity());
                handleUser(user);
                ArrayList<String> options = new ArrayList<>();
                options.add(BotHelper.Answers.STATUS_CAR.text);
                options.add(BotHelper.Answers.STATUS_GIDROTRANSFORMATOR.text);
                options.add(BotHelper.Answers.OTMENA.text);
                System.out.println(userDBManager.getUserById(message.getFrom().getId().toString()));
                sendMsg(message, BotHelper.Answers.SELECT_OPTION.text, options);

            }
            /**
             * Если пользователь нажал Отмена
             */
            else if (messageText.equals(BotHelper.Answers.OTMENA.text)) {
                System.out.println(userDBManager.getUserById(message.getFrom().getId().toString()));
                sendMsg(message, BotHelper.Answers.SELECT_OPTION.text, answeroptions);
            }

            /**
             * Пользователь выбрал вопрос "Куда отправить деталь в ремонт"
             */
            else if (messageText.equals(BotHelper.Answers.SEND_PART.text)) {
                System.out.println("post");
                sendMsg(message, BotHelper.Answers.POST_DATA.text, answeroptions);

            }
            /**
             * Адрес и график работы
             */
            else if (messageText.equals(BotHelper.Answers.ADDRESS_AND_WORKING_TIME.text)) {
                String userCity = userDBManager.getUserById(message.getFrom().getId().toString()).getCity();
                System.out.println(userCity);
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                List<InlineKeyboardButton> row1 = new ArrayList<>();
                List<InlineKeyboardButton> row2 = new ArrayList<>();
                InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
                markupKeyboard.setKeyboard(buttons);
                SendMessage sendMessage = new SendMessage();
                sendMessage.enableMarkdown(true);
                // Боту может писать не один человек, и поэтому чтобы отправить сообщение, нужно узнать куда его отправлять
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setReplyToMessageId(message.getMessageId());


                if (userCity.equals(BotHelper.Answers.KIEV_CITY.text)) {
                    row1.add(new InlineKeyboardButton().setText("Степана Бандеры 21В").setUrl("https://goo.gl/maps/BzDmc71EokB2"));
                    row2.add(new InlineKeyboardButton().setText("Железнодорожное шоссе 4").setUrl("https://goo.gl/maps/DTwJTG2rBX32"));
                    buttons.add(row1);
                    buttons.add(row2);

                    sendMessage.setText(BotHelper.Answers.WORKING_TIMES_SHOP.text + BotHelper.Answers.WORKING_TIMES1.text);
                    inlineKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (userCity.equals(BotHelper.Answers.ODESSA_CITY.text)) {
                    row1.add(new InlineKeyboardButton().setText("Одесса ул. Чумацкая 56а").setUrl("https://goo.gl/maps/xwuodHVSgGJ2"));
                    buttons.add(row1);

                    sendMessage.setReplyToMessageId(message.getMessageId());
                    sendMessage.setText(BotHelper.Answers.WORKING_TIMES1.text);

                    inlineKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (userCity.equals(BotHelper.Answers.KHARKOV_CITY.text)) {
                    row1.add(new InlineKeyboardButton().setText("Харьков, ул. Тахиаташская 3").setUrl("https://goo.gl/maps/tz8S9rThJSw"));
                    buttons.add(row1);

                    sendMessage.setText(BotHelper.Answers.WORKING_TIMES1.text);

                    inlineKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (userCity.equals(BotHelper.Answers.LVOV_CITY.text)) {
                    row1.add(new InlineKeyboardButton().setText("Львов, ул. Хлебная 4").setUrl("https://goo.gl/maps/PzrskeEA9YC2"));
                    buttons.add(row1);

                    sendMessage.setText(BotHelper.Answers.WORKING_TIMES1.text);

                    inlineKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
            /**
             * Если пользователь выбрал "контакт"
             */
            else if (messageText.equals(BotHelper.Answers.CONTACTS.text)) {
                String userCity = userDBManager.getUserById(message.getFrom().getId().toString()).getCity();
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                List<InlineKeyboardButton> row1 = new ArrayList<>();
                row1.add(new InlineKeyboardButton().setText("Сайт").setUrl("https://akpp.com.ua"));
                buttons.add(row1);
                InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
                markupKeyboard.setKeyboard(buttons);
                SendMessage sendMessage = new SendMessage();
                sendMessage.enableMarkdown(true);
                // Боту может писать не один человек, и поэтому чтобы отправить сообщение, нужно узнать куда его отправлять
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setReplyToMessageId(message.getMessageId());
                /**
                 * Контакты в Киеве
                 */
                if (userCity.equals(BotHelper.Answers.KIEV_CITY.text)) {
                    sendMessage.setText(BotHelper.Answers.CONTACT_KIEV.text);
                    inlineKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                /**
                 * Контакты в Одесса
                 */
                else if (userCity.equals(BotHelper.Answers.ODESSA_CITY.text)) {
                    sendMessage.setText(BotHelper.Answers.CONTACT_ODESSA.text);
                    inlineKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                /**
                 * Контакты в Харьков
                 */
                else if (userCity.equals(BotHelper.Answers.KHARKOV_CITY.text)) {
                    sendMessage.setText(BotHelper.Answers.CONTACT_KHARKOV.text);
                    inlineKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                /**
                 * Контакты в Львов
                 */
                else if (userCity.equals(BotHelper.Answers.LVOV_CITY.text)) {
                    sendMessage.setText(BotHelper.Answers.CONTACT_LVOV.text);
                    inlineKeyboardMarkup.setKeyboard(buttons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
            /**
             * Если пользователь выбрал диагностику
             */
            else if (messageText.equals(BotHelper.Answers.DIAGNOSTICS.text)) {
                System.out.println("diagnostics");
                sendMsg(message, BotHelper.Answers.DIAGNOSTICS_INFO.text, answeroptions);
            }
            /**
             * Если пользователь выбрал статус ремонта
             */
            else if (messageText.equals(BotHelper.Answers.STATUS_OF_REPAIR.text)) {
                ArrayList<String> options = new ArrayList<>();
                String userPhone = userDBManager.getUserById(userID).getPhoneNumber()==null ? "":userDBManager.getUserById(userID).getPhoneNumber() ;
                options.add(BotHelper.Answers.SHARE_CONTACT.text);
                options.add(BotHelper.Answers.OTMENA.text);
                if (userPhone==null || userPhone.equals("_")) {
                    sendMsg(message, BotHelper.Answers.GIVE_NUMBER.text, options);
                }
                else {
                    ArrayList<String> otherOptions = new ArrayList<>();
                    otherOptions.add(BotHelper.Answers.STATUS_CAR.text);
                    otherOptions.add(BotHelper.Answers.STATUS_GIDROTRANSFORMATOR.text);
                    otherOptions.add(BotHelper.Answers.OTMENA.text);
                    System.out.println(userDBManager.getUserById(message.getFrom().getId().toString()));
                    sendMsg(message, BotHelper.Answers.SELECT_OPTION.text, otherOptions);
                }
            }
            /**
             * Пользователь хочет поменять город
             */
            else if (messageText.equals(BotHelper.Answers.CHANGE_CITY.text)) {
                sendMsg(message, BotHelper.Answers.SELECT_CITY.text + ", " + update.getMessage().getFrom().getFirstName(), cities);
            }
            /**
             * Пользователь выбрал ремонт гидротрансформатора
             */
            else if (messageText.equals(BotHelper.Answers.REPAIR_GIDRO.text)) {
                sendMsg(message, BotHelper.Answers.GIDRO_TRANS_INFO.text, answeroptions);
            }

        }
        /*
        // If inline Option button was pressed
        else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("update_msg_text")) {
                String answer = "Updated message text";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }

        }
        */
    }

    public String getBotUsername() {
        return "Мактранс Бот";
    }

    public String getBotToken() {
        return "762807472:AAFLMjE_nh2-UuYlouW4bDHXVM3VEJ_melY";
    }

    public void sendMsg(Message message, String text, ArrayList<String> answerOptions) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        // Боту может писать не один человек, и поэтому чтобы отправить сообщение, нужно узнать куда его отправлять
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            //message with answer;
            if (answerOptions != null)
                setButtons(sendMessage, answerOptions);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage, ArrayList<String> options) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        //dont hide keyboard
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = BotHelper.setAnswerOptions(options);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }


    private enum UserState {

        START("start"),
        PHONE("phone"),
        MAIN_MENU("main");

        private final String stateString;

        private UserState(String stateString) {
            this.stateString = stateString;
        }
    }

    public void handleUser(User user) {
        //add user if doesnt exist yet, otherwise update his state;
        if (!userDBManager.userExists(user.getUserId())) {
            userDBManager.insertUser(user);
        } else {
            userDBManager.updateUser(user);
        }

    }


}
