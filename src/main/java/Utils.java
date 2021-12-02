import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Utils {

    private Utils() {}

    public static File createTempFile() throws IOException {
        return File.createTempFile("external_sort", ".tmp");
    }

    public static Page readPage(File file, int pageNumber) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        byte[] rawPage = new byte[Page.PAGE_SIZE];
        randomAccessFile.seek(pageNumber * Page.PAGE_SIZE);
        int bytesRead = randomAccessFile.read(rawPage, 0, Page.PAGE_SIZE);

        return new Page(rawPage, bytesRead);
    }

    public static void writePageToFile(File file, Page page) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] rawPage = page.serialize();
        fileOutputStream.write(rawPage);
        fileOutputStream.close();
    }

    public static void flushPage(File file, Page page) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        byte[] rawPage = page.serialize();
        fileOutputStream.write(rawPage);
        fileOutputStream.close();
    }
}
