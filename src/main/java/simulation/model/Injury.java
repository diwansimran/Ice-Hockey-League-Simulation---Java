package simulation.model;

public class Injury {
    private double randomInjuryChance;
    private int injuryDaysLow;
    private int injuryDaysHigh;

    public Injury() {
    }

    public double getRandomInjuryChance() {
        return randomInjuryChance;
    }

    public void setRandomInjuryChance(double randomInjuryChance) {
        this.randomInjuryChance = randomInjuryChance;
    }

    public int getInjuryDaysLow() {
        return injuryDaysLow;
    }

    public void setInjuryDaysLow(int injuryDaysLow) {
        this.injuryDaysLow = injuryDaysLow;
    }

    public int getInjuryDaysHigh() {
        return injuryDaysHigh;
    }

    public void setInjuryDaysHigh(int injuryDaysHigh) {
        this.injuryDaysHigh = injuryDaysHigh;
    }


}
