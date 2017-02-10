package nhs.genetics.cardiff.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Helper class for reading lists
 *
 * @author  Matt Lyon
 * @version 1.1
 * @since   2015-02-26
 */

public class ListReader {

    private File filePath;
    private ArrayList<String> elements = new ArrayList<>();

    public ListReader(File filePath){
        this.filePath = filePath;
    }

    public void parseListReader() throws IOException {
        String line;

        try (BufferedReader bufferedReader = new BufferedReader (new FileReader(filePath))){
            while ((line = bufferedReader.readLine()) != null) {
                elements.add(line);
            }
        }

    }

    public ArrayList<String> getElements() {
        return elements;
    }
    public File getFilePath() {
        return filePath;
    }
    public String getFileName(){
        return filePath.getName();
    }
    public HashSet<String> getUniqueElements() {
        HashSet<String> hashSet = new HashSet<>();
        for (String s : elements){
            hashSet.add(s);
        }
        return hashSet;
    }

}