package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Zachary Zhu
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        cleanNotches(notches);
        _notches = notches;
    }

    /** Method that checks if notches are valid or not.
     * Throws an error if they are not.
     * @param notches string notches given as input
     */
    public void cleanNotches(String notches) {
        for (int i = 0; i < notches.length(); i += 1) {
            for (int j = i + 1; j < notches.length(); j += 1) {
                if (notches.charAt(i) == notches.charAt(j)) {
                    throw new
                            EnigmaException("Notches are "
                            + "invalid: contains repeat");
                }
            }
            if (!alphabet().contains(notches.charAt(i))) {
                throw new
                        EnigmaException("Notches are "
                        + "invalid: not in alphabet");
            }
        }
    }

    @Override
    boolean atNotch() {
        char c = alphabet().toChar(setting());
        if (_notches.indexOf(c) != -1) {
            return true;
        }
        return false;
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    @Override
    boolean rotates() {
        return true;
    }

    /** Return the notches.
     * @return string notches
     */

    String getNotches() {
        return _notches;
    }

    /** Variable that stores ths notches string. **/
    private String _notches;
}
