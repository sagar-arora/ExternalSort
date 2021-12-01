import jdk.jshell.execution.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class ExternalSort {

    BufferPool bufferPool;
    private int pageSize;
    private int numPages;
    private File fileToSort;

    public static final int DEFAULT_PAGE_SIZE = 4096;
    public static final int DEFAULT_NUM_PAGES = 3;

    public void setBufferPool(BufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setFileToSort(File fileToSort) {
        this.fileToSort = fileToSort;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public BufferPool getBufferPool() {
        return bufferPool;
    }

    public ExternalSort(BufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    public ExternalSort(int pageSize, int numPages) {
        this(new BufferPool(numPages));
        this.pageSize = pageSize;
    }

    public void sort(File file) throws Exception {
        File binaryFile = convertToBinaryFile(file);
        List<Run> runs = splitIntoRuns(binaryFile);

        while (runs.size() != 1) {
            runs = mergeRuns(runs);
        }
    }

    public List<Run> mergeRuns(List<Run> runs) {

        List<Run> mergeList = new ArrayList<>();
        for (int i = 0; i < runs.size(); i++) {
            int maxLen = Math.max(runs.size(), this.numPages);
            List<Run> listToMerge = new ArrayList<>(runs.subList(0, maxLen));
            listToMerge.subList(0, maxLen).clear();
            mergeRuns(listToMerge);
        }

        return null;
    }

    public List<Run> splitIntoRuns(File binaryFile) throws IOException {

        int len = (int) binaryFile.length() / pageSize;

        List<Run> runs = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            File tempFile = Utils.createTempFile();
            Page page = Utils.readPage(binaryFile, i);
            page.sort();
            page.writePageToFile(tempFile);
            Run run = new Run(tempFile);
            runs.add(run);
        }

        return runs;
    }

    File convertToBinaryFile(File file) throws Exception {

        File binaryFile = Utils.createTempFile();

        DataOutputStream dos = new DataOutputStream(new FileOutputStream(binaryFile));
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = null;
        while ((line = br.readLine()) != null) {
            dos.writeInt(Integer.parseInt(line));
        }

        dos.close();
        br.close();

        return binaryFile;
    }

    public static void main(String[] args) {

        int pageSize = 4096;
        int numPages = 3;
        File file = null;

        ExternalSort externalSort = new ExternalSort(pageSize, numPages);
        externalSort.fileToSort = file;

        // 1. Generate the runs

    }
}
