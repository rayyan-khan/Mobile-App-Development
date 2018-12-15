package com.example.rkhan2019.lab5p2;

public class counter {

    private static int createClick;
    private static int startClick;
    private static int resumeClick;
    private static int pauseClick;
    private static int stopClick;
    private static int restartClick;
    private static int destroyClick;


    public counter(){
        createClick = 0;
        startClick = 0;
        resumeClick = 0;
        pauseClick = 0;
        stopClick = 0;
        restartClick = 0;
        destroyClick = 0;
    }
    public void increment(String s){
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

    public int getCreateClick() {
        return createClick;
    }

    public int getStartClick() {
        return startClick;
    }

    public int getResumeClick() {
        return resumeClick;
    }

    public int getPauseClick() {
        return pauseClick;
    }

    public int getStopClick() {
        return stopClick;
    }

    public int getRestartClick() {
        return restartClick;
    }

    public int getDestroyClick() {
        return destroyClick;
    }
}
