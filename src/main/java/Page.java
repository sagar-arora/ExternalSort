import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page {

    List<Integer> data;
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

    public void writePageToFile(File file) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        for (int num : data) {
            objectOutputStream.writeInt(num);
        }
        objectOutputStream.close();
    }
}
