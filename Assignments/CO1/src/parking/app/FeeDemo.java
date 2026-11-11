import parking.model.*;
public class FeeDemo {
    public static void main(String[] args) {
        Vehicle[] parked = {
            new TwoWheeler("TN01A1","A"),
            new Car("TN02B2","B"),
            new ElectricCar("TN03C3","C")
        };
        for (Vehicle v : parked) {
            double fee = v.calculateFee(130);
            if (v instanceof Chargeable c) fee += c.chargingFee(10);
            System.out.println(v.getClass().getSimpleName()+" "+v.getRegistrationNumber()+" -> Rs "+fee);
        }
        System.out.println(new Car("TN02B2","B").equals(new ElectricCar("TN02B2","C")));
    }
}
