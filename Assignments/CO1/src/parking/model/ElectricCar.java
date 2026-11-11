package parking.model;
public class ElectricCar extends Car implements Chargeable {
    public ElectricCar(String regNo, String owner) { super(regNo, owner); }
    @Override public double chargingFee(double kWh) {
        if (kWh < 0) throw new IllegalArgumentException("Energy cannot be negative");
        return kWh * 12.0;
    }
}
