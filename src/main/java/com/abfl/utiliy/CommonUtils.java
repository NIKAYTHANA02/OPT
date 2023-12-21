package com.abfl.utiliy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    public  static Long getTimeDifferenceinSec(Date startTime,Date endTime)
    {
        System.out.println("Start Date:"+startTime+" End Date:"+endTime);
        //Date startTime=new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").parse("2022-27-06 22:34:00");

        //Date endTime=new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").parse("2022-27-06 22:34:10");

        //java.util.Date endTime=new Date();

        long duration = (endTime.getTime() - startTime.getTime());
        System.out.println(duration);
        long difference_In_Seconds
                = (duration
                / 1000);
        System.out.println(difference_In_Seconds);
        return  difference_In_Seconds;
    }
}
