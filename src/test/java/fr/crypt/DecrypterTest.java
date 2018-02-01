package fr.crypt;

import org.junit.Assert;
import org.junit.Test;

public class DecrypterTest {

    private final String CRYPTOGRAM = "6702 296306 1732 4976 062973 66730 44000 54025 069 025 387 043455 7502 5567 0622 86476 704 246 991 0437 0270 023670 02625 00594 540 8506 3882 3992 0646 2851 900 6262 494 400213 120210 600229 5202 28710 2245 2021 784023 666025 36503 8498 70320 35102 1850 02488 506 2919 933602 235 90210 760 630 9986 9204 6347";
    private final String CLEAR = "The password to have a beer is \"WasDATSOHarD42TImeZINArow?\"";

    private final Decrypter instance = new Decrypter();

    @Test
    public void should_return_correct_output_on_valid_input() {
        String result = instance.produceOutput(CRYPTOGRAM);
        Assert.assertEquals(CLEAR, result);
    }

    @Test
    public void should_return_empty_output_on_empty_input() {
        String result = instance.produceOutput("");
        Assert.assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_on_invalid_input() {
        instance.produceOutput("???");
    }

}
