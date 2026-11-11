package parking.model;
public class EBike extends Vehicle implements Chargeable {
    private static final double FLAT_FEE = 25.0;
    public EBike(String regNo, String owner) { super(regNo, owner); }
    @Override public double getHourlyRate() { return FLAT_FEE; }
    @Override public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        return minutes == 0 ? 0.0 : FLAT_FEE;
    }
    @Override public double chargingFee(double kWh) {
        if (kWh < 0) throw new IllegalArgumentException("Energy cannot be negative");
        return kWh * 5.0;
    }
}
