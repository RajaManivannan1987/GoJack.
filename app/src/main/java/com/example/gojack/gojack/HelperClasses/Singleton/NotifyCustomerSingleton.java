package com.example.gojack.gojack.HelperClasses.Singleton;

import com.example.gojack.gojack.HelperClasses.Interface.ActionCompleted;

/**
 * Created by IM0033 on 8/19/2016.
 */
public class NotifyCustomerSingleton {
    private ActionCompleted actionCompleted;
    private static NotifyCustomerSingleton completedSingleton = new NotifyCustomerSingleton();
    private static NotifyCustomerSingleton cancelNotify = new NotifyCustomerSingleton();
    private static NotifyCustomerSingleton closeActivity = new NotifyCustomerSingleton();

    public static NotifyCustomerSingleton actionCompletedSingleton() {
        return completedSingleton;
    }

    public static NotifyCustomerSingleton actionCanceled() {
        return cancelNotify;
    }

    public static NotifyCustomerSingleton closActivity() {
        return closeActivity;
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
