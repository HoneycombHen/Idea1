import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private static final int FIELD_WIDTH = 200;
    private static final int LABEL_WIDTH = 150;
    private static final int SPACING = 10;
    private static final double IMAGE_WIDTH_SHARE = 0.5;
    private static final double IMAGE_HEIGHT_SHARE = 0.4;
    private static final int WINDOW_WIDTH = 450;
    private static final int WINDOW_HEIGHT = 500;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label status = new Label("No image selected");
        Label cardLabel = new Label("Fill up the card");
        cardLabel.setMinHeight(Region.USE_PREF_SIZE);
        cardLabel.setMinWidth(LABEL_WIDTH);
        cardLabel.setMaxWidth(Double.MAX_VALUE);
        cardLabel.setWrapText(true);
        status.setWrapText(true);

        Button chooseButton = new Button("Choose Image");
        Button loadImageButton = new Button("Load Image");
        Button loadCardButton = new Button("Load Card");
        Button saveButton = new  Button("Save as PNG");

        ImageView imageView = new ImageView();

        TextField recipientField = new TextField();
        recipientField.setMaxWidth(FIELD_WIDTH);
        TextArea bodyField = new TextArea();
        bodyField.setMaxWidth(FIELD_WIDTH);
        bodyField.setWrapText(true);
        bodyField.setPrefRowCount(2);
        TextField writerField = new TextField();
        writerField.setMaxWidth(FIELD_WIDTH);

        // load card button
        loadCardButton.setOnAction(event -> {
            String recipient = recipientField.getText();
            String body = bodyField.getText();
            String writer = writerField.getText();
            Card card = new Card(recipient, body, writer, "placeholderPath");
            String cardFormattedText = card.getFormattedText();

            cardLabel.setText(cardFormattedText);
        });

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

        // Load image into card preview
        loadImageButton.setOnAction(event -> {
            if (selectedImageFile == null) {
                status.setText("Choose an image first.");
                return;
            }

            Image image = new Image(selectedImageFile.toURI().toString());

            if (image.isError()) {
                status.setText("Could not load that image.");
                return;
            }

            imageView.setImage(image);
            status.setText("Image loaded: " + selectedImageFile.getName());
        });

        HBox cardView = new HBox(SPACING, imageView, cardLabel);
        HBox.setHgrow(cardLabel, Priority.ALWAYS);


        VBox root = new VBox(SPACING, recipientField, bodyField, writerField, loadCardButton,
                status, chooseButton, loadImageButton, saveButton, cardView);

        // setup snapshot for PNG save
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.WHITE);


        saveButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "png files", "*.png")
            );
            chooser.setTitle("Choose a png file");
            chooser.setInitialFileName("card.png");
            File pngFile = chooser.showSaveDialog(stage);
            if (pngFile == null) {
                return;
            }

            WritableImage snapshot = cardView.snapshot(params, null);
            BufferedImage buffered = SwingFXUtils.fromFXImage(snapshot, null);
            try {
                ImageIO.write(buffered, "png", pngFile);
                status.setText("Saved");
            } catch (IOException e) {
                status.setText("Could not save: " + e.getMessage());
            }
        });


        imageView.fitWidthProperty().bind(root.widthProperty().multiply(IMAGE_WIDTH_SHARE));
        imageView.fitHeightProperty().bind(root.heightProperty().multiply(IMAGE_HEIGHT_SHARE));
        imageView.setPreserveRatio(true);


        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Note Card");
        stage.setScene(scene);
        stage.show();
    }
}
