import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Page {

    private List<Integer> data;
    public static int FIELD_SIZE = Integer.SIZE;
    // size in bytes
    public static int PAGE_SIZE = 4096;

    Page(byte[] rawPage, int length) {
        IntBuffer intBuf = ByteBuffer.wrap(rawPage).asIntBuffer();
        data = new ArrayList<>();
        int lengthRemaining = length;

        while (lengthRemaining > 0) {
            data.add(intBuf.get());
            lengthRemaining -= Integer.BYTES;
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
        DataOutputStream oos = new DataOutputStream(baos);
        for (Integer num : data) {
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

    public Iterator<Integer> pageIterator() {
        return this.data.iterator();
    }

    public String toString() {
        return "page = " + String.join(",", this.data.stream().map(x -> String.valueOf(x)).toList());
    }
}
