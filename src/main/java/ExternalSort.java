import jdk.jshell.execution.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class ExternalSort {

    private static BufferPool bufferPool;
    private int pageSize;
    private int numPages;
    private File fileToSort;

    public void setFileToSort(File fileToSort) {
        this.fileToSort = fileToSort;
    }

    public static BufferPool getBufferPool() {
        return bufferPool;
    }

    public ExternalSort(int pageSize, int numPages) {
        this.pageSize = pageSize;
        this.numPages = numPages;
        bufferPool = new BufferPool(pageSize);
    }

    public Run mergeRuns(List<Run> runs) throws IOException {
        List<Run.RunIterator> runIterators = new ArrayList<>();
        Run mergedRun = new Run();
        int bufferIndex = 0;
        for (int i = 0; i < runs.size(); i++) {
            Run.RunIterator runIterator = runs.get(i).getIterator(i);
            if (runIterator.hasNext()) {
                runIterator.next();
                runIterators.add(runIterator);
                bufferIndex++;
            }
        }

        System.out.println("Total buffers : " + bufferIndex);
        while(runIterators.size() > 0) {
            int min = Integer.MAX_VALUE;
            Run.RunIterator currentMinIterator = null;
            for (Run.RunIterator runIterator : runIterators){
               if (runIterator.current() <= min) {
                   currentMinIterator = runIterator;
                   min = runIterator.current();
               }
            }
           mergedRun.addField(min);

            if (currentMinIterator != null && currentMinIterator.hasNext()) {
                currentMinIterator.next();
            } else {
                runIterators.remove(currentMinIterator);
            }

        }

        mergedRun.flush();

        return mergedRun;
    }

    public List<Run> doAMergeIteration(List<Run> runs) throws IOException {

        List<Run> mergeList = new ArrayList<>();
        for (int i = 0; i < runs.size(); i++) {
            int maxLen = Math.min(runs.size(), this.numPages - 1);
            List<Run> listToMerge = new ArrayList<>(runs.subList(0, maxLen));
            runs.subList(0, maxLen).clear();
            Run run = mergeRuns(listToMerge);
            mergeList.add(run);
        }

        return mergeList;
    }

    public List<Run> splitIntoRuns(File binaryFile) throws IOException {

        int numPages = (int) Math.ceil((double) binaryFile.length() / pageSize);

        System.out.println("number of pages: " + numPages);
        List<Run> runs = new ArrayList<>();

        for (int i = 0; i < numPages; i++) {
            File tempFile = Utils.createTempFile();
            Page page = Utils.readPage(binaryFile, i);
            page.sort();
            System.out.println(page);
            Utils.writePageToFile(tempFile, page);
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
            System.out.println("read line " + line);
            dos.writeInt(Integer.parseInt(line));
        }

        dos.close();
        br.close();

        return binaryFile;
    }


    public void sort() throws Exception {
        File binaryFile = convertToBinaryFile(fileToSort);
        List<Run> runs = splitIntoRuns(binaryFile);

        int i = 0;
        while (runs.size() != 1) {
            System.out.println("Running iteration = " + i);
            i++;
            runs = doAMergeIteration(runs);
        }

        System.out.println("Finished merging the runs.");
        Run finalSortedRun = runs.get(0);
        Run.RunIterator finalRun = finalSortedRun.getIterator(0); // pick an arbitrary buffer to use
        File outputFile = new File("output.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        while (finalRun.hasNext()) {
            bufferedWriter.write("" + finalRun.next());
            bufferedWriter.newLine();
            System.out.println(finalRun.current());
        }
        bufferedWriter.close();
    }

    public static void main(String[] args) throws Exception {
        int pageSize = Integer.parseInt(args[0]);
        int numPages = Integer.parseInt(args[1]);
        String fileNameToSort = args[2];

        System.out.println("Got the following input:");
        System.out.println("pageSize: " + pageSize);
        System.out.println("numPages: " + numPages);
        System.out.println("fileNameToSort: " + fileNameToSort);

        ExternalSort externalSort = new ExternalSort(pageSize, numPages);
        externalSort.setFileToSort(new File(fileNameToSort));

        externalSort.sort();
    }
}
