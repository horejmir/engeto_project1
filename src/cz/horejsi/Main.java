package cz.horejsi;

public class Main {

    private static final String FILENAME = "vat-eu.csv";
    private static final String DELIMITER = "\t";

    public static void main(String[] args) {

        StateTaxService stateTaxService = new StateTaxService();

        try {
            stateTaxService.importFromFile(FILENAME, DELIMITER);
        } catch (StateTaxException e) {
            System.err.println(e.getMessage());
        }

        stateTaxService.printBasicInfo(20, false);

    }
}
