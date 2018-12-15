package com.example.rkhan2019.lab5p1;

public class counter {
    private static int createClick = 0;
    private static int startClick = 0;
    private static int resumeClick = 0;
    private static int pauseClick =0;
    private static int stopClick =0;
    private static int restartClick=0;
    private static int destroyClick=0;

    public static void increment(String s){
        switch(s){
            case "create":
                createClick++;
                break;
            case "start":
                startClick++;
                break;
            case "resume":
                resumeClick++;
                break;
            case "pause":
                pauseClick++;
                break;
            case "stop":
                stopClick++;
                break;
            case "restart":
                restartClick++;
                break;
            case "destroy":
                destroyClick++;
                break;
        }
    }

    public static int getCreateClick() {
        return createClick;
    }

    public static int getStartClick() {
        return startClick;
    }

    public static int getResumeClick() {
        return resumeClick;
    }

    public static int getPauseClick() {
        return pauseClick;
    }

    public static int getStopClick() {
        return stopClick;
    }

    public static int getRestartClick() {
        return restartClick;
    }

    public static int getDestroyClick() {
        return destroyClick;
    }
}
