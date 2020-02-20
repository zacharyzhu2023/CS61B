import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Zachary Zhu
 */
public class TrReader extends Reader {
    /**
     * A new TrReader that produces the stream of characters produced
     * by STR, converting all characters that occur in FROM to the
     * corresponding characters in TO.  That is, change occurrences of
     * FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     * in STR unchanged.  FROM and TO must have the same length.
     */

    /** Reader r instance variable. **/
    private Reader r;
    /** String f instance variable. **/
    private String f;
    /** String t instance variable. **/
    private String t;

    /** Constructor for TrReader.
     * @param str input reader
     * @param from string from
     * @param to string to
     **/
    public TrReader(Reader str, String from, String to) throws IOException {
        r = str;
        f = from;
        t = to;
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */

    /** Implement the close() method.
     * **/
    public void close() throws IOException {
        r.close();
    }


    /** Implement the read(char[] arr, int offset, int len) method.
     * @param arr input array
     * @param offset the int offset
     * @param len num characters read max
     * @return int number characters read
     * **/
    public int read(char[] arr, int offset, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int readNum = r.read(arr, offset, len);
        for (int i = 0; i + offset < arr.length && i < len; i += 1) {
            if (f.indexOf(arr[i + offset]) != -1) {
                arr[i + offset] = t.charAt(f.indexOf(arr[i + offset]));
            }
        }
        return readNum;
    }
}
