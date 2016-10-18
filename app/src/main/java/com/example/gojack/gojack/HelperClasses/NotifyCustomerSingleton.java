package com.example.gojack.gojack.HelperClasses;

import com.example.gojack.gojack.Interface.ActionCompleted;

/**
 * Created by IM0033 on 8/19/2016.
 */
public class NotifyCustomerSingleton {
    private ActionCompleted actionCompleted;
    private static NotifyCustomerSingleton completedSingleton = new NotifyCustomerSingleton();

    public static NotifyCustomerSingleton actionCompletedSingleton() {
        return completedSingleton;
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
