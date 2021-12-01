import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page {

    List<Integer> data;
    public static int FIELD_SIZE = Integer.SIZE;
    // size in bytes
    public static int PAGE_SIZE = 4096;

    Page(byte[] rawPage, int length) {
        IntBuffer intBuf = ByteBuffer.wrap(rawPage).asIntBuffer();
        data = new ArrayList<>();
        int lengthRemaining = length;

        while (lengthRemaining > 0) {
            data.add(intBuf.get());
            lengthRemaining -= 4;
        }
    }

    public Page(List<Integer> list) {
        this.data = list;
    }

    public Page() {
        this.data = new ArrayList<>();
    }

    public void sort() {
        Collections.sort(data);
    }

    public List<Integer> getData() {
        return data;
    }


    public int getSize() {
        return PAGE_SIZE;
    }

    public void setSize(int size) {
        this.PAGE_SIZE = size;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        for (int num : data) {
            oos.writeInt(num);
        }
        oos.flush();
        return baos.toByteArray();
    }

    public boolean pageFull() {
        return data.size() * FIELD_SIZE > PAGE_SIZE;
    }

    public void addField(int field) throws PageFullException {
        if (pageFull()) {
            throw new PageFullException();
        }

        data.add(field);
    }
}
