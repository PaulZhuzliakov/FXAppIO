import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML
    //TableView будет содержать в себе набор объектов типа FileInfo
    TableView<FileInfo> filesTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //в этом столбцке будет храниться тип файла, он будет строковым
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>("Type");
        //создаём строковое свойство. Приходит один FileInfo, преобразуем его в значение данной ячейки
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(120);

        //создаем еще один столбец
        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        // список столбцов таблицы. будет добавлены столбецы
        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn);
        //о умолчанию сортирует данные в первом столбце. Сначала папки, потом директории
        filesTable.getSortOrder().add(fileTypeColumn);



        updateList(Paths.get("."));
    }

    //метод собирает список файлов из любой директории и закидывает это в таблицу
    public void updateList(Path path) {
        //File.list по указанному пути вернёт поток путей/ по указанному пути получили поток pass`ов
        //filesTable.getItems().addAll(Files.list(path));
        //полученную пачку путей преобразовываем к FileInfo. map в StreamAPI - реобразование данных.
        //берем каждый путь из потока list(path) и отдаем в конструктор FileInfo
        //получим список файлов в виде объектов FileInfo
        //filesTable.getItems().addAll(Files.list(path).map(FileInfo::new));
        //полученный список файлов собираем в List и List отдаём в таблицу
        try {
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            //что бы табличка отсортировалась по умолчанию
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "о какой-то причине не удалось обновить список файлов", ButtonType.OK);
            //показать окно и подождать пока пользователь кликнет ОК
            alert.showAndWait();
        }

    }
    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

}
