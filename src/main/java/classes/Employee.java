package classes;

public class Employee {
    private String name;
    private int totalSales;
    private int salesPeriod;
    private float experienceMultiplier;
    private float score;

    public Employee(String name, int totalSales, int salesPeriod, float experienceMultiplier) {
        this.name = name;
        this.totalSales = totalSales;
        this.salesPeriod = salesPeriod;
        this.experienceMultiplier = experienceMultiplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getSalesPeriod() {
        return salesPeriod;
    }

    public void setSalesPeriod(int salesPeriod) {
        this.salesPeriod = salesPeriod;
    }

    public double getExperienceMultiplier() {
        return experienceMultiplier;
    }

    public double getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setExperienceMultiplier(float experienceMultiplier) {
        this.experienceMultiplier = experienceMultiplier;
    }
}
