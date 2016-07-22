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

    private String category;
    private String imgurl;
    private int votes;

    public static Post FromEncodedString(String compressed) {
        String[] separated = compressed.split(VALUE_SEPARATOR);

        String author      = separated.length > 0 ? separated[0] : "";
        String title       = separated.length > 1 ? separated[1] : "";
        String description = separated.length > 2 ? separated[2] : "";
        double latitude    = separated.length > 3 ? Double.parseDouble(separated[3]) : 0;
        double longitude   = separated.length > 4 ? Double.parseDouble(separated[4]) : 0;
        String category    = separated.length > 5 ? separated[5] : "";
        String imgurl      = separated.length > 6 ? separated[6] : "";
        int votes          = separated.length > 7 ? Integer.parseInt(separated[7]) : 0;

        return new Post(author, title, description, latitude, longitude, category, imgurl, votes);
    }

    public Post(String author, String title, String description, double latitude, double longitude,
                String category, String imgurl, int votes) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.imgurl = imgurl;
        this.votes = votes;
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

        builder.append(category);
        builder.append(VALUE_SEPARATOR);

        builder.append(imgurl);
        builder.append(VALUE_SEPARATOR);

        builder.append(votes);

        return builder.toString();
    }

    public boolean hasEmptyFields() {
        return title.length() == 0 || description.length() == 0;
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

    public String getCategory() {
        return category;
    }

    public String getImgurl() {
        return imgurl;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote(int votes) {
        this.votes += votes;
    }
}
