package cz.horejsi;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Main {

    private static final String INPUT_FILENAME = "vat-eu.csv";
    private static final String DELIMITER = "\t";
    private static final String INPUT_FILE_LOCALE_LANGUAGE = "cs";
    private static final String INPUT_FILE_LOCALE_COUNTRY = "CZ";

    private static final String LOCALE_LANGUAGE = "en";
    private static final String LOCALE_COUNTRY = "US";

    private static final String OUTPUT_TAX_PATTERN = "0.# '%'";
    private static final String OUTPUT_FILE_PREFIX = "vat-over-";
    private static final String OUTPUT_FILE_EXTENSION = ".txt";

    public static void main(String[] args) {

        Locale.setDefault(new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY));

        StateTaxService stateTaxService = new StateTaxService();

        //import data from file
        try {
            NumberFormat inputNumberFormat = NumberFormat.getInstance(new Locale(INPUT_FILE_LOCALE_LANGUAGE, INPUT_FILE_LOCALE_COUNTRY));
            stateTaxService.importFromFile(INPUT_FILENAME, DELIMITER, inputNumberFormat);
        } catch (StateTaxException e) {
            System.err.println(e.getMessage());
        }

        //get filter limit from user
        double lowerLimit = 20.0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Insert lower limit of base VAT tax: ");
            String input = reader.readLine();
            lowerLimit = Double.parseDouble(input);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Input is not valid (will be used "+ lowerLimit + " limit): " + e.getMessage());
        }

        //get and print output
        String output = stateTaxService.filterAndFormatOutput(lowerLimit,false, new DecimalFormat(OUTPUT_TAX_PATTERN));
        System.out.println(output);

        //export to file
        String outputFilename = OUTPUT_FILE_PREFIX + (int)lowerLimit + OUTPUT_FILE_EXTENSION;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
            writer.write(output);
            System.out.println("\n# OUTPUT SAVED TO FILE: '" + outputFilename + "'");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}


