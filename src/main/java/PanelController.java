import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelController implements Initializable{
    @FXML
    //TableView будет содержать в себе набор объектов типа FileInfo
            TableView<FileInfo> filesTable;

    @FXML
    ComboBox<String> disksBox;

    @FXML
    TextField pathField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //в этом столбцке будет храниться тип файла, он будет строковым
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>("Type");
        //создаём строковое свойство. Приходит один FileInfo, преобразуем его в значение данной ячейки
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(40);

        //создать столбец с именем файла
        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        //создать столбец с размером файла
        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            // отвечает за то как выглядит ячейка в столбце
            return new TableCell<FileInfo, Long>(){
                @Override
                //значение ячейки, пустая ячейка или нет
                protected void updateItem(Long item, boolean empty) {
                    //JavaFX будет пробегать по каждой строке и ориентируясь на эти данные будет что-то рисовать
                    super.updateItem(item, empty);
                    //если лонг не заполнен или ячейка пустая
                    if (item == null || empty) {
                        //то в ячейке ничего не будем писать и она никак не будет выглядеть
                        setText("");
                        setStyle("");
                    } else {
                        //берем числовое значение и добавляем разделитель и bytes. теперь сортировка будет правильной по зачению Long, а не как текст
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(120);

        //создать столбец с Датой измения файла
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Date");
        //Форматируем дату LocalDateTime через DateTimeFormatter
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDateColumn.setPrefWidth(120);

        // список столбцов таблицы. будет добавлены столбецы
        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDateColumn);
        //по умолчанию сортирует данные в первом столбце. Сначала папки, потом директории
        filesTable.getSortOrder().add(fileTypeColumn);

        disksBox.getItems().clear();
        //FileSystems предоставляет инфу о файловой системе. берем дефолтную файловую систему. запрашиваем список корневых директорий
        for (Path p: FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(p.toString());
        }
        //по умолчанию выбираем первую корневую директорию
        disksBox.getSelectionModel().select(0);


        //Paths.get- в Java NIO способ создания путей
        updateList(Paths.get("." + "/CatalogForTest"));
    }

    //метод собирает список файлов из любой директории и закидывает это в таблицу
    public void updateList(Path path) {

        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTable.getItems().clear();
            //File.list по указанному пути вернёт поток путей
            //полученную пачку путей преобразовываем к FileInfo. map в StreamAPI - преобразование данных.
            //берем каждый путь из потока list(path) и отдаем в конструктор FileInfo
            //получим список файлов в виде объектов FileInfo
            //полученный список файлов собираем в List и List отдаём в таблицу
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            //что бы табличка отсортировалась по умолчанию
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "о какой-то причине не удалось обновить список файлов", ButtonType.OK);
            //показать окно и подождать пока пользователь кликнет ОК
            alert.showAndWait();
        }

    }

    public void btnPathUpAction(ActionEvent actionEvent) {
        //берем строчку из pathField и по ней строим объект типа путь. И у него запрашиваем родителя
        Path upperPath = Paths.get(pathField.getText()).getParent();


    }
}
