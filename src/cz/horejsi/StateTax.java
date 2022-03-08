package cz.horejsi;

import java.text.DecimalFormat;

public class StateTax {

    private final DecimalFormat outputFormat = new DecimalFormat("0.#");

    private String stateShortcut;
    private String stateName;
    private Double baseTax;
    private Double reducedTax;
    private boolean hasSpecialTax;

    public StateTax(String stateShortcut, String stateName, Double fullTax, Double reducedTax, boolean hasSpecialTax) throws StateTaxException {
        this.stateShortcut = stateShortcut;
        this.stateName = stateName;
        setBaseTax(fullTax);
        setReducedTax(reducedTax);
        this.hasSpecialTax = hasSpecialTax;
    }

    public String getBasicInfo(){
        return stateName + " (" + stateShortcut + "):\t" + outputFormat.format(baseTax) + " %\t(" + outputFormat.format(reducedTax) + " %)";
    }

    @Override
    public String toString() {
        return "StateTax{" +
                "stateShortcut='" + stateShortcut + '\'' +
                ", stateName='" + stateName + '\'' +
                ", baseTax=" + baseTax +
                ", reducedTax=" + reducedTax +
                ", hasSpecialTax=" + hasSpecialTax +
                '}';
    }

    public String getStateShortcut() {
        return stateShortcut;
    }

    public void setStateShortcut(String stateShortcut) {
        this.stateShortcut = stateShortcut;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Double getBaseTax() {
        return baseTax;
    }

    public void setBaseTax(Double baseTax) throws StateTaxException {

        if(baseTax < 0.0)
            throw new StateTaxException("base tax must be greater or equal to 0");
        else
            this.baseTax = baseTax;
    }

    public Double getReducedTax() {
        return reducedTax;
    }

    public void setReducedTax(Double reducedTax) throws StateTaxException {

        if(reducedTax < 0.0)
            throw new StateTaxException("reduced tax must be greater or equal to 0");
        else
            this.reducedTax = reducedTax;
    }

    public boolean hasSpecialTax() {
        return hasSpecialTax;
    }

    public void setSpecialTax(boolean hasSpecialTax) {
        this.hasSpecialTax = hasSpecialTax;
    }
}
