import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;

public class CardApp extends Application {

    // Remembers the chosen file while the app is running
    private File selectedImageFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label status = new Label("No image selected");
        Label cardLabel = new Label("Fill up the card");
        cardLabel.setMinHeight(Region.USE_PREF_SIZE);
        cardLabel.setMaxWidth(Double.MAX_VALUE);
        status.setWrapText(true);

        Button chooseButton = new Button("Choose Image");
        Button loadButton = new Button("Load Image");
        Button loadCardButton = new Button("Load Card");
        Button saveButton = new  Button("Save as PNG");

        ImageView preview = new ImageView();
        preview.setFitWidth(350);
        preview.setPreserveRatio(true);

        TextField recipientField = new TextField();
        recipientField.setMaxWidth(200);
        TextArea bodyField = new TextArea();
        bodyField.setMaxWidth(200);
        bodyField.setWrapText(true);
        bodyField.setPrefRowCount(2);
        TextField writerField = new TextField();
        writerField.setMaxWidth(200);

        // load card button
        loadCardButton.setOnAction(event -> {
            String recipient = recipientField.getText();
            String body = bodyField.getText();
            String writer = writerField.getText();
            Card card = new Card(recipient, body, writer, "placeholderPath");
            String cardFormattedText = card.getFormattedText();

            cardLabel.setText(cardFormattedText);
        });

        cardLabel.setWrapText(true);


        // choose file button
        chooseButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose an image");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Image files", "*.png", "*.jpg", "*.jpeg"
                    )
            );

            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                selectedImageFile = file;
                status.setText("Selected: " + file.getAbsolutePath());
            }
        });

        // load button stuff
        loadButton.setOnAction(event -> {
            if (selectedImageFile == null) {
                status.setText("Choose an image first.");
                return;
            }

            Image image = new Image(selectedImageFile.toURI().toString());

            if (image.isError()) {
                status.setText("Could not load that image.");
                return;
            }

            preview.setImage(image);
            status.setText("Image loaded: " + selectedImageFile.getName());
        });

        HBox hBox = new HBox(10, preview, cardLabel);
        HBox.getHgrow(cardLabel);
        VBox layout = new VBox(10, recipientField, bodyField, writerField, loadCardButton,
                chooseButton, loadButton, saveButton, hBox);

        // setup snapshot for PNG save
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.WHITE);


        saveButton.setOnAction(event -> {
            WritableImage snapshot = hBox.snapshot(params, null);
            BufferedImage buffered = SwingFXUtils.fromFXImage(snapshot, null);
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose a png file");
            chooser.setInitialFileName("card.png");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "png files", "*.png")
            );
            File pngFile = chooser.showSaveDialog(stage);
            try {
                ImageIO.write(buffered, "png", pngFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });




        preview.fitWidthProperty().bind(layout.widthProperty().multiply(0.5));
        preview.fitHeightProperty().bind(layout.heightProperty().multiply(0.4));
        preview.setPreserveRatio(true);


        Scene scene = new Scene(layout, 450, 500);

        stage.setTitle("Note Card");
        stage.setScene(scene);
        stage.show();
    }
}
