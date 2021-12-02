import java.io.File;
import java.io.IOException;

public class BufferPool {

    Page[] pages;
    int outputBufferIndex;
    int size;

    public BufferPool(int size) {
        this.pages = new Page[size];
        this.size = size;
        this.outputBufferIndex = size - 1;
    }

    public Page getPage(int index) {
        return pages[index];
    }

    public int getSize() {
        return size;
    }

    public void addToOutputBuffer(File file, int field) throws IOException {
        Page outputBufferPage = pages[outputBufferIndex];
        try {
            outputBufferPage.addField(field);
        } catch (PageFullException e) {
            Utils.flushPage(file, outputBufferPage);
            pages[outputBufferIndex] = new Page();
            try {
                pages[outputBufferIndex].addField(field);
            } catch (PageFullException ex) {
                System.out.println("Shouldn't come here.");
            }
        }
    }

    public Page readPageIntoBufferPool(File file, int pageNumber, int bufferIndex) throws IOException {
        Page page = Utils.readPage(file, pageNumber);
        pages[pageNumber] = page;
        return page;
    }
}
