package com.mine.common.fx;

import com.mine.common.exception.ExceptionUtil;
import com.mine.common.exception.MyException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

/**
 * @author : zyb
 * 时间 : 2022/3/13 21:18
 */
public class JavaFxUtil {


    /**
     * 文件选择器
     *
     * @param primaryStage
     * @return
     */
    public static TextField createFileChoose(Stage primaryStage, GridPane g, String title, String description, String extensions, String buttonTxt) {
        TextField textField = new TextField();
        textField.setPrefWidth(500);
        g.add(textField, 1, 0);
        Button btn = new Button();
        btn.setText(buttonTxt);
        btn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(title);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extensions));
            String path = fileChooser.showOpenDialog(primaryStage).getPath();
            textField.setText(path);
        });
        g.add(btn, 2, 0);
        return textField;
    }


    /**
     * 任务执行
     * 进度条
     * 错误信息展示
     */
    public static void execute(Stage primaryStage, Runnable runnable, ProgressBar p, Label resultLabel, Button executeButton) {
        p.setVisible(true);
        p.setDisable(false);
        Task task = new Task<Object>() {
            @Override
            protected Object call() {
                Exception ex = null;
                try {
                    runnable.run();
                    System.gc();
                } catch (Exception e) {
                    ex = e;
                    e.printStackTrace();
                }

                Exception exx = ex;
                Platform.runLater(() -> {
                    resultLabel.setVisible(true);
                    executeButton.setDisable(false);
                    if (exx == null) {
                        resultLabel.setText("   执行完成！");
                    } else {
                        StringBuilder exceptionInfo = null;
                        try {
                            exceptionInfo = getExceptionInfo(exx);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (exceptionInfo == null) {
                            exceptionInfo = new StringBuilder("异常信息生成失败");
                        }
                        resultLabel.setText(exceptionInfo.toString());
                    }
                    //刷新界面
                    primaryStage.hide();
                    primaryStage.show();
                });
                resultLabel.setVisible(true);
                p.setVisible(false);
                p.setDisable(true);
                return null;
            }
        };

        p.progressProperty().bind(task.progressProperty());
        new Thread(() -> task.run()).start();
    }


    private static StringBuilder getExceptionInfo(Exception exx) throws ClassNotFoundException {
        String black = "\n       ";
        StringBuilder exceptionInfo = new StringBuilder();
        exceptionInfo.append("运行异常:");
        if (exx instanceof MyException) {
            exceptionInfo.append(black);
            exceptionInfo.append("异常信息: " + ((MyException) exx).getErrorMessage());
            if (((MyException) exx).getE() != null) {
                exx = ((MyException) exx).getE();
            }
        }
        String errorMessage = exx instanceof NullPointerException ? "空指针异常" :
                exx instanceof FileNotFoundException ? "文件找不到:" + exx.getMessage() : exx.getMessage();
        exceptionInfo.append(black);
        exceptionInfo.append("异常类型: " + errorMessage);

        StringBuilder userStackTraceInfo = ExceptionUtil.getUserStackTraceInfo(exx);
        exceptionInfo.append(black);
        exceptionInfo.append("异常执行流程:");
        exceptionInfo.append(userStackTraceInfo);

        StringBuilder stackTraceInfo = ExceptionUtil.getStackTraceInfo(exx);
        exceptionInfo.append(black);
        exceptionInfo.append("异常代码信息: ");
        exceptionInfo.append(stackTraceInfo);
        return exceptionInfo;
    }
}
