package tools;

import java.util.Stack;

import javafx.event.ActionEvent;

public class BackBtnTools {
    static Stack<String> backBtnStack = new Stack<>();

    /**
     * add the current scene to the back button stack
     * 
     * @param scene the current scene
     */
    public static void addToBackBtnStack(String scene) {
        backBtnStack.push(scene);

        System.out.println(backBtnStack);
    }

    /**
     * get the previous scene from the back button stack and pop it
     *
     * @return the previous scene
     */
    public static String getLastScene() {
        return backBtnStack.pop();
    }

    /**
     * check if the back button stack is empty
     *
     * @return boolean if the stack is empty
     */
    public static boolean isBackBtnStackEmpty() {
        return backBtnStack.empty();
    }

    /**
     * clear the back button stack
     */
    public static void clearBackBtnStack() {
        backBtnStack.clear();
    }

    /**
     * change the scene to the previous scene
     *
     * @param event the event of the scene
     */
    public static void backBtnActionEvent(ActionEvent event) {

        SwitchSceneTools.changeSceneActionEvent(event, getLastScene());

        System.out.println(backBtnStack);
    }
}
