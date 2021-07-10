package ir.ac.kntu.services;

import javafx.application.Platform;

import java.time.DateTimeException;

public class CountDownTimer {

    private final int minute;

    private final int second;

    private OnTimerFinishListener onTimerFinishListener;

    private OnTimerTickListener onTimerTickListener;

    private boolean canRestart = true;

    private boolean stop = false;

    private int initialDelaySeconds;

    private Thread thread;

    public CountDownTimer(int minute, int second) {
        checkTime(minute, second);
        this.minute = minute;
        this.second = second;
    }

    public void setOnTimerFinishListener(OnTimerFinishListener onTimerFinishListener) {
        this.onTimerFinishListener = onTimerFinishListener;
    }

    public void setOnTimerTickListener(OnTimerTickListener onTimerTickListener) {
        this.onTimerTickListener = onTimerTickListener;
    }

    public void setInitialDelaySeconds(int initialDelaySeconds) {
        this.initialDelaySeconds = initialDelaySeconds;
    }

    public void start() {
        if (!canRestart) {
            return;
        }
        thread = new Thread(() -> {
            if (stop) {
                return;
            }
            canRestart = false;
            initialDelay();
            int totalSeconds = convertTimeToSeconds(minute, second);
            for (int i = totalSeconds; i >= 0; i--) {
                if (stop) {
                    return;
                }
                try {
                    tick(i);
                } catch (InterruptedException e) {
                    return;
                }
            }
            canRestart = true;
            Platform.runLater(() -> {
                if (onTimerFinishListener != null) {
                    onTimerFinishListener.onFinish();
                }
            });
        });
        thread.start();
    }

    public void stop() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
        stop = true;
    }

    private int convertTimeToSeconds(int minute, int second) {
        return minute * 60 + second;
    }

    private void checkTime(int minute, int second) {
        if (minute < 0 || minute > 59 || second < 0 || second > 59) {
            throw new DateTimeException("Invalid Time");
        }
    }

    private void initialDelay() {
        for (int i = 1; i <= initialDelaySeconds; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tick(int i) throws InterruptedException {
        Thread.sleep(1000);
        int curMinute = i / 60;
        int curSecond = i % 60;
        Platform.runLater(() -> {
            if (onTimerTickListener != null) {
                onTimerTickListener.onTick(curMinute, curSecond);
            }
        });
    }
}
