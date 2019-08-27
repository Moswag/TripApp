package com.alium.nibo.origindestinationpicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String args[]) throws Exception{
        String date = "21-9-2017";
        SimpleDateFormat sdt = new SimpleDateFormat("d-m-YYYY");
        Date result = sdt.parse(date);
        System.out.println(result);
    }
}
