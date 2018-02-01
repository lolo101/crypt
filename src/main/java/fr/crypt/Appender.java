package fr.crypt;

import java.io.IOException;

class Appender implements OutputHandler {

    private final Appendable appendable;
    private int index = 0;

    Appender(Appendable appendable) {
        this.appendable = appendable;
    }

    @Override
    public void handleOutput(String target) {
        try {
            appendable.append(String.format("%d\t%s%n", index++, target));
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }
}
