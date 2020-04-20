package gitlet;

import java.io.Serializable;
import java.util.HashMap;

public class AddArea implements Serializable {
    private HashMap<String, String> addedFiles; // Name --> ID
    public AddArea() {
        addedFiles = new HashMap<String, String>();
    }
    void add(String fileName, String ID) {
        addedFiles.put(fileName, ID);
    }
    void remove(String fileName) {
        addedFiles.remove(fileName);
    }
    HashMap<String, String> getAddedFiles() {
        return addedFiles;
    }
}
