package src_ASCII;
public class AsciiArt {
    char[][] img;
    int width;
    int height;

    public AsciiArt(char[][] original) {
        this.height = original.length;
        this.width  = (this.height == 0 ? 0 : original[0].length);
        this.img    = original;
    }

    /** Pad each row to newWidth with spaces (no scaling, just safe alignment). */
    public void resize(int newWidth) {
        if (newWidth <= width) return;
        for (int i = 0; i < height; i++) {
            char[] row = new char[newWidth];
            System.arraycopy(img[i], 0, row, 0, width);
            for (int j = width; j < newWidth; j++) row[j] = ' ';
            img[i] = row;
        }
        width = newWidth;
    }

    /** Return the widest art in an array. */
    public static AsciiArt getWidestArt(AsciiArt[] arts) {
        AsciiArt widest = arts[0];
        for (AsciiArt a : arts) if (a.width > widest.width) widest = a;
        return widest;
    }
}