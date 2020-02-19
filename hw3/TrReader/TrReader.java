import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Zachary Zhu
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */

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
        // FIXME
        r.close();
    }


    public int read(char[] arr, int offset, int len) throws IOException{
        //FIXME

        String initialStr = "";
        for (char c: arr) {
            initialStr += c;
        }
        System.out.println("initial: " + initialStr);
        if (len == 0) {
            return 0;
        }
        int c = r.read();
        if (c == -1) {
            return -1;
        }

        int numRead = 1;
        int counter = 0;
        //counter < len - 1
        while (c != -1 && counter + offset < arr.length && numRead <= len) {
            if (f.indexOf(c) != -1) {
                arr[counter + offset] = t.charAt(f.indexOf(c));
            }
            else {
                arr[counter + offset] = (char) c;
            }
            c = r.read();
            numRead += 1;
            counter += 1;
        }

        String finalStr = "";
        for (char ff: arr) {
            finalStr += ff;
        }
        System.out.println("final: " + finalStr);
        return numRead - 1;
    }
}
