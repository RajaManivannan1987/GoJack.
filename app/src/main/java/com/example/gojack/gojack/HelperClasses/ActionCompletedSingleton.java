package com.example.gojack.gojack.HelperClasses;

import com.example.gojack.gojack.Interface.ActionCompleted;

/**
 * Created by IM0033 on 8/19/2016.
 */
public class ActionCompletedSingleton {
    private static ActionCompletedSingleton completedSingleton = new ActionCompletedSingleton();
    private static ActionCompletedSingleton actionCompletedSingleton = new ActionCompletedSingleton();
    private ActionCompleted actionCompleted;

    public static ActionCompletedSingleton actionCompletedSingleton() {
        return completedSingleton;
    }

    public static ActionCompletedSingleton getActionCompletedSingleton() {
        return actionCompletedSingleton;
    }

    public ActionCompletedSingleton() {

    }

    public void setListener(ActionCompleted completed) {
        actionCompleted = completed;
    }

    public void ActionCompleted() {
        if (actionCompleted != null) {
            actionCompleted.actionCompleted();
        }
    }

}
