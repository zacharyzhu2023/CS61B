package gitlet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that is the RemoveArea.
 * @author Zachary Zhu
 */
public class RemoveArea implements Serializable {
    /** Files to be removed. */
    private ArrayList<String> removeFiles;

    /**
     * Initialize removeArea.
     */
    public RemoveArea() {
        removeFiles = new ArrayList<String>();
    }

    /**
     * Add a file.
     * @param fileName name of file
     */
    void add(String fileName) {
        removeFiles.add(fileName);
    }

    /**
     * Accessor method for files to be removed.
     * @return files to be removed.
     */
    ArrayList<String> getRemoveFiles() {
        return removeFiles;
    }
}
