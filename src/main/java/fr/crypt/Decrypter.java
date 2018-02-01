package fr.crypt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Decrypter implements InputHandler {

    private static final Pattern CRYPTO_PATTERN = Pattern.compile("[0-9 ]*");

    @Override
    public String produceOutput(String cryptogram) {
        checkValidCryptogram(cryptogram);
        String cryptogramNoSpace = cryptogram.replace(" ", "");
        return cryptogramToTlvs(cryptogramNoSpace)
                .flatMap(Decrypter::tlvToRefs)
                .map(ref -> String.valueOf(Crypter.REF_TABLE.charAt(ref)))
                .reduce(String::concat)
                .orElse("");
    }

    /**
     * Check cryptogram validity.
     *
     * @param crypto
     * @throws IllegalArgumentException if any of the cryptogram chars is invalid.
     */
    private static void checkValidCryptogram(String crypto) throws IllegalArgumentException {
        if (!CRYPTO_PATTERN.matcher(crypto).matches()) {
            throw new IllegalArgumentException("Cryptogram invalid !");
        }
    }

    private static Stream<Tlv> cryptogramToTlvs(String c) {
        List<Tlv> tlvs = new ArrayList<>();
        for (int n, start = 0; start < c.length(); start += 4 + n) {
            String subStr = c.substring(start);
            String type = subStr.substring(0, 2);
            n = Integer.parseInt(subStr.substring(2, 4));
            String value = subStr.substring(4, 4 + n);
            tlvs.add(new Tlv(type, value));
        }
        return tlvs.stream();
    }

    private static Stream<Integer> tlvToRefs(Tlv tlv) {
        LinkedList<Integer> list = new LinkedList<>();
        int value = Integer.parseInt(tlv.getValue());
        for (int base = Integer.parseInt(tlv.getType()); value > base; value /= base) {
            int ref = value % base;
            list.addFirst(ref);
        }
        list.addFirst(value);
        return list.stream();
    }
}
