package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Commit implements Serializable {

    // Instance Variables
    private String _message;
    private String _dateTime;
    private String _parentID;
    private String _commitID;
    private HashMap<String, String> _files; // Key: Name, Value: commitID

    // Init Commit
    public Commit() {
        this._dateTime = "Wed Dec 31 17:00:00 1969 -0700";
        this._message = "initial commit";
        this._parentID = null;
        this._commitID = makeID();
    }
    // Generic Commit--NOT merge
    public Commit(String message, HashMap<String, String> files, String parentID) {
        this._message = message;
        this._files = files;
        this._dateTime = getDate();
        this._commitID = makeID();
        this._parentID = parentID;
    }

    // Get the current date
    public String getDate() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZZ");
        return format.format(today);
    }

    // Hash Representation of a Commit
    public String makeID() {
        if (this._parentID == null) {
            return Utils.sha1(_message, _dateTime);
        }
        return Utils.sha1(_message, _files.toString(), _dateTime, _parentID);
    }

    public void addFile(String fileName, String fileID) {
        _files.put(fileName, fileID);
    }

    public void removeFile(String fileName) {
        _files.remove(fileName);
    }

    // String Format to print a commit--nonmerge
    public String toString() {
        return "commit " + _commitID
                + "\nDate: " + _dateTime
                + "\n" + _message + "\n";
    }


    public HashMap<String, String> getFiles() {
        return _files;
    }

    public String getParentID() {
        return this._parentID;
    }

    public String getMessage() {
        return _message;
    }

    public String getID() {
        return _commitID;
    }



}
