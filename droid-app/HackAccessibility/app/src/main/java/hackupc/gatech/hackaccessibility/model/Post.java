package hackupc.gatech.hackaccessibility.model;

/**
 * Created by michael on 7/21/16.
 */
public class Post {

    private static final String VALUE_SEPARATOR = "~!`!~";

    private String author;
    private String title;
    private String description;

    //private something image;

    private double latitude;
    private double longitude;

    public static Post FromEncodedString(String compressed) {
        String[] separated = compressed.split(VALUE_SEPARATOR);

        return new Post(
                separated[0],
                separated[1],
                separated[2],
                Double.parseDouble(separated[3]),
                Double.parseDouble(separated[4]));
    }

    public Post(String author, String title, String description, double latitude, double longitude) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        return (title + latitude + longitude).hashCode();
    }

    public String encodeToString() {
        StringBuilder builder = new StringBuilder();
        builder.append(author);
        builder.append(VALUE_SEPARATOR);

        builder.append(title);
        builder.append(VALUE_SEPARATOR);

        builder.append(description);
        builder.append(VALUE_SEPARATOR);

        builder.append(latitude);
        builder.append(VALUE_SEPARATOR);

        builder.append(longitude);
        builder.append(VALUE_SEPARATOR);

        return builder.toString();
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
