package hackupc.gatech.hackaccessibility.model;

/**
 * Created by michael on 7/21/16.
 */
public class Post {

    private String author;
    private String title;
    private String description;

    //private something image;

    private double latitude;
    private double longitude;


    public Post(String author, String title, String description, double latitude, double longitude) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
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
