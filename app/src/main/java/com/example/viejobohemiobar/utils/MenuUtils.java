package com.example.viejobohemiobar.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.viejobohemiobar.R;
import com.example.viejobohemiobar.model.pojo.Product;
import com.example.viejobohemiobar.model.pojo.Result;

import java.util.Calendar;

public class MenuUtils {

    public static String getTotal(Result result) {
        Double total = 0.0;
        for (Product product : result.getResults()) {
            String stringPrice = product.getPrice().substring(1);
            double doble = Double.parseDouble(stringPrice);
            total = total + doble;
        }
        return "$" + total;
    }

    public static String getTime() {
        Calendar calendario = Calendar.getInstance();
        int hour = calendario.get(Calendar.HOUR_OF_DAY);
        int minutes = calendario.get(Calendar.MINUTE);
        String time = (hour + ":" + minutes);
        return time;
    }

    public static String stringToPath(String path) {
        String pending = "pending orders";
        String process = "in process orders";
        String closed = "closed orders";
        switch (path) {
            case "p":
                path = pending;
                break;
            case "i":
                path = process;
                break;
            case "c":
                path = closed;
                break;
        }
        return path;
    }

    public static void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
