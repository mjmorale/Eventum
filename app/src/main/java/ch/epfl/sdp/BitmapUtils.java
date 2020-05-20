package ch.epfl.sdp;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Utility class that provides bitmap processing operations
 */
public class BitmapUtils {

    /**
     * Crops the given bitmap to a square and scales it to a given size. The square is centered.
     *
     * @param input The input image to process.
     * @param size The size of the output square.
     * @return The cropped image.
     */
    public static Bitmap cropToScaledSquare(@NonNull Bitmap input, int size) {
        verifyNotNull(input);
        if(size <= 0) {
            throw new IllegalArgumentException("Size should be greater than 0");
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        if(inputWidth == inputHeight) {
            // The image is already square: if the side matches with the required size then
            // return the input image, else return the scaled version.
            if(inputWidth == size) {
                return input;
            }
            return Bitmap.createScaledBitmap(input, size, size, size > inputWidth);
        }
        // The image is not square: crop it from the center.
        int minSide = Math.min(inputWidth, inputHeight);
        int startX = (int)Math.floor(inputWidth - (minSide / 2.0f));
        int startY = (int)Math.floor(inputHeight - (minSide / 2.0f));
        Bitmap cropped = Bitmap.createBitmap(input, startX, startY, minSide, minSide);
        // If needed, scale the square to the required size.
        if(minSide != size) {
            return Bitmap.createScaledBitmap(cropped, size, size, size > minSide);
        }
        return cropped;
    }

    /**
     * Crops the given bitmap to a square. The square is centered. The size of the square is the
     * minimum between the height and width of the original image.
     *
     * @param input The input image to process.
     * @return The cropped image.
     */
    public static Bitmap cropToSquare(@NonNull Bitmap input) {
        verifyNotNull(input);
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        if(inputWidth == inputHeight) {
            return input;
        }
        int minSide = Math.min(inputWidth, inputHeight);
        return cropToScaledSquare(input, minSide);
    }

    /**
     * Scale the given image so that its height matches with a given height.
     * The aspect ratio of the original image is preserved.
     *
     * @param input The input image to process
     * @param height The new height of the output image
     * @return The scaled image.
     */
    public static Bitmap resizeWithHeight(Bitmap input, int height) {
        verifyNotNull(input);
        if(height <= 0) {
            throw new IllegalArgumentException("Height should be greater than 0");
        }
        int inputHeight = input.getHeight();
        if(inputHeight == height) {
            // If the input has the required size, return the input.
            return input;
        }
        int inputWidth = input.getWidth();
        int scaledWidth = (int)Math.floor(height * inputWidth / (float)inputHeight);
        return Bitmap.createScaledBitmap(input, scaledWidth, height, height > inputHeight);
    }
}
