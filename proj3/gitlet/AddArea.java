package gitlet;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class that represents added files.
 * @author Zachary Zhu
 */
public class AddArea implements Serializable {
    /** Contains added files. */
    private HashMap<String, String> addedFiles;

    /**
     * Init method.
     */
    public AddArea() {
        addedFiles = new HashMap<String, String>();
    }

    /**
     * Add Files to AddArea.
     * @param fileName name of file
     * @param id fileID
     */
    void add(String fileName, String id) {
        addedFiles.put(fileName, id);
    }

    /**
     * Remove files from RemoveArea.
     * @param fileName name of file
     */
    void remove(String fileName) {
        if (addedFiles.containsKey(fileName)) {
            addedFiles.remove(fileName);
        }
    }

    /**
     * Accessor method for added files.
     * @return addedFiles.
     */
    HashMap<String, String> getAddedFiles() {
        return addedFiles;
    }
}
