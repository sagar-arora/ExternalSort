import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Run {

    public class RunIterator implements Iterator<Integer> {

        private final int bufferIndex;
        private int currentPageNumber;
        private final int numPages;
        private Iterator<Integer> pageIterator;
        Integer next;
        Integer current;
        RunIterator(int bufferIndex) throws IOException {
            this.bufferIndex = bufferIndex;
            this.currentPageNumber = 0;
            this.numPages = (int) Math.ceil((double)file.length() / Page.PAGE_SIZE);
            Page page = bufferPool.readPageIntoBufferPool(file, 0, bufferIndex);
            this.pageIterator = page.pageIterator();
        }

        public Page readNextPage() {
            try {
                return bufferPool.readPageIntoBufferPool(file, this.currentPageNumber, bufferIndex);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error occurred. should never happen");
            }
            return null;
        }

        public Integer readNextField() {
            if (pageIterator.hasNext()) {
                return pageIterator.next();
            } else if (this.currentPageNumber++ < numPages) {
                Page page = readNextPage();
                this.pageIterator = page.pageIterator();
                return this.pageIterator.next();
            } else {
                return null;
            }
        }

        @Override
        public boolean hasNext() {
            next = readNextField();
            return next != null;
        }

        @Override
        public Integer next() {
           if (next == null) {
               return null;
           }
           current = next;
           return next;
        }

        public int current() {
            return current;
        }
    }

    private File file;
    private static BufferPool bufferPool;
    Run(File file) {
        this.file = file;
        bufferPool = ExternalSort.getBufferPool();
    }

    Run() throws IOException {
        this.file = File.createTempFile("external_sort", ".tmp");
        this.file.deleteOnExit();
        bufferPool = ExternalSort.getBufferPool();
    }

    public void addField(int field) throws IOException {
        bufferPool.addToOutputBuffer(file, field);
    }

    public RunIterator getIterator(int bufferIndex) throws IOException {
        return new RunIterator(bufferIndex);
    }

    public void flush() throws IOException {
        Utils.flushPage(file, bufferPool.getOutputBufferPage());
    }
}

