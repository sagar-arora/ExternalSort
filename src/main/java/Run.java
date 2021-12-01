import java.io.File;
import java.io.IOException;

public class Run {

    File file;

    Run(File file) {
        this.file = file;
    }

    Run() throws IOException {
        this.file = File.createTempFile("external_sort", ".tmp");
        this.file.deleteOnExit();
    }

    public void addField(int field) {

    }
}
