package application;

import java.util.ArrayList;

public class InputValidation {

    private static final int maxLoginLength = 50;
    private static final int minLoginLength = 5;
    private static final int maxPasswordLength = 50;
    private static final int minPasswordLength = 5;

    public static boolean isLoginCorrect(String login) {
        return !login.isBlank() && login.length() > minLoginLength && login.length() < maxLoginLength;
    }

    public static boolean isPasswordCorrect(String password) {
        return !password.isBlank() && password.length() > minPasswordLength && password.length() < maxPasswordLength;
    }

    public static boolean isCardNumberCorrect(String number) {

        String visa = "^4[0-9]{12}(?:[0-9]{3})?$";
        String visaMasterCard = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14})$";
        String unionPay = "^(62[0-9]{14,17})$";
        String masterCard = "^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$";
        String[] regexArray = new String[]{
                visa,
                visaMasterCard,
                unionPay,
                masterCard,
        };
        for (var regex: regexArray) {
            if(number.matches(regex)) return true;
        }
        return false;
    }

    public static boolean isDateOfIssueCorrect(String dateOfIssue){

        String regex = "^(0[1-9]|1[0-2])\\/?([0-9]{2})$";
        return dateOfIssue.matches(regex);
    }

    public static boolean isCVVCorrect(String CVV){

        String regex = "^[0-9]{3,4}$";
        return CVV.matches(regex);
    }

    public static boolean isCardOwnerCorrect(String cardOwner){

        String regex = "[a-zA-Z]+";
        return cardOwner.matches(regex);
    }

    public static boolean isEmailCorrect(final char[] input) {

        if (input == null) {
            return false;
        }

        int state = 0;
        char ch;
        int index = 0;
        int mark = 0;
        String local = null;
        ArrayList<String> domain = new ArrayList<String>();

        while (index <= input.length && state != -1) {

            if (index == input.length) {
                ch = '\0'; // Так мы обозначаем конец нашей работы
            } else {
                ch = input[index];
                if (ch == '\0') {
                    // символ, которым мы кодируем конец работы, не может быть частью ввода
                    return false;
                }
            }
            switch (state) {

                case 0: {
                    // Первый символ {atext} -- текстовой части локального имени
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                            || (ch >= '0' && ch <= '9') || ch == '_' || ch == '-'
                            || ch == '+') {
                        state = 1;
                        break;
                    }
                    // Если встретили неправильный символ -> отмечаемся в state об ошибке
                    state = -1;
                    break;
                }

                case 1: {
                    // Остальные символы {atext} -- текстовой части локального имени
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                            || (ch >= '0' && ch <= '9') || ch == '_' || ch == '-'
                            || ch == '+') {
                        break;
                    }
                    if (ch == '.') {
                        state = 2;
                        break;
                    }
                    if (ch == '@') { // Конец локальной части
                        local = new String(input, 0, index - mark);
                        mark = index + 1;
                        state = 3;
                        break;
                    }
                    // Если встретили неправильный символ -> отмечаемся в state об ошибке
                    state = -1;
                    break;
                }

                case 2: {
                    // Переход к {atext} (текстовой части) после точки
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                            || (ch >= '0' && ch <= '9') || ch == '_' || ch == '-'
                            || ch == '+') {
                        state = 1;
                        break;
                    }
                    // Если встретили неправильный символ -> отмечаемся в state об ошибке
                    state = -1;
                    break;
                }

                case 3: {
                    // Переходим {alnum} (домену), проверяем первый символ
                    if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')
                            || (ch >= 'A' && ch <= 'Z')) {
                        state = 4;
                        break;
                    }
                    // Если встретили неправильный символ -> отмечаемся в state об ошибке
                    state = -1;
                    break;
                }

                case 4: {
                    // Собираем {alnum} --- домен
                    if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')
                            || (ch >= 'A' && ch <= 'Z')) {
                        break;
                    }
                    if (ch == '-') {
                        state = 5;
                        break;
                    }
                    if (ch == '.') {
                        domain.add(new String(input, mark, index - mark));
                        mark = index + 1;
                        state = 5;
                        break;
                    }
                    // Проверка на конец строки
                    if (ch == '\0') {
                        domain.add(new String(input, mark, index - mark));
                        state = 6;
                        break; // Дошли до конца строки -> заканчиваем работу
                    }
                    // Если встретили неправильный символ -> отмечаемся в state об ошибке
                    state = -1;
                    break;
                }

                case 5: {
                    if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')
                            || (ch >= 'A' && ch <= 'Z')) {
                        state = 4;
                        break;
                    }
                    if (ch == '-') {
                        break;
                    }
                    // Если встретили неправильный символ -> отмечаемся в state об ошибке
                    state = -1;
                    break;
                }

                case 6: {
                    // Успех! (На самом деле, мы сюда никогда не попадём)
                    break;
                }
            }
            index++;
        }

        // Остальные проверки

        // Не прошли проверку выше? Возвращаем false!
        if (state != 6)
            return false;

        // Нам нужен домен как минимум второго уровня
        if (domain.size() < 2)
            return false;

        // Ограничения длины по спецификации RFC 5321
        if (local.length() > 64)
            return false;

        // Ограничения длины по спецификации RFC 5321
        if (input.length > 254)
            return false;

        // Домен верхнего уровня должен состоять только из букв и быть не короче двух символов
        index = input.length - 1;
        while (index > 0) {
            ch = input[index];
            if (ch == '.' && input.length - index > 2) {
                return true;
            }
            if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))) {
                return false;
            }
            index--;
        }
        return true;
    }

    public static boolean isStringContainsOnlyLetters(String str){
        String regex = "[a-zA-Z]+";
        return str.matches(regex);
    }

    public static boolean isPhoneNumber(String phoneNumber){
        String regex = "^\\\\s?((\\\\+[1-9]{1,4}[ \\\\-]*)|(\\\\([0-9]{2,3}\\\\)[ \\\\-]*)|([0-9]{2,4})[ \\\\-]*)*?[0-9]{3,4}?[ \\\\-]*[0-9]{3,4}?\\\\s?";
        return phoneNumber.matches(regex);
    }

}
