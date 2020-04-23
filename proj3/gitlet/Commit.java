package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/** Commit: a class for the representation of a git commit.
 *  @author Zachary Zhu
 */

public class Commit implements Serializable {
    /** Commit message. */
    private String _message;
    /** DateTime created. */
    private String _dateTime;
    /** ID of the parent. */
    private String _parentID;
    /** ID of the second parent. */
    private String _secondParentID;
    /** ID of a commit. */
    private String _commitID;
    /** Tracked Files. */
    private HashMap<String, String> _files;

    /**
     * Init Commit.
     */
    public Commit() {
        this._dateTime = "Wed Dec 31 17:00:00 1969 -0700";
        this._message = "initial commit";
        this._parentID = null;
        this._commitID = makeID();
    }

    /**
     * Generate a non-merge commit.
     * @param message
     * @param files
     * @param parentID
     */
    public Commit(String message, HashMap<String, String> files, String parentID) {
        this._message = message;
        this._files = files;
        this._dateTime = getDate();
        this._commitID = makeID();
        this._parentID = parentID;
    }

    /**
     * Generate a merged commit.
     * @param message
     * @param files
     * @param parentID
     * @param secondParentID
     */
    public Commit(String message, HashMap<String, String> files, String parentID, String secondParentID) {
        this._message = message;
        this._files = files;
        this._dateTime = getDate();
        this._commitID = makeID();
        this._parentID = parentID;
        this._secondParentID = secondParentID;
    }

    /**
     * Get the current date.
     * @return String representation of a date.
     */
    public String getDate() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZZ");
        return format.format(today);
    }

    /**
     * Make hashRepresentation of a Commit.
     * @return String representation
     */
    public String makeID() {
        if (this._parentID == null) {
            return Utils.sha1(_message, _dateTime);
        } else if (this._secondParentID == null) {
            return Utils.sha1(_message, _files.toString(), _dateTime, _parentID);
        }
        return Utils.sha1(_message, _files.toString(), _dateTime, _parentID, _secondParentID);
    }

    /**
     * Method that creates string representation of commit.
     * @return toString representation.
     */
    public String toString() {
        if (getParent2ID() == null) {
            return "commit " + _commitID
                    + "\nDate: " + _dateTime
                    + "\n" + _message + "\n";
        } else {
            return "commit " + _commitID
                    + "\nMerge: " + _parentID.substring(0, 7) + " " + _secondParentID.substring(0, 7)
                    + "\nDate: " + _dateTime
                    + "\n" + _message + "\n";
        }
    }

    /**
     * Accessor methods for files.
     * @return files
     */
    public HashMap<String, String> getFiles() {
        return _files;
    }

    /**
     * Accessor method for parentID.
     * @return parentID
     */
    public String getParentID() {
        return this._parentID;
    }

    /**
     * Accessor method for secondParentID.
     * @return secondParentID
     */
    public String getParent2ID() {return this._secondParentID;}

    /**
     * Accessor method for the message.
     * @return message
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Accessor method for current ID.
     * @return Commit's ID
     */
    public String getID() {
        return _commitID;
    }



}
