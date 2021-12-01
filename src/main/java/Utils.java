import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;

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

        Page page = new Page(rawPage, bytesRead);
        return page;
    }
}
