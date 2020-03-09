package enigma;
import java.util.HashMap;
import java.util.Collection;


/** Class that represents a complete enigma machine.
 *  @author Zachary Zhu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        if (numRotors <= 1) {
            throw new EnigmaException("Insufficient rotors");
        } else if (pawls >= numRotors || pawls < 0) {
            throw new EnigmaException("Wrong number of pawls");
        }
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _rotorArray = new Rotor[allRotors.size()];
        _rotorHashMap = new HashMap<>();
        int counter = 0;
        cleanRotors(allRotors);
        for (Rotor r: allRotors) {
            _rotorArray[counter] = r;
            _rotorHashMap.put(r.name(), r);
            counter += 1;
        }

    }

    /**
     * 1. Must have unique names
     * 2. Check TRUE number of rotors (2+), pawls
     * (number of moving rotors), and reflectors (must == 1)
     * 3. Rotors must share an alphabet
     * @param rotors check to see if argument passed in contains errors.
     */
    void cleanRotors(Collection<Rotor> rotors) {
        String[] names = new String[rotors.size()];
        int totalRotors = 0;
        int numPawls = 0;
        int counter = 0;
        for (Rotor r: rotors) {
            if (!r.alphabet().getAlphabet()
                    .equals(_alphabet.getAlphabet())) {
                throw new EnigmaException("Alphabets don't match up");
            }
            names[counter] = r.name();
            if (r.rotates()) {
                numPawls += 1;
            }
            totalRotors += 1;
            counter += 1;
        }
        for (int i = 0; i < names.length; i += 1) {
            for (int j = i + 1; j < names.length; j += 1) {
                if (names[i].equals(names[j])) {
                    throw new EnigmaException("Repeated name for rotor");
                }
            }
        }
        if (totalRotors <= 1) {
            throw new EnigmaException("Wrong number of rotors passed in");
        } else if (numPawls < 0 || numPawls >= totalRotors) {
            throw new EnigmaException("Wrong number of pawls passed in");
        }

    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */


    void insertRotors(String[] rotors) {
        if (rotors.length > _rotorArray.length) {
            throw new EnigmaException("Length of rotors passed "
                    + "and matching don't correspond");
        }
        for (String name: rotors) {
            if (!_rotorHashMap.keySet().contains(name)) {
                throw new EnigmaException("Rotor name not found");
            }
        }
        for (int i = 0; i < rotors.length; i += 1) {
            for (int j = i + 1; j < rotors.length; j += 1) {
                if (rotors[i].equals(rotors[j])) {
                    throw new EnigmaException("insertRotors "
                            + "contains repeated name in rotor");
                }
            }
        }
        _rotorArray = new Rotor[rotors.length];
        for (int i = 0; i < rotors.length; i += 1) {
            _rotorArray[i] = _rotorHashMap.get(rotors[i]);
        }
        if (!_rotorArray[0].reflecting()) {
            throw new EnigmaException("First rotor isn't reflecting");
        }
        boolean moving = false;
        for (int i = _rotorArray.length - 1; i <= 0; i -= 1) {
            if (moving && !_rotorArray[i].rotates()) {
                throw new EnigmaException("Stationary rotor "
                        + "after moving encountered");
            }
            if (_rotorArray[i].rotates()) {
                moving = true;
            }
        }
    }


    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Wrong number of "
                    + "characters in setting");
        } else {
            for (int i = 1; i < _rotorArray.length; i += 1) {
                if (!_rotorArray[i].alphabet().
                        contains(setting.charAt(i - 1))) {
                    throw new EnigmaException("Rotor alphabet "
                            + "does not contain setting char");
                } else {
                    _rotorArray[i].permutation().
                            shiftAlphabet(_alphabet.getAlphabet().charAt(0));
                    _rotorArray[i].set(setting.charAt(i - 1));
                }
            }
        }
    }

    /**
     * Shift the Rotors' permutations in accordance
     * with Ringstalleung configuration.
     * @param setting reflects the setting of the Ringstalleung.
     */
    void ringstalleungRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Wrong number of "
                    + "characters in setting");
        } else {
            for (int i = 1; i < _rotorArray.length; i += 1) {
                if (!_rotorArray[i].alphabet().
                        contains(setting.charAt(i - 1))) {
                    throw new EnigmaException("Rotor alphabet "
                            + "does not contain setting char");
                } else {
                    _rotorArray[i].permutation().
                            shiftAlphabet(setting.charAt(i - 1));
                    int currentPos = _rotorArray[i].setting();
                    int shift =
                            _alphabet.getAlphabet().
                                    indexOf(setting.charAt(i - 1));
                    int finalPos = currentPos - shift;
                    _rotorArray[i].set(finalPos);
                }
            }
        }
    }
    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        if (!plugboard.alphabet().getAlphabet().equals
                (_alphabet.getAlphabet())) {
            throw new EnigmaException("Alphabet of plugboard wrong");
        }
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine.
     *  Approach
     *     1. Advance all relevant components of the machine
     *     2. Adjust for plugboard at beginning
     *     3. Go through all of the rotors--for each, figure
     *     out the 4 components:
     *          - What's the raw input? Translated input?
     *          Output? Translated output?
     *     4. Adjust for plugboard at the end
     *
     *  */
    int convert(int c) {
        boolean[] canRotate = new boolean[_rotorArray.length];
        canRotate[_rotorArray.length - 1] = true;
        for (int i = _rotorArray.length - 1; i >= 1; i -= 1) {
            if (_rotorArray[i].atNotch()
                    && _rotorArray[i - 1].rotates()) {
                canRotate[i] = true;
                canRotate[i - 1] = true;
            }
        }
        for (int i = 0; i < _rotorArray.length; i += 1) {
            if (canRotate[i]) {
                _rotorArray[i].advance();
            }
        }
        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        for (int i = _rotorArray.length - 1; i >= 0; i -= 1) {
            c = _alphabet.toInt(_rotorArray[i].alphabet().toChar
                    (_rotorArray[i].convertForward
                            (_rotorArray[i].alphabet().toInt
                                    (_alphabet.toChar(c)))));
        }
        for (int i = 1; i <= _rotorArray.length - 1; i += 1) {
            c = _alphabet.toInt(_rotorArray[i].alphabet().toChar
                    (_rotorArray[i].convertBackward
                            (_rotorArray[i].alphabet().toInt
                                    (_alphabet.toChar(c)))));
        }
        if (_plugboard != null) {
            c = _plugboard.invert(c);
        }
        return c;
    }


    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String convertedMsg = "";
        for (int i = 0; i < msg.length(); i += 1) {
            int c = _alphabet.toInt(msg.charAt(i));
            int conversion = convert(c);
            char back = _alphabet.toChar(conversion);
            convertedMsg += back;
        }
        return convertedMsg;
    }

    /**
     * Accessor method for the hashmap.
     * @return the RotorHashMap
     */
    HashMap<String, Rotor> getRotorHashMap() {
        return _rotorHashMap;
    }
    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of rotors. */
    private int _numRotors;
    /** The number of pawls. */
    private int _pawls;
    /** The HashMap with corresponding name and rotor. */
    private final HashMap<String, Rotor> _rotorHashMap;
    /** The Permutation of plugboard. */
    private Permutation _plugboard;
    /** The array holding the rotors. */
    private Rotor[] _rotorArray;

}
