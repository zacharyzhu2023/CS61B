package gitlet;

import java.io.Serializable;
import java.util.ArrayList;

public class RemoveArea implements Serializable {
    private ArrayList<String> removeFiles; // Name
    public RemoveArea() {
        removeFiles = new ArrayList();
    }
    void add(String fileName) {
        removeFiles.add(fileName);
    }
    void remove(String fileName) {
        removeFiles.remove(fileName);
    }
    ArrayList<String> getRemoveFiles() {
        return removeFiles;
    }
}
