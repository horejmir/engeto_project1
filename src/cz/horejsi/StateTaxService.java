package cz.horejsi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class StateTaxService {

    private final String LOCALE_LANGUAGE = "cs";
    private final String LOCALE_COUNTRY = "CZ";
    private final NumberFormat format = NumberFormat.getInstance(new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY));


    private final List<StateTax> stateTaxList = new ArrayList<>();

    public void importFromFile(String filename, String delimiter) throws StateTaxException {

        try( Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {

            int rowCounter = 0;
            while (scanner.hasNextLine()) {

                rowCounter++;
                String[] parts = scanner.nextLine().split(delimiter);

                if(parts.length != 5) throw new StateTaxException("Wrong number of items on the row.");

                try {
                    var stateShortcut = parts[0];
                    var stateName = parts[1];
                    var baseTax = format.parse(parts[2]).doubleValue();
                    var reducedTax = format.parse(parts[3]).doubleValue();
                    var isSpecialTax = Boolean.parseBoolean(parts[4]);

                    stateTaxList.add(new StateTax(stateShortcut, stateName, baseTax, reducedTax, isSpecialTax));

                } catch (ParseException e) {
                    throw new StateTaxException("error reading data from file: '" + filename + "' on row: " + rowCounter + " \n\t" + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new StateTaxException("File not found: " + e.getMessage());
        }
    }

    public void printBasicInfo() {
        stateTaxList.forEach(s -> System.out.println(s.getBasicInfo()));
    }
}




