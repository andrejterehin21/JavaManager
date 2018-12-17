package andrej;

import com.company.ArrayTaskList;
import com.company.Task;
import com.company.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Controller {

    private static TaskList taskList;

    @FXML
    private VBox main;
    @FXML
    private Pane newList;
    @FXML
    private TextField listTitle;
    @FXML
    private ListView<Task> Display;
    @FXML
    private TextField hours;
    @FXML
    private TextField minutes;
    @FXML
    private TextField seconds;
    @FXML
    private TextField taskTitle;
    @FXML
    private TextArea taskDescription;
    @FXML
    private CheckBox repeat;
    @FXML
    private DatePicker start;
    @FXML
    private DatePicker end;
    @FXML
    private Button newTask;
    @FXML
    private AnchorPane taskPane;
    @FXML
    private Label ListTitle;

    public void newTaskList(ActionEvent actionEvent)  throws Exception {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("New Tasklist");
        Pane pane = FXMLLoader.load(getClass().getResource("newList.fxml"));
        stage.setScene(new Scene(pane));
        stage.showAndWait();
        newTask.setDisable(false);
        ListTitle.setText(taskList.getTitle());
        ObservableList<Task> display = FXCollections.observableArrayList(((ArrayTaskList)taskList).getArray());
        Display.setItems(display);
    }

    public void saveTasklist(ActionEvent actionEvent){
        taskList = new ArrayTaskList();
        taskList.setTitle(listTitle.getText());
        ((Stage)newList.getScene().getWindow()).close();
    }

    public void openTasklist(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File row = fileChooser.showOpenDialog(stage);
        FileInputStream file = new FileInputStream(row);
        ObjectInputStream in = new ObjectInputStream(file);
        taskList = (ArrayTaskList)in.readObject();
        in.close();
        ListTitle.setText(taskList.getTitle());
        newTask.setDisable(false);
        ObservableList<Task> display = FXCollections.observableArrayList(((ArrayTaskList)taskList).getArray());
        Display.setItems(display);
    }

    public void saveToFile(ActionEvent actionEvent) throws Exception {
        FileOutputStream file = new FileOutputStream(taskList.getTitle()+".tskl");
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(taskList);
        out.close();
    }

    public void renew(MouseEvent mouseEvent) {

    }

    public void newTask(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("New Task...");
        Pane pane = FXMLLoader.load(getClass().getResource("newTask.fxml"));
        NumberString converter = new NumberString();
        final TextFormatter<Number> formatter1 = new TextFormatter<>(
                converter,
                0,
                converter.getFilter()
        );
        final TextFormatter<Number> formatter2 = new TextFormatter<>(
                converter,
                0,
                converter.getFilter()
        );
        final TextFormatter<Number> formatter3 = new TextFormatter<>(
                converter,
                0,
                converter.getFilter()
        );
        ((TextField)pane.getChildren().get(4)).setTextFormatter(formatter1);
        ((TextField)pane.getChildren().get(5)).setTextFormatter(formatter2);
        ((TextField)pane.getChildren().get(6)).setTextFormatter(formatter3);
        pane.getChildren().get(3).setDisable(true);
        pane.getChildren().get(4).setDisable(true);
        pane.getChildren().get(5).setDisable(true);
        pane.getChildren().get(6).setDisable(true);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    public void addTask(ActionEvent actionEvent) {
        LocalDate slocalDate = start.getValue();
        Instant sinstant = Instant.from(slocalDate.atStartOfDay(ZoneId.systemDefault()));
        Date sdate = Date.from(sinstant);

        if(!repeat.isSelected()) {
            Task task = new Task(taskTitle.getText(), sdate);
            task.setActive(true);
            taskList.add(task);
        }else{

            LocalDate elocalDate = end.getValue();
            Instant einstant = Instant.from(elocalDate.atStartOfDay(ZoneId.systemDefault()));
            Date edate = Date.from(einstant);

            Task task = new Task(taskTitle.getText(), sdate, edate,
                    new Date(0,0,0,
                            Integer.parseUnsignedInt(hours.getText()),
                            Integer.parseUnsignedInt(minutes.getText()),
                            Integer.parseUnsignedInt(seconds.getText())
                    )
            );
            task.setActive(true);
            String description = taskDescription.getText();
            if(description != null && !description.isEmpty()){
                task.setDescription(description);
            }
                System.out.println(task);
                System.out.println(taskList == null);
                taskList.add(task);
        }
        ((Stage)taskPane.getScene().getWindow()).close();
    }

    public void repeat(ActionEvent actionEvent) {
       if (repeat.isSelected()){
           hours.setDisable(false);
           minutes.setDisable(false);
           seconds.setDisable(false);
            end.setDisable(false);
       } else {
           hours.setDisable(true);
           minutes.setDisable(true);
           seconds.setDisable(true);
           end.setDisable(true);
       }
    }
}

