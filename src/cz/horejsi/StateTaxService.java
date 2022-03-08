package cz.horejsi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
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
    private final DecimalFormat outputFormat = new DecimalFormat("0.#");


    private final List<StateTax> stateTaxList = new ArrayList<>();

    public void importFromFile(String filename, String delimiter) throws StateTaxException {

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {

            int rowCounter = 0;
            while (scanner.hasNextLine()) {

                rowCounter++;
                String[] parts = scanner.nextLine().split(delimiter);

                if (parts.length != 5)
                    throw new StateTaxException("ERROR: reading data from file: '" + filename + "' on the row: " + rowCounter + " \n\twrong number of items");

                try {
                    var stateShortcut = parts[0];
                    var stateName = parts[1];
                    var baseTax = format.parse(parts[2]).doubleValue();
                    var reducedTax = format.parse(parts[3]).doubleValue();
                    var isSpecialTax = Boolean.parseBoolean(parts[4]);

                    stateTaxList.add(new StateTax(stateShortcut, stateName, baseTax, reducedTax, isSpecialTax));

                } catch (ParseException | StateTaxException e) {
                    throw new StateTaxException("ERROR: reading data from file: '" + filename + "' on row: " + rowCounter + " \n\t" + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new StateTaxException("ERROR: file: '" + filename + "'  not found.\n\t" + e.getMessage());
        }
    }

    public void printBasicInfo() {
        stateTaxList.forEach(s -> System.out.println(s.getBasicInfo()));

        //stateTaxList.forEach(System.out::println);
    }

    public void printBasicInfo(double baseTaxLowRange, boolean isSpecialTax) {

        List<StateTax> validItems = new ArrayList<>();
        List<StateTax> notValidItems = new ArrayList<>();

        for (StateTax stateTax : stateTaxList) {
            if (stateTax.getBaseTax() > baseTaxLowRange && stateTax.hasSpecialTax() == isSpecialTax)
                validItems.add(stateTax);
            else
                notValidItems.add(stateTax);
        }

        String withSpecilTax = "with another special VAT tax(es)";
        String withoutSpecialTax =  "without another special VAT tax(es)";

        System.out.println("States with base VAT tax higher then " + outputFormat.format(baseTaxLowRange) +  " % and " + (isSpecialTax ? withSpecilTax : withoutSpecialTax) + ".");
        validItems.forEach(s -> System.out.println(s.getBasicInfo()));

        System.out.print("Base VAT tax " + outputFormat.format(baseTaxLowRange) +  " % or lower, or "  + (!isSpecialTax ? withSpecilTax : withoutSpecialTax) + ": ");
        notValidItems.forEach(s -> System.out.print("(" + s.getStateShortcut() + ") "));


    }
}




