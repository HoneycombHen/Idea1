import java.io.IOException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class CardGenerator {

    // Generates a card for a person. It includes notes, a picture
    // and other things usually found in a note card for someone

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Start with dear ...
        System.out.print("Input who you are writing to here: ");

        String recepient = scanner.nextLine();

        System.out.print("\n");

        // Body of the note card
        System.out.print("Input your note body here: ");
        String noteBody = scanner.nextLine();

        System.out.print("\n");

        // End with your's truly ...
        System.out.print("Input your name here: ");
        String writer = scanner.nextLine();

        System.out.print("Dear " + recepient + ", \n");
        System.out.print(noteBody + "\n");
        System.out.print("Yours truly, \n" + writer);

        // Get the path to an image

        System.out.print("\nEnter the image path: ");
        String imagePath = scanner.nextLine().trim();

        try {
            if (imagePath.isBlank()) {
                throw new IOException("No image path was entered.");
            }

            Path path = Path.of(imagePath);
            System.out.println("Checking: " + path.toAbsolutePath());

            if (!Files.isRegularFile(path)) {
                throw new IOException("No file found at that path.");
            }

            BufferedImage image = ImageIO.read(path.toFile());
            if (image == null) {
                throw new IOException("The file could not be read as an image.");
            }

            System.out.println("Image loaded: "
                    + image.getWidth() + " × " + image.getHeight() + " pixels");

        } catch (InvalidPathException | IOException e) {
            System.out.println("Could not load image: " + e.getMessage());
        }

        // Close scanner
        scanner.close();

    }
}
