import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BotHelper {

    public static ArrayList<String> getMainMenuOptions() {
        ArrayList<String> answeroptions = new ArrayList<>();
        answeroptions.add(Answers.SEND_PART.text);
        answeroptions.add(Answers.ADDRESS_AND_WORKING_TIME.text);
        answeroptions.add(Answers.DIAGNOSTICS.text);
        answeroptions.add(Answers.CONTACTS.text);
        answeroptions.add(Answers.REPAIR_GIDRO.text);
        answeroptions.add(Answers.STATUS_OF_REPAIR.text);
        answeroptions.add(Answers.CHANGE_CITY.text);
        answeroptions.add(Answers.MAKE_ORDER.text);
        return answeroptions;
    }

    public static List<KeyboardRow> setAnswerOptions(ArrayList<String> answerOptions) {
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        int answersAmountToAdd = answerOptions.size();
        int rowsAmount = answersAmountToAdd%2 == 0 ? answersAmountToAdd/2 : answersAmountToAdd/2 + 1;

        Iterator<String> iterator = answerOptions.iterator();

        for (int i = 0; i < rowsAmount; i++) {
            KeyboardRow keyboardRow = new KeyboardRow();
            int buttonsInRow = 0;
            while(iterator.hasNext()) {
                if (buttonsInRow >= 2) {
                    break;
                }

                buttonsInRow++;
                String buttonText = iterator.next();
                if (!buttonText.equals("Поделиться контактом")) {
                    keyboardRow.add(new KeyboardButton(buttonText));
                } else {
                    keyboardRow.add(new KeyboardButton(buttonText).setRequestContact(true));
                }
            }
            keyboardRowList.add(keyboardRow);

        }
        return keyboardRowList;
    }

    public enum Answers {
        LVOV_CITY("Львов"),
        ODESSA_CITY("Одесса"),
        KIEV_CITY("Киев"),
        KHARKOV_CITY("Харьков"),
        GREETING("Виртуальный ассистент Мактранс приветствует Вас, "),
        SELECT_CITY("Выберите ближайший к Вам город"),
        GIVE_NUMBER("Предоставьте Ваш номер телефона пожалуйста, чтобы мы могли узнать статус ремонта.\n\nНажмите \"Поделиться контактом\""),
        CITY_CHOSEN("Вы выбрали город "),
        SELECT_OPTION("Выберите интересующий Вас вопрос"),
        ADDRESS_AND_WORKING_TIME(Emoji.ROUND_PUSHPIN+""+Emoji.ALARM_CLOCK+ "Адрес и график работы"),
        DIAGNOSTICS(Emoji.POLICE_CAR+"Записаться на диагностику"),
        CONTACTS(Emoji.MOBILE_PHONE+"Контакты"),
        REPAIR_GIDRO(Emoji.WRENCH +"Ремонт гидротрансформатора"),
        CHANGE_CITY(Emoji.BACK_ARROW+"Поменять город"),
        BACK_BUTTON("Назад"),
        STATUS_GIDROTRANSFORMATOR("Ремонт гидротрансформатора"),
        STATUS_CAR("Ремонт автомобиля"),
        POST_DATA(Emoji.ROUND_PUSHPIN +"г. Киев, Новая Почта \n"+"Получатель: Рабизо Дмитрий +38050 527 22 36\n\n"+Emoji.ROUND_PUSHPIN+"до 30кг: отделение \u211617\n"+Emoji.ROUND_PUSHPIN+
                "больше 30кг: отделение \u2116225\n\n " +
                Emoji.ALARM_CLOCK+"Мы забираем посылки два раза в день в 11:00 и 15:00"),
        OTMENA(Emoji.BACK_ARROW+"Отмена"),
        SEND_PART(Emoji.POST_BOX+"Отправить деталь в ремонт"),
        SHARE_CONTACT("Поделиться контактом"),
        STATUS_OF_REPAIR(Emoji.SPEECH+"Узнать статус ремонта"),
        WORKING_TIMES_SHOP(Emoji.MONEY_BAG+"Отдел запчастей:\nПонедельник - пятница 9:00-18:00\n\n"),
        WORKING_TIMES1(Emoji.WRENCH +"Отдел ремонта:\nПонедельник - суббота 9:00-18:00, воскресенье выходной\n\nАдрес в Google Maps"+ Emoji.ARROW_DOWN),
        CONTACT_KIEV(Emoji.ROUND_PUSHPIN+"Железнодорожное шоссе:\n+380505272236\n\n\n"+Emoji.ROUND_PUSHPIN+ "ул. Степана Бандеры:\n+380990015742\n+380968965155"),
        CONTACT_ODESSA(Emoji.WRENCH + "Отдел ремонта:\n+380981103761\n+380502628449\n+380674692328"),
        CONTACT_KHARKOV(Emoji.WRENCH +"Отдел ремонта:\n+380968122122\n+380931410041"),
        CONTACT_LVOV(Emoji.WRENCH +"Отдел ремонта:\n+380674692328\n+380502628449"),
        DIAGNOSTICS_INFO(Emoji.HEAVY_EXCLAMATION_MARK_SYMBOL+"Подъезжайте в порядке живой очереди с 09:00 до 18:00, в любой день, кроме воскресенья.\n\n"
                +Emoji.DOLLAR+"Цена: 500 гривен"),
        GIDRO_TRANS_INFO(Emoji.ALARM_CLOCK+"Ремонт занимает 1-2 дня, после того как мы заберем его с Новой Почты.\n\n"+Emoji.DOLLAR+"Цена: Ремонт 100$ + Запчасти 40-180$ в зависимости от модели"),
        MAKE_ORDER(Emoji.SHOPPING_BAGS+"Оформить заказ");





        public final String text;

        private Answers(String text) {
            this.text = text;
        }
    }
}
