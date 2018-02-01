package fr.crypt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

class FileWriter implements OutputHandler {

    private final String baseName;
    private int index = 0;

    FileWriter(String baseName) {
        this.baseName = baseName;
    }

    @Override
    public void handleOutput(String target) {
        try {
            File f = new File(baseName + "." + index++);
            writeFile(target, f);
            System.out.printf("written %sn", f, index);
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    private static void writeFile(String crypt, File dest) throws IOException {
        try (PrintWriter pw = new PrintWriter(dest)) {
            pw.print(crypt);
        }
    }
}
