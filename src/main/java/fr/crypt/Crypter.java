package fr.crypt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Crypter implements InputHandler {

    static final String REF_TABLE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ,./<>?`~!@#$%^&*()_+-=[]{}'\\\"|;:\r\n";
    private static final double LOG2 = Math.log(2.0);
    private final Random RAND = new Random();

    @Override
    public String produceOutput(String clearMsg) {
        checkValidMessage(clearMsg);
        int[] refArray = clearMsg.chars().map(REF_TABLE::indexOf).toArray();
        String cryptNoSpace = refsToTlvs(refArray)
                .map(Tlv::toString)
                .reduce(String::concat)
                .orElse("");
        return insertSpaces(cryptNoSpace);
    }

    /**
     * Check message validity. The message is valid if all its chars are in the
     * reference table.
     *
     * @param clearMsg
     * @throws IllegalArgumentException if any of the message chars is not in
     * the reference table.
     */
    private static void checkValidMessage(String clearMsg) throws IllegalArgumentException {
        Set<Character> invalidChars = clearMsg.chars()
                .filter(c -> REF_TABLE.indexOf(c) == -1)
                .boxed()
                .map(ci -> (char) ci.intValue())
                .collect(Collectors.toSet());
        if (!invalidChars.isEmpty()) {
            throw new IllegalArgumentException("Message invalid ! Contains invalid char(s): " + invalidChars);
        }
    }

    private Stream<Tlv> refsToTlvs(int[] refArray) {
        List<Tlv> tlvs = new ArrayList<>();
        for (int nbRefToEncode, index = 0; index < refArray.length; index += nbRefToEncode) {
            nbRefToEncode = Math.min(RAND.nextInt(3) + 1, refArray.length - index);
            Collection<Integer> refs = subArray(refArray, index, nbRefToEncode);
            tlvs.add(encode(refs));
        }
        return tlvs.stream();
    }

    private List<Integer> subArray(int[] refArray, int index, int count) {
        // Collect refs
        List<Integer> refs = new ArrayList<>(count);
        for (int offset = 0; offset < count; ++offset) {
            refs.add(refArray[index + offset]);
        }
        return refs;
    }

    private Tlv encode(Collection<Integer> refs) {
        int base = selectBase(Collections.max(refs));
        int value = refs.stream().reduce((r1, r2) -> r1 * base + r2).get();
        return new Tlv(String.format("%02d", base), String.valueOf(value));
    }

    private int selectBase(int max) {
        return RAND.nextInt(99 - max) + max;
    }

    private static String insertSpaces(String cryptNoSpace) {
        // on coupe n-3 fois,
        // où n est le plus petit entier tel que 2^n >= cryptNoSpace.length()
        // ainsi on devrait découper cryptNoSpace en tronçons d'environ 8 chars.
        double n = Math.ceil(Math.log(cryptNoSpace.length()) / LOG2) - 3;
        return splitRecursively(cryptNoSpace, (int) n);
    }

    private static String splitRecursively(String unspaced, int n) {
        if (n > 0) {
            int middle = unspaced.length() / 2;
            String left = splitRecursively(unspaced.substring(0, middle), n - 1);
            String right = splitRecursively(unspaced.substring(middle), n - 1);
            return left + " " + right;
        }
        return unspaced;
    }

}
