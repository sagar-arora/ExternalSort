import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Run {

    class RunIterator<Integer> implements Iterator<Integer> {

        private final int bufferIndex;
        private final int currentPageNumber;
        private final int numPages;

        RunIterator(int bufferIndex) {
            this.bufferIndex = bufferIndex;
            this.currentPageNumber = 0;
            this.numPages = (int) Math.ceil((double)file.length() / Page.PAGE_SIZE);
        }

        public Integer readNextField() {
            if ()
        }

        @Override
        public boolean hasNext() {
            if (currentPageNumber) {

            }

            return false;
        }

        @Override
        public Integer next() {
            return null;
        }
    }

    private File file;
    private BufferPool bufferPool;
    Run(File file) {
        this.file = file;
        this.bufferPool = ExternalSort.getBufferPool();
    }

    Run() throws IOException {
        this.file = File.createTempFile("external_sort", ".tmp");
        this.file.deleteOnExit();
        this.bufferPool = ExternalSort.getBufferPool();
    }

    public void addField(int field) throws IOException {
        bufferPool.addToOutputBuffer(file, field);
    }

}

