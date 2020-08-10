import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller{

    //ривязка панелей в Controller`е
    @FXML
    VBox leftPanel, rightPanel;

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void copyBtnAction(ActionEvent actionEvent) {
        //из левой и правой панелей получаем ссылки на контроллеры этих панелей
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");

        if (leftPC.getSelectedFileName() == null && rightPC.getSelectedFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ни один файл не был выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        //srcPC- PanelController, в котором был выбран файл. destPC - куда копировать
        PanelController srcPC = null, destPC = null;
        //назначаем панель откуда копируем и панель куда конируем
        if (leftPC.getSelectedFileName() != null) {
            srcPC = leftPC;
            destPC = rightPC;
        }
        if (rightPC.getSelectedFileName() != null) {
            srcPC = rightPC;
            destPC = leftPC;
        }
        //берем нанель, запрашиваем путь и запрашиваем выбранный файл
        Path scrPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path destPath = Paths.get(destPC.getCurrentPath()).resolve(scrPath.getFileName().toString());

        try {
            Files.copy(scrPath, destPath);
            //после копирования файл должен отобразиться на панели назначения. Обновление destPC
            destPC.updateList(Paths.get(destPC.getCurrentPath()));
        } catch (IOException e) {
            //если файл уже существует, то показывается Alert
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось скопировать указанный файл. Файл уже существует", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
