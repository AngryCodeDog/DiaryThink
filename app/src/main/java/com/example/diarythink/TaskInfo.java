package com.example.diarythink;

/**
 * Created by zhuyupei on 2017/9/21 0021.
 */

public class TaskInfo {
    private boolean complete = false;
    private String taskDescripe = "";

    public TaskInfo(boolean complete, String taskDescripe) {
        this.complete = complete;
        this.taskDescripe = taskDescripe;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        complete = complete;
    }

    public String getTaskDescripe() {
        return taskDescripe;
    }

    public void setTaskDescripe(String taskDescripe) {
        this.taskDescripe = taskDescripe;
    }
}
