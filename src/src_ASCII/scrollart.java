package src_ASCII;
import java.util.Random;

public class scrollart {
    // --------- Nested helper class so no separate file is needed ---------
    static class AsciiArt {
        char[][] img;
        int width;
        int height;

        AsciiArt(char[][] original) {
            this.height = original.length;
            this.width  = (this.height == 0 ? 0 : original[0].length);
            this.img    = original;
        }

        void resize(int newWidth) {
            if (newWidth <= width) return;
            for (int i = 0; i < height; i++) {
                char[] row = new char[newWidth];
                System.arraycopy(img[i], 0, row, 0, width);
                for (int j = width; j < newWidth; j++) row[j] = ' ';
                img[i] = row;
            }
            width = newWidth;
        }

        static AsciiArt getWidestArt(AsciiArt[] arts) {
            AsciiArt widest = arts[0];
            for (AsciiArt a : arts) if (a.width > widest.width) widest = a;
            return widest;
        }
    }
    // ---------------------------------------------------------------------

    static final int WIDTH = getTerminalWidth() - 1;
    static final Random rand = new Random();

    public static void main(String[] args) throws InterruptedException {
        AsciiArt burger = new AsciiArt(getBurger());
        AsciiArt ice    = new AsciiArt(getIC());
        AsciiArt ghost  = lines(
            "   .-.   ",
            "  (o o)  ",
            "  | O |  ",
            "  |   |  ",
            "  |___|  ",
            "  /   \\  ",
            " /_____\\ "
        );
        AsciiArt catScarf = lines(
            "  /\\_/\\  ",
            " ( o.o ) ",
            "  > ^ <  ",
            "  |###|  "
        );
        AsciiArt bat = lines(
            "  /\\   /\\  ",
            " (  \\_/  ) ",
            "  \\  |  /  ",
            "   \\_|_/   "
        );

        // Add initials ONLY to burger & ice
        addInitials(burger);
        addInitials(ice);

        AsciiArt[] all = new AsciiArt[]{burger, ice, ghost, catScarf, bat};

        int widest = AsciiArt.getWidestArt(all).width;
        for (AsciiArt a : all) a.resize(widest);

        int canvasH = 0;
        for (AsciiArt a : all) canvasH = Math.max(canvasH, a.height);

        char[][] nextRows = new char[canvasH][WIDTH];
        for (int i = 0; i < nextRows.length; i++) nextRows[i] = emptyRow();

        while (true) {
            if (rand.nextDouble() < 0.28) {
                AsciiArt art = all[rand.nextInt(all.length)];
                blit(nextRows, art, randomSafeX(art.width));
            }
            System.out.println(new String(nextRows[0]));
            shiftRowsUp(nextRows);
            Thread.sleep(60);
        }
    }

    static void addInitials(AsciiArt a) {
        int h = a.height, w = a.width;
        if (h >= 2 && w >= 3) {
            a.img[h - 2][w - 3] = 'B';
            a.img[h - 2][w - 2] = 'T';
        }
    }

    static void blit(char[][] nextRows, AsciiArt art, int x) {
        int h = Math.min(art.height, nextRows.length);
        int w = art.width;
        for (int iy = 0; iy < h; iy++) {
            for (int ix = 0; ix < w; ix++) {
                int dx = x + ix;
                if (dx >= 0 && dx < nextRows[iy].length) {
                    char c = art.img[iy][ix];
                    if (c != ' ') nextRows[iy][dx] = c;
                }
            }
        }
    }

    static int randomSafeX(int artWidth) {
        int maxX = Math.max(0, WIDTH - artWidth);
        return rand.nextInt(maxX + 1);
    }

    static void shiftRowsUp(char[][] nextRows) {
        for (int i = 1; i < nextRows.length; i++)
            nextRows[i - 1] = nextRows[i];
        nextRows[nextRows.length - 1] = emptyRow();
    }

    static char[] emptyRow() {
        char[] row = new char[WIDTH];
        for (int i = 0; i < WIDTH; i++) row[i] = ' ';
        return row;
    }

    static AsciiArt lines(String... raw) {
        int h = raw.length, w = 0;
        for (String s : raw) w = Math.max(w, s.length());
        char[][] img = new char[h][w];
        for (int y = 0; y < h; y++) {
            String s = raw[y];
            for (int x = 0; x < w; x++) img[y][x] = (x < s.length()) ? s.charAt(x) : ' ';
        }
        return new AsciiArt(img);
    }

