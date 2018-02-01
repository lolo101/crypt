package fr.crypt;

import org.junit.Assert;
import org.junit.Test;

public class CrypterTest {

    private final String CLEAR = "The password to have a beer is \"WasDATSOHarD42TImeZINArow?\"";

    private final Crypter instance = new Crypter();

    @Test
    public void should_return_output_on_valid_input() {
        String result = instance.produceOutput(CLEAR);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void should_return_empty_output_on_empty_input() {
        String result = instance.produceOutput("");
        Assert.assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_on_invalid_input() {
        instance.produceOutput("ø");
    }

}
