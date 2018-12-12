package io.github.gravetii.controller;

import io.github.gravetii.game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static io.github.gravetii.controller.GameResultDisplayer.TableResult;

public class GameResultController implements FxController {

  private final Game game;
  private final GameGridController ref;
  private GameResultDisplayer displayer;

  @FXML private TableView<TableResult> tblDisplay;
  @FXML private TableColumn<TableResult, Integer> idTblCol;
  @FXML private TableColumn<TableResult, String> wordTblCol;
  @FXML private TableColumn<TableResult, Integer> pointsTblCol;

  public GameResultController(Game game, GameGridController ref) {
    this.game = game;
    this.ref = ref;
  }

  @FXML
  public void initialize() {
    this.idTblCol.prefWidthProperty().bind(tblDisplay.widthProperty().divide(4));
    this.wordTblCol.prefWidthProperty().bind(tblDisplay.widthProperty().divide(2));
    this.pointsTblCol.prefWidthProperty().bind(tblDisplay.widthProperty().divide(4));
    this.idTblCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    this.wordTblCol.setCellValueFactory(new PropertyValueFactory<>("word"));
    this.pointsTblCol.setCellValueFactory(new PropertyValueFactory<>("score"));
    this.tblDisplay.setRowFactory(
        callback -> {
          TableRow<TableResult> row = new TableRow<>();
          row.setOnMouseClicked(
              event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                  TableResult result = row.getItem();
                  String word = result.getWord();
                  if (result.isByUser()) {
                    this.ref.revisitUserWord(word);
                  } else {
                    this.ref.revisitGameWord(word);
                  }
                }
              });

          return row;
        });

    this.displayer = new GameResultDisplayer(this.tblDisplay);
  }

  @FXML
  public void onGoBtnClick(ActionEvent event) {
    this.ref
        .validateWordOnBtnClick()
        .ifPresent(
            result -> {
              this.displayer.showUserWord(result);
            });
  }

  private void displayGameWords() {
    this.game
        .result()
        .all()
        .forEach(
            (word, result) -> {
              this.displayer.showGameWord(result);
            });
  }

  public void onGameEnd() {
    this.displayGameWords();
  }
}