    // --------- Burger (13x13) ----------
    static final int BURGER_HEIGHT = 13;
    static final int BURGER_WIDTH  = 13;
    static char[][] getBurger() {
        char[][] img = new char[BURGER_HEIGHT][BURGER_WIDTH];
        for (int y = 0; y < BURGER_HEIGHT; y++)
            for (int x = 0; x < BURGER_WIDTH; x++)
                img[y][x] = ' ';

        img[1][3]  = '-'; img[1][4]  = '-'; img[1][5]  = '-'; img[1][6]  = '-';
        img[1][7]  = '-'; img[1][8]  = '-'; img[1][9]  = '-'; img[1][10] = '-';
        img[2][2]  = '/'; img[2][11] = '\\';
        img[3][1]  = '/'; img[3][12] = '\\';
        img[4][1]  = '|';
        img[4][2]  = '-'; img[4][3]  = '-'; img[4][4]  = '-'; img[4][5]  = '-';
        img[4][6]  = '-'; img[4][7]  = '-'; img[4][8]  = '-'; img[4][9]  = '-';
        img[4][10] = '-'; img[4][11] = '-'; img[4][12] = '|';
        img[6][1]  = '/'; img[6][2]  = '\\'; img[6][3]  = '/'; img[6][4]  = '\\';
        img[6][5]  = '/'; img[6][6]  = '\\'; img[6][7]  = '/'; img[6][8]  = '\\';
        img[6][9]  = '/'; img[6][10] = '\\'; img[6][11] = '/'; img[6][12] = '\\';
        img[7][2]  = '-'; img[7][3]  = '-'; img[7][4]  = '-'; img[7][5]  = '-';
        img[7][6]  = '-'; img[7][7]  = '-'; img[7][8]  = '-'; img[7][9]  = '-';
        img[7][10] = '-'; img[7][11] = '-';
        img[8][1]  = '<'; img[8][12] = '>';
        img[9][2]  = '-'; img[9][3]  = '-'; img[9][4]  = '-'; img[9][5]  = '-';
        img[9][6]  = '-'; img[9][7]  = '-'; img[9][8]  = '-'; img[9][9]  = '-';
        img[9][10] = '-'; img[9][11] = '-';
        img[10][2] = '-'; img[10][3] = '-'; img[10][4] = '-'; img[10][5] = '-';
        img[10][6] = '-'; img[10][7] = '-'; img[10][8] = '-'; img[10][9] = '-';
        img[10][10] = '-'; img[10][11] = '-';
        img[11][1] = '|'; img[11][12] = '|';
        img[12][2] = '-'; img[12][3] = '-'; img[12][4] = '-'; img[12][5] = '-';
        img[12][6] = '-'; img[12][7] = '-'; img[12][8] = '-'; img[12][9] = '-';
        img[12][10] = '-'; img[12][11] = '-';

        return img;
    }

    // --------- Ice Cream (19x12) ----------
    static final int IC_HEIGHT = 19;
    static final int IC_WIDTH  = 12;
    static char[][] getIC() {
        char[][] img = new char[IC_HEIGHT][IC_WIDTH];
        for (int y = 0; y < IC_HEIGHT; y++)
            for (int x = 0; x < IC_WIDTH; x++)
                img[y][x] = ' ';

        img[1][5] = '-'; img[1][6] = '-'; img[1][7] = '-';
        img[2][4] = '/'; img[2][5] = '@'; img[2][8] = '\\';
        img[3][3] = '/'; img[3][6] = '#'; img[3][8] = '*'; img[3][9] = '\\';
        img[4][2] = '/'; img[4][3] = '#'; img[4][5] = '*'; img[4][10] = '\\';
        img[5][1] = '('; img[5][3] = '*'; img[5][7] = '@'; img[5][9] = '#'; img[5][11] = ')';
        img[6][1] = '('; img[6][2] = '_'; img[6][3] = '_'; img[6][4] = '_'; img[6][5] = '_';
        img[6][6] = '_'; img[6][7] = '_'; img[6][8] = '_'; img[6][9] = '_'; img[6][10] = '_'; img[6][11] = ')';

        for (int y = 7; y <= 16; y++)
            for (int x = 4; x <= 8; x++) img[y][x] = '|';

        img[17][4] = '\\'; img[17][5] = '|'; img[17][6] = '|'; img[17][7] = '|'; img[17][8] = '/';
        img[18][5] = '_'; img[18][6] = '_'; img[18][7] = '_';

        return img;
    }

    // --------- Terminal width helpers ----------
    public static int getTerminalWidth() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return getUnixTerminalWidth();
        } else return 80;
    }

    private static int getUnixTerminalWidth() {
        try {
            String columns = System.getenv("COLUMNS");
            if (columns != null && !columns.isEmpty()) return Integer.parseInt(columns);
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "stty size </dev/tty");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            if (output != null && !output.isEmpty()) {
                String[] p = output.trim().split(" ");
                return Integer.parseInt(p[1]);
            }
        } catch (Exception ignore) {}
        return 80;
    }
}