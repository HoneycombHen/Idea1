public class Card {

    private String writer;
    private String recipient;
    private String body;
    private String imagePath;

    public Card(String recipient, String body, String writer, String imagePath) {
        this.writer = writer;
        this.recipient = recipient;
        this.body = body;
        this.imagePath = imagePath;
    }

    public String getWriter() {
        return writer;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getBody() {
        return body;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getFormattedText() {
        return "Dear " + recipient + ",\n"
                + body + "\n"
                + "Your's sincerely,\n"
                + writer + "\n";
    }
}
