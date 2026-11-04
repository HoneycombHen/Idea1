import java.io.File;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;

public class CardApp extends Application {

    // Remembers the chosen file while the app is running
    private File selectedImageFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label status = new Label("No image selected");
        status.setWrapText(true);

        Button chooseButton = new Button("Choose Image");
        Button loadButton = new Button("Load Image");

        ImageView preview = new ImageView();
        preview.setFitWidth(350);
        preview.setPreserveRatio(true);

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

        VBox layout = new VBox(10, chooseButton, loadButton, status, preview);
        Scene scene = new Scene(layout, 450, 500);

        stage.setTitle("Note Card");
        stage.setScene(scene);
        stage.show();
    }
}
