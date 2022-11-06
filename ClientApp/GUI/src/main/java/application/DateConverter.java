package application;

import customContainers.Pair;

import java.util.Date;

public class DateConverter {

    public static String convert(Date date){
        var dateStr = "";
        dateStr += date.getHours() + ":";
        var minutes = String.valueOf(date.getMinutes());
        if (minutes.length() == 1){
            minutes = '0' + minutes;
        }
        return dateStr += minutes;
    }

    public  static String convertDifference(Date date1, Date date2){
       return convert(new Date(date1.getTime() - date2.getTime()));
    }

    public static Pair<Integer, Integer> stringToHHMM(String str, String splitter) throws Exception{

        var values = str.split(splitter);
        if (values.length != 2) throw new Exception();
        Pair<Integer, Integer> pair = new Pair<>();
        pair.setFirstValue(Integer.parseInt(values[0]));
        pair.setSecondValue(Integer.parseInt(values[1]));
        return pair;
    }
}
