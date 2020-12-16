package com.forum.ensak.exceptions;

import java.util.Date;
import java.util.ArrayList;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = this.customizeDetails(details);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

//    x is detail we want to cstomize
    public String customizeDetails(String x)
    {
        x = x.replaceAll("\n",";");

        String[] a = x.split(";");
        String has = "";
        String brg="";

        ArrayList<String> cars = new ArrayList<String>();


        for (int i = 0;i<a.length;i++)
        {
            if(a[i].startsWith(" default message"))
                cars.add(a[i]);
        }
        for (int i = 0;i<cars.size();i++)
        {
            if(i % 2 != 0)
            {
                has += cars.get(i);
            }
        }
        has = has.replaceAll("\\[",";");
        has = has.replaceAll("\\]",";");

        String[] hamza = has.split(";");

        for (int i = 0;i<hamza.length;i++) {
            if (i % 2 != 0) {
                brg += hamza[i] + ",";

            }
        }
        return brg;

    }
}