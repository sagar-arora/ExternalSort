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

    public void flushPage(int index) {

    }

    public void addToOutputBuffer(int field) {

    }
}
