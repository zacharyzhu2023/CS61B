package gitlet;

import java.io.Serializable;
import java.util.ArrayList;

public class RemoveArea implements Serializable {
    private ArrayList<String> removeFiles; // Name
    public RemoveArea() {
        removeFiles = new ArrayList<String>();
    }
    void add(String fileName) {
        removeFiles.add(fileName);
    }
    void remove(String fileName) {
        if (removeFiles.contains(fileName)) {
            removeFiles.remove(fileName);
        }
    }
    ArrayList<String> getRemoveFiles() {
        return removeFiles;
    }
}
