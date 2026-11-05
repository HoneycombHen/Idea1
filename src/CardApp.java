import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;

public class CardApp extends Application {

    // Remembers the chosen file while the app is running
    private File selectedImageFile;
    private String selectedImagePath;
    private Card currentCard;   // null until the user loads a card

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
        status.setWrapText(true);

        Label cardLabel = new Label("Fill up the card");
        cardLabel.setMinHeight(Region.USE_PREF_SIZE);
        cardLabel.setMinWidth(LABEL_WIDTH);
        cardLabel.setMaxWidth(Double.MAX_VALUE);
        cardLabel.setWrapText(true);


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


        ComboBox<String> fontPicker = new ComboBox<>();
        fontPicker.getItems().addAll(Font.getFamilies());   // fill the list
        fontPicker.setValue("Arial");

        // fontPicker updates cardLabel font on drop down action
        fontPicker.setOnAction(event -> {
            if (currentCard == null) {
                return;
            }
            String fontFamily = fontPicker.getValue();
            currentCard = currentCard.withFontFamily(fontFamily);
            cardLabel.setFont(Font.font(fontFamily, 16));
        });

        // loadCardButton sets text for cardLabel on action
        loadCardButton.setOnAction(event -> {
            String recipient = recipientField.getText();
            String body = bodyField.getText();
            String writer = writerField.getText();
            String fontFamily = fontPicker.getValue();
            try {
                currentCard = new Card(recipient, body, writer, selectedImagePath, fontFamily);
                String cardFormattedText = currentCard.getFormattedText();
                cardLabel.setText(cardFormattedText);
            }
            catch (IllegalArgumentException e) {
                status.setText(e.getMessage());
            }

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
                selectedImagePath = selectedImageFile.getAbsolutePath();
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

        // Layout containers
        HBox cardView = new HBox(SPACING, imageView, cardLabel);
        HBox.setHgrow(cardLabel, Priority.ALWAYS);

        VBox root = new VBox(SPACING, recipientField, bodyField, writerField,
                fontPicker, loadCardButton,
                status, chooseButton, loadImageButton, saveButton, cardView);

        // imageView scaled to root
        imageView.fitWidthProperty().bind(root.widthProperty().multiply(IMAGE_WIDTH_SHARE));
        imageView.fitHeightProperty().bind(root.heightProperty().multiply(IMAGE_HEIGHT_SHARE));
        imageView.setPreserveRatio(true);

        // save the card as a PNG
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


        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Note Card");
        stage.setScene(scene);
        stage.show();
    }
}
