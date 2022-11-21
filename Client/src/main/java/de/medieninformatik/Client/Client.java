package de.medieninformatik.Client;

import de.medieninformatik.Theatre.Reservierung;
import de.medieninformatik.Theatre.Theatre;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client extends Application {
    private static final double STAGE_WIDTH = 1000.0;
    private static final double STAGE_HEIGHT = 750.0;
    private static final double GRID_WIDTH = STAGE_WIDTH;
    private static final double GRID_HEIGHT = STAGE_HEIGHT - 200;
    private final double GRID_INSET_X = 10;
    private final double GRID_INSET_Y = 10;
    final double SPACE_TO_BOX_RATIO = 0.3;
    private Reservierung reservierung;
    private Stage stage;
    private GridPane pane;
    private Alert information;
    private Color free = Color.BLUE;
    private Color reserviert = Color.RED;
    private boolean showNames;

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param st the primary stage for this application, onto which
     *           the application scene can be set.
     *           Applications may create other stages, if needed, but they will not be
     *           primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage st) throws Exception {
        this.stage = st;
        GridPane gp = new GridPane();
        this.pane = gp;
        pane.setMinSize(GRID_WIDTH, GRID_HEIGHT);
        pane.setMaxSize(GRID_WIDTH, GRID_HEIGHT);
        pane.setAlignment(Pos.CENTER);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        try {
            reservierung = ReservierungFactory.getInstance();
        } catch (Exception e) {
            System.err.println("Keine Verbindung zum Server hergestellt.");
            System.exit(-1);
        }
        final VBox vbox = new VBox();
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        this.information = info;
        Button update = new Button("Update");
        Button mode = new Button("Reservieren");
        mode.setOnMouseClicked(e -> {
            if (showNames) {
                showNames = false;
                mode.setText("Reservieren");
            } else {
                showNames = true;
                mode.setText("Namen anzeigen");
            }
        });
        update.setOnAction(e -> {
            updateData();
            information.setContentText("Updated Data!");
            information.showAndWait();
        });
        vbox.getChildren().addAll(pane, update, mode);
        stage.setScene(new Scene(vbox, STAGE_WIDTH, STAGE_HEIGHT));
        stage.setTitle("Vorverkaufsstelle");
        stage.setResizable(false);
        updateData();
        stage.show();
    }

    private void updateData() {
        Theatre t = getServerData();
        updateGrid(t);
    }

    private void updateGrid(Theatre theatre) {
        final double BLOCK_AMOUNT_X = theatre.getColumns();
        final double BLOCK_AMOUNT_Y = theatre.getRows();
        final double SPACING_X = ((GRID_WIDTH - GRID_INSET_X) / BLOCK_AMOUNT_X) * SPACE_TO_BOX_RATIO;
        final double SPACING_Y = ((GRID_HEIGHT - GRID_INSET_Y) / BLOCK_AMOUNT_Y) * SPACE_TO_BOX_RATIO;
        final double BLOCK_SIZE_X = ((GRID_WIDTH - GRID_INSET_X) / BLOCK_AMOUNT_X) * (1 - SPACE_TO_BOX_RATIO);
        final double BLOCK_SIZE_Y = ((GRID_HEIGHT - GRID_INSET_Y) / BLOCK_AMOUNT_Y) * (1 - SPACE_TO_BOX_RATIO);

        for (int i = 0; i < BLOCK_AMOUNT_X; i++) {
            for (int j = 0; j < BLOCK_AMOUNT_Y; j++) {
                Theatre.Seat s = theatre.getSeat(i, j);
                Rectangle r = new Rectangle(BLOCK_SIZE_X, BLOCK_SIZE_Y, s.isBooked() ? reserviert : free);
                r.setOnMouseClicked((e) -> clickOnSeat(r, s));
                pane.add(r, i, j);
            }
        }
        pane.setHgap(SPACING_X);
        pane.setVgap(SPACING_Y);
    }

    private Theatre getServerData() {
        Theatre t = null;
        try {
            t = reservierung.updateTheatre();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return t;
    }

    private void clickOnSeat(Rectangle r, Theatre.Seat s) {
        if (showNames) {
            try {
                information.setContentText(s.isBooked() ? reservierung.getName(s.getRow(), s.getColumn()) : "Platz ist frei.");
            } catch (Exception e) {

            }
            information.showAndWait();
            return;
        }
        //Client nimmt Name an
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Name des Reservierenden");
        td.setHeaderText("Reservierender");
        td.showAndWait();
        if (td.getResult() == null || td.getResult().isBlank()) return;
        r.styleProperty().setValue("-fx-fill: #FF0000");
        //Auf Server reservieren
        try {
            String text;
            if (reservierung.reservieren(s.getRow(), s.getColumn(), td.getResult())) {
                text = "Reserviert.";
            } else {
                text = "Reservierung fehlgeschlagen.";
            }
            information.setContentText(text);
            information.showAndWait();
            updateData();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(Client.class, args);
    }
}
