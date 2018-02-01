package fr.crypt;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Crypt {

    public static void main(String... args) throws IOException {
        Options options = createOptions();

        try {
            CommandLine cl = new DefaultParser().parse(options, args);

            String input = getInput(cl);
            InputHandler inputHandler = cl.hasOption('d') ? new Decrypter() : new Crypter();
            OutputHandler outputHandler = cl.hasOption('o') ? new FileWriter(cl.getOptionValue('o')) : new Appender(System.out);
            int nbCrypts = cl.hasOption('n') && !cl.hasOption('u') ? Integer.parseInt(cl.getOptionValue('n')) : 1;
            for (int n = 0; n < nbCrypts; ++n) {
                outputHandler.handleOutput(inputHandler.produceOutput(input));
            }
        } catch (ParseException ex) {
            System.out.println(ex.getLocalizedMessage());
            new HelpFormatter().printHelp("java " + Crypt.class.getName() + " [options] (<message> | -f <src> <dest>)", options);
        }
    }

    private static Options createOptions() throws IllegalArgumentException {
        Option decrypt = new Option("d", false, "uncrypt the specified cryptogram.");
        Option nb = new Option("n", true, "Number of cryptograms to generate (disabled if -d is specified).");
        nb.setType(int.class);
        nb.setArgName("number");
        Option fileIn = new Option("i", true, "Specify a file as input.");
        fileIn.setArgName("file");
        Option fileOut = new Option("o", true, "Specify a file as output.");
        fileOut.setArgName("file");
        return new Options().addOption(decrypt).addOption(nb).addOption(fileIn).addOption(fileOut);
    }

    private static String getInput(CommandLine cl) throws IOException {
        if (cl.hasOption('i')) {
            String sourceFile = cl.getOptionValue('i');
            return readFile(sourceFile);
        }
        if (!cl.getArgList().isEmpty()) {
            return cl.getArgList().get(0);
        }
        Console console = System.console();
        return console.readLine();
    }

    private static String readFile(String source) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            return br.lines().reduce(String::concat).get();
        }
    }
}
