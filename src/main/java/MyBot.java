import dtos.Activity;
import dtos.Goal;
import dtos.UserDto;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import states.States;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Abdulaziz Pulatjonov
 * Date: 06/29/2023 18:20
 */

public class MyBot extends TelegramLongPollingBot {
    HashMap<String, UserDto> users = new HashMap<>();
    HashMap<String, States> condition = new HashMap<>();

    @Override
    public String getBotUsername() {
        return TelegramBotUtils.USERNAME;
    }

    @Override
    public String getBotToken() {
        return TelegramBotUtils.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                if (update.getMessage().getText().equals("/start")) {
                    UserDto user = users.get(chatId);
                    if (user == null) {
                        user = new UserDto(update);
                        users.put(chatId, user);
                    }
                    try {
                        condition.put(chatId, States.MAIN_MENU);
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Welcome to the Element app's telegram bot!");
                        sendMessage.setChatId(chatId);
                        sendMessage.setReplyMarkup(first());
                        execute(sendMessage);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else if (condition.get(chatId).equals(States.MAIN_MENU)) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Choose your gender from the menu below:");
                    sendMessage.setChatId(chatId);
                    sendMessage.setReplyMarkup(genderMenu());
                    try {
                        condition.put(chatId, States.SET_GENDER);
                        execute(sendMessage);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else if (condition.get(chatId).equals(States.SET_GENDER)) {
                    String result = setGender(update);
                    if (result.equals("Success!")) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Insert your weight in digits!");
                        sendMessage.setChatId(chatId);
                        sendMessage.setReplyMarkup(first());
                        try {
                            condition.put(chatId, States.SET_WEIGHT);
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {

                    }
                } else if (condition.get(chatId).equals(States.SET_WEIGHT)) {
                    String result = setWeight(update);
                    if (result.equals("Success!")) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Insert your height in digits!");
                        sendMessage.setChatId(chatId);
                        try {
                            condition.put(chatId, States.SET_HEIGHT);
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Try again with digital input only!");
                        sendMessage.setChatId(chatId);
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                } else if (condition.get(chatId).equals(States.SET_HEIGHT)) {
                    String result = setHeight(update);
                    if (result.equals("Success!")) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Insert your age in digits!");
                        sendMessage.setChatId(chatId);
                        try {
                            condition.put(chatId, States.SET_AGE);
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Try again with digital input only!");
                        sendMessage.setChatId(chatId);
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                } else if (condition.get(chatId).equals(States.SET_AGE)) {
                    String result = setAge(update);
                    if (result.equals("Success!")) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Choose your activity type from the list in the menu:\n\n"
                                + "SEDENTARY -> little to no exercise\n"
                                + "LIGHT -> 1-3 times exercise/week\n"
                                + "MODERATE -> 4-5 times exercise/week\n"
                                + "ACTIVE -> daily or intense exercise/week\n"
                                + "VERY_ACTIVE -> 6-7 times intense exercise/week\n"
                                + "EXTREME_ACTIVE -> very intense daily exercise or physical job");
                        sendMessage.setChatId(chatId);
                        sendMessage.setReplyMarkup(activityMarkup());
                        try {
                            condition.put(chatId, States.SET_ACTIVITY);
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Try again with digital input only!");
                        sendMessage.setChatId(chatId);
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                } else if (condition.get(chatId).equals(States.SET_ACTIVITY)) {
                    System.out.println(condition.get(chatId));
                    String result = setActivity(update);
                    System.out.println(result);
                    if (result.equals("Success!")) {
                        System.out.println("Success!");
                        String response = macroCalculator(update);
                        System.out.println("Res: " + response);
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText(response);
                        sendMessage.setChatId(chatId);
                        sendMessage.setReplyMarkup(first());
                        try {
                            condition.put(chatId, States.MAIN_MENU);
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Choose your activity type from the list in the menu:\n\n"
                                + "SEDENTARY -> little to no exercise\n"
                                + "LIGHT -> 1-3 times exercise/week\n"
                                + "MODERATE -> 4-5 times exercise/week\n"
                                + "ACTIVE -> daily or intense exercise/week\n"
                                + "VERY_ACTIVE -> 6-7 times intense exercise/week\n"
                                + "EXTREME_ACTIVE -> very intense daily exercise or physical job");
                        sendMessage.setChatId(chatId);
                        sendMessage.setReplyMarkup(activityMarkup());
                        try {
                            condition.put(chatId, States.SET_ACTIVITY);
                            execute(sendMessage);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
    }

    private ReplyKeyboard activityMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("SEDENTARY");

        KeyboardRow row1 = new KeyboardRow();
        row1.add("LIGHT");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("MODERATE");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("ACTIVE");

        KeyboardRow row4 = new KeyboardRow();
        row4.add("VERY_ACTIVE");

        KeyboardRow row5 = new KeyboardRow();
        row5.add("EXTREME_ACTIVE");
        keyboard.add(row);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup first() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Calculate your calory usage!");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup genderMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Male");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Female");
        keyboard.add(row);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    private String setAge(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        if (user != null) {
            try {
                Integer age = Integer.valueOf(update.getMessage().getText());
                user.setAge(age);
            } catch (Exception e) {
                return "Invalid input!";
            }
            users.put(update.getMessage().getChatId().toString(), user);
        } else {
            users.put(update.getMessage().getChatId().toString(), new UserDto(update));
        }
        return "Success!";
    }

    private String setGender(Update response) {
        UserDto user = users.get(response.getMessage().getChatId().toString());
        if (user != null) {
            if (response.getMessage().getText().toLowerCase().equals("male")) {
                user.setGender(UserDto.Gender.MALE);
            } else if (response.getMessage().getText().toLowerCase().equals("female")) {
                user.setGender(UserDto.Gender.FEMALE);
            } else {
                return "Invalid input!";
            }
            users.put(response.getMessage().getChatId().toString(), user);
        } else {
            users.put(response.getMessage().getChatId().toString(), new UserDto(response));
        }
        return "Success!";
    }

    private String setHeight(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        if (user != null) {
            try {
                Float height = Float.valueOf(update.getMessage().getText());
                user.setHeight(height);
            } catch (Exception e) {
                return "Invalid input!";
            }
            users.put(update.getMessage().getChatId().toString(), user);
        } else {
            users.put(update.getMessage().getChatId().toString(), new UserDto(update));
        }
        return "Success!";
    }

    private String setWeight(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        if (user != null) {
            try {
                float weight = Float.parseFloat(update.getMessage().getText());
                user.setWeight(weight);
            } catch (Exception e) {
                return "Invalid input!";
            }
            users.put(update.getMessage().getChatId().toString(), user);
        } else {
            users.put(update.getMessage().getChatId().toString(), new UserDto(update));
        }
        return "Success!";
    }

    private String setNeck(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        if (user != null) {
            try {
                float neck = Float.parseFloat(update.getMessage().getText());
                user.setNeck(neck);
            } catch (Exception e) {
                return "Invalid input!";
            }
            users.put(update.getMessage().getChatId().toString(), user);
        } else {
            users.put(update.getMessage().getChatId().toString(), new UserDto(update));
        }
        return "Success!";
    }

    private String setWaist(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        if (user != null) {
            try {
                float waist = Float.parseFloat(update.getMessage().getText());
                user.setWaist(waist);
            } catch (Exception e) {
                return "Invalid input!";
            }
            users.put(update.getMessage().getChatId().toString(), user);
        } else {
            users.put(update.getMessage().getChatId().toString(), new UserDto(update));
        }
        return "Success!";
    }

    private String setHip(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        if (user != null) {
            try {
                float hip = Float.parseFloat(update.getMessage().getText());
                user.setHip(hip);
            } catch (Exception e) {
                return "Invalid input!";
            }
            users.put(update.getMessage().getChatId().toString(), user);
        } else {
            users.put(update.getMessage().getChatId().toString(), new UserDto(update));
        }
        return "Success!";
    }

    private String setActivity(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        if (user != null) {
            String activity = update.getMessage().getText();
            switch (activity.toLowerCase()) {
                case "sedentary" -> user.setActivity(Activity.SEDENTARY);
                case "light" -> user.setActivity(Activity.LIGHT);
                case "moderate" -> user.setActivity(Activity.MODERATE);
                case "active" -> user.setActivity(Activity.ACTIVE);
                case "very active" -> user.setActivity(Activity.VERY_ACTIVE);
                case "extreme active" -> user.setActivity(Activity.EXTREME_ACTIVE);
            }
            users.put(update.getMessage().getChatId().toString(), user);
        } else {
            users.put(update.getMessage().getChatId().toString(), new UserDto(update));
        }
        return "Success!";
    }

    private String setGoal(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        if (user != null) {
            String goal = update.getMessage().getText();
            switch (goal.toLowerCase()) {
                case "maintain weight" -> user.setGoal(Goal.MAINTAIN_WEIGHT);
                case "mid weight loss" -> user.setGoal(Goal.MID_WEIGHT_LOSS);
                case "weight loss" -> user.setGoal(Goal.WEIGHT_LOSS);
                case "extreme weight loss" -> user.setGoal(Goal.EXTREME_WEIGHT_LOSS);
                case "mild weight gain" -> user.setGoal(Goal.MILD_WEIGHT_GAIN);
                case "weight gain" -> user.setGoal(Goal.WEIGHT_GAIN);
                case "extreme weight gain" -> user.setGoal(Goal.EXTREME_WEIGHT_GAIN);
            }
            users.put(update.getMessage().getChatId().toString(), user);
        } else {
            users.put(update.getMessage().getChatId().toString(), new UserDto(update));
        }
        return "Success!";
    }

    private String macroCalculator(Update update) {
        UserDto user = users.get(update.getMessage().getChatId().toString());
        double bmr = 0;
        if (user.getGender().equals(UserDto.Gender.MALE)) {
            bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5;
        } else {
            bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161;
        }
        double indexA = 1.2;
        switch (user.getActivity()) {
            case LIGHT -> indexA = 1.375;
            case MODERATE -> indexA = 1.55;
            case VERY_ACTIVE -> indexA = 1.725;
            case EXTREME_ACTIVE -> indexA = 1.9;
        }
        double index = bmr * indexA;

        double carbohydratesMin = (index / 4) * 0.45;
        double carbohydratesMax = (index / 4) * 0.65;

        double proteinMin = (index / 4) * 0.1;
        double proteinMax = (index / 4) * 0.35;

        double fatMin = (index / 9) * 0.2;
        double fatMax = (index / 9) * 0.35;

        return "BMR: " + (int)bmr
                + "\n\nProtein: " + (int)((proteinMin + proteinMax) / 2) + " grams/day\n"
                + "Range: " + (int)proteinMin + " - " + (int)proteinMax + "\n\n"
                + "Carbohydrates: " + (int)((carbohydratesMin + carbohydratesMax) / 2) + " grams/day\n"
                + "Range: " + (int)carbohydratesMin + " - " + (int)carbohydratesMax + "\n\n"
                + "Fat: " + (int)((fatMax + fatMin) / 2) + " grams/day\n"
                + "Range: " + (int)fatMin + " - " + (int)fatMax;
    }
}
