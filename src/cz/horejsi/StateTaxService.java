package cz.horejsi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class StateTaxService {

    private final List<StateTax> stateTaxList = new ArrayList<>();

    public void importFromFile(String filename, String delimiter, NumberFormat inputNumberFormat) throws StateTaxException {

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {

            int rowCounter = 0;
            while (scanner.hasNextLine()) {

                rowCounter++;
                String[] parts = scanner.nextLine().split(delimiter);

                if (parts.length != 5)
                    throw new StateTaxException("CHYBA: načítání dat ze souboru: '" + filename + "' na řádku: " + rowCounter + " \n\tneplatný počet položek.");

                try {
                    String stateShortcut = parts[0];
                    String stateName = parts[1];
                    double baseTax = inputNumberFormat.parse(parts[2]).doubleValue();
                    double reducedTax = inputNumberFormat.parse(parts[3]).doubleValue();
                    boolean isSpecialTax = Boolean.parseBoolean(parts[4]);

                    stateTaxList.add(new StateTax(stateShortcut, stateName, baseTax, reducedTax, isSpecialTax));

                } catch (ParseException | StateTaxException e) {
                    throw new StateTaxException("CHYBA: načítání dat ze souboru: '" + filename + "' na řádku: " + rowCounter + " \n\t" + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) { throw new StateTaxException("CHYBA: soubor: '" + filename + "' nenalezen.\n\t" + e.getMessage()); }
    }

    public String filterAndFormatOutput(double lowerLimit, boolean hasSpecialTax, DecimalFormat taxFormat ) {

        List<StateTax> validItems = new ArrayList<>();
        List<StateTax> notValidItems = new ArrayList<>();

        //filtering
        for (StateTax stateTax : stateTaxList)
            if (stateTax.getBaseTax() > lowerLimit && stateTax.hasSpecialTax() == hasSpecialTax)
                validItems.add(stateTax);
            else
                notValidItems.add(stateTax);

        //sorting
        validItems.sort(Comparator.comparing(StateTax::getBaseTax).thenComparing(StateTax::getReducedTax).reversed());
        Collections.sort(notValidItems);

        //output
        return  validItems.stream().map(s -> s.getFullOutput(taxFormat)).collect(Collectors.joining("\n"))
                +"\nSazba VAT " + taxFormat.format(lowerLimit) +  " nebo nižší nebo "  + (!hasSpecialTax ? "používají" : "nepoužívají") + " speciální sazbu: "
                + notValidItems.stream().map(StateTax::getShortOutput).collect(Collectors.joining(" "));
    }
}




