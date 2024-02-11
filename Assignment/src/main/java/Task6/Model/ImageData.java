package Task6.Model;

public class ImageData {
    private int id;
    private String imageUrl;
    private byte[] imageData; // Assuming image data might be stored as a byte array

    // Constructor
    public ImageData() {
    }

    public ImageData(String imageUrl, byte[] imageData) {
        this.imageUrl = imageUrl;
        this.imageData = imageData;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
