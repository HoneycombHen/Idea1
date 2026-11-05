public class Card {

    private String writer;
    private String recipient;
    private String body;
    private String imagePath;
    private String fontFamily;

    public Card(String recipient, String body, String writer, String imagePath, String fontFamily) {
        this.writer = writer;
        this.recipient = recipient;
        this.body = body;
        this.imagePath = imagePath;
        this.fontFamily = fontFamily;

        if (writer == null | recipient == null | body == null | fontFamily == null) {
            throw new IllegalArgumentException("Card initialization failed: non-Null parameter is required.");
        }
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

    public String getFontFamily() { return  fontFamily; }

    public String getFormattedText() {
        return "Dear " + recipient + ",\n"
                + body + "\n"
                + "Yours sincerely,\n"
                + writer + "\n";
    }

    public Card withFontFamily(String fontFamily) {
        return new Card(recipient, body, writer, imagePath, fontFamily);
    }


}
