package web.english.application.utils;

import web.english.application.entity.ScheduleInfoHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleHelper {

    /**
     * format="yyyy-MM-dd"
     * @author VQKHANH
     * @param date
     * @return
     */
    public LocalDate convertStringToLocalDate(String date){
        String format="yyyy-MM-dd";
        DateTimeFormatter dateTimeFormat= DateTimeFormatter.ofPattern(format);
        LocalDate localDate=LocalDate.parse(date,dateTimeFormat);
        return localDate;
    }

    /**
     * return day of week following VN format
     * eg: 2022-05-03 => tuesday => T3
     * @author VQKHANH
     * @param localDate
     * @return
     */
    public String getDayOfWeekOfSpecifyDate(LocalDate localDate){
        String dayOfWeek;
        int dow=localDate.getDayOfWeek().getValue();
        switch (dow){
            case 2:
                dayOfWeek="T3";
                break;
            case 3:
                dayOfWeek="T4";
                break;
            case 4:
                dayOfWeek="T5";
                break;
            case 5:
                dayOfWeek="T6";
                break;
            case 6:
                dayOfWeek="T7";
                break;
            case 7:
                dayOfWeek="CN";
                break;
            default:
                dayOfWeek="T2";
        }
        return  dayOfWeek;
    }

    /**
     * @author VQKHANH
     * @param list
     * @return
     */
    public List<ScheduleInfoHolder> convertStringTiListScheduleHolder(List<String> list){
        List<ScheduleInfoHolder> scheduleInfoHolders=new ArrayList<>();
        for (String s: list
             ) {
            scheduleInfoHolders.add(convertStringToScheduleInfoHolder(s));
        }
        return scheduleInfoHolders;
    }

    /**
     * @author VQKHANH
     * @param s
     * @return
     */
    public ScheduleInfoHolder convertStringToScheduleInfoHolder(String s){
        String[] arr=s.trim().split(",");
        ScheduleInfoHolder scheduleInfoHolder=new ScheduleInfoHolder(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6]);
        return scheduleInfoHolder;
    }
}
