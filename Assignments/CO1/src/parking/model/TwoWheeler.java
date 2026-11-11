package parking.model;
public class TwoWheeler extends Vehicle {
    public TwoWheeler(String regNo, String owner) { super(regNo, owner); }
    @Override public double getHourlyRate() { return 10.0; }
}
