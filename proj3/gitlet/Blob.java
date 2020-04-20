package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private byte[] _contents;
    private String _ID;
    public Blob(File f) {
        _contents = Utils.readContents(f);
        _ID = Utils.sha1(_contents);
    }
    byte[] getContents() {
        return _contents;
    }
    String getID() {
        return _ID;
    }
    boolean sameBlob(Blob b) {
        return this._ID.equals(b._ID);
    }
}
