package parking.model;
public class DeliveryVan extends Vehicle {
    public DeliveryVan(String regNo, String owner) { super(regNo, owner); }
    @Override public double getHourlyRate() { return 30.0; }
    @Override public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        return ((minutes + 59) / 60) * getHourlyRate();
    }
    /** Doubles the rate for each billable hour block starting from 09:00 through 16:59. */
    public double calculateFee(int minutes, int entryHour) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        if (entryHour < 0 || entryHour > 23) throw new IllegalArgumentException("Invalid hour");
        int hours = (minutes + 59) / 60;
        double total = 0.0;
        for (int i = 0; i < hours; i++) {
            int hourOfDay = (entryHour + i) % 24;
            double multiplier = (hourOfDay >= 9 && hourOfDay < 17) ? 2.0 : 1.0;
            total += getHourlyRate() * multiplier;
        }
        return total;
    }
}
