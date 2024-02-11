
package Task6.Controller;

import Task6.Database.DatabaseHandler;
import Task6.Model.ImageData;

public class Connector {
    public void saveImageToDatabase(String imageUrl, byte[] imageData) {
        // Create an ImageData object
        ImageData image = new ImageData();
        image.setImageUrl(imageUrl);
        image.setImageData(imageData);

        // Save the image to the database
        DatabaseHandler.saveImage(image);
    }

    public byte[] getImageFromDatabase(String imageUrl) {
        // Retrieve the image data from the database
        return DatabaseHandler.getImageData(imageUrl);
    }
}
