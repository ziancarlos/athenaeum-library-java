package tools;

import java.util.Stack;

import javafx.event.ActionEvent;

public class BackBtnTools {
    static Stack<String> backBtnStack = new Stack<>();

    public static void addToBackBtnStack(String scene) {
        backBtnStack.push(scene);
    }

    public static String getLastScene() {
        return backBtnStack.pop();
    }

    public static boolean isBackBtnStackEmpty() {
        return backBtnStack.empty();
    }

    public static void clearBackBtnStack() {
        backBtnStack.clear();
    }

    public static void backBtnActionEvent(ActionEvent event) {
        if (!isBackBtnStackEmpty()) {
            SwitchSceneTools.changeSceneActionEvent(event, getLastScene());
        }
    }
}
