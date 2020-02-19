import java.io.Reader;
import java.io.IOException;
import java.util.Arrays;

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

    private Reader r;
    private String f;
    private String t;

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
    public void close() throws IOException {
        r.close();
    }


    public int read(char[] arr, int offset, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        System.out.println("Initial array: " + Arrays.toString(arr));
        int readNum = r.read(arr, offset, len);
        System.out.println("Final array: " + Arrays.toString(arr));
        for (int i = 0; i + offset < arr.length && i < len; i += 1) {
            if (f.indexOf(arr[i]) != -1) {
                arr[i] = t.charAt(f.indexOf(arr[i]));
            }
        }
        return readNum;
    }
}