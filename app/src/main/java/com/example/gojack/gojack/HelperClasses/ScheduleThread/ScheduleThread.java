package com.example.gojack.gojack.HelperClasses.ScheduleThread;

import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Im033 on 4/19/2017.
 */

public class ScheduleThread {
    private TimerInterface timerInterface;
    private int delayTime = 20000, initialDelayTime = 1000;
    private Activity activity;
    private boolean status = false;

    public ScheduleThread(TimerInterface timerInterface, Activity activity) {
        this.timerInterface = timerInterface;
        this.activity = activity;
    }

    public ScheduleThread(TimerInterface timerInterface, int delayTime, Activity activity) {
        this.timerInterface = timerInterface;
        this.delayTime = delayTime;
        this.activity = activity;
    }

    public ScheduleThread(TimerInterface timerInterface, int delayTime, int initialDelayTime, Activity activity) {
        this.timerInterface = timerInterface;
        this.delayTime = delayTime;
        this.initialDelayTime = initialDelayTime;
        this.activity = activity;
    }

    private Timer timer = new Timer();

    public boolean isCanceled() {
        return status;
    }

    public void start() {
        if (!status) {
            status = true;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerInterface.onRun();
                        }
                    });
                }
            }, initialDelayTime, delayTime);
        }
    }

    public void stop() {
        status = false;
        timer.cancel();
    }
}
