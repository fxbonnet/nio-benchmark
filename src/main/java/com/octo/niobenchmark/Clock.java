package com.octo.niobenchmark;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Clock {
    private static volatile String time = getCurrentTime();
    private static Thread timer;
    static {
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        time = getCurrentTime();
                        Thread.sleep(Parameters.CLOCK_INTERVAL);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });
        timer.start();
    }

    public static String getTime() {
        return time;
    }

    private static String getCurrentTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

}
