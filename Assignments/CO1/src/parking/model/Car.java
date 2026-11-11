package parking.model;
public class Car extends Vehicle {
    public Car(String regNo, String owner) { super(regNo, owner); }
    @Override public double getHourlyRate() { return 30.0; }
}
