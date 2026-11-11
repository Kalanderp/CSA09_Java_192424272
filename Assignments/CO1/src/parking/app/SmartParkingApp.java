package parking.app;

import parking.model.*;
import parking.service.ParkingFacility;
import java.time.LocalDate;

public class SmartParkingApp {
    public static void main(String[] args) {
        ParkingFacility facility = new ParkingFacility(new int[]{2, 3, 2, 4, 3});
        Vehicle[] arrivals = {
            new TwoWheeler("TN01AB1111", "Asha"),
            new Car("TN02CD2222", "Bala"),
            new ElectricCar("TN03EF3333", "Charan"),
            new EBike("TN04GH4444", "Divya"),
            new DeliveryVan("TN05JK5555", "Eshan")
        };
        for (Vehicle v : arrivals) {
            ParkingTicket t = facility.enter(v);
            System.out.println(t == null ? "FULL" :
                v.getClass().getSimpleName()+" "+v.getRegistrationNumber()+" ticket="+t.getTicketNumber()+
                " slot="+t.getFloor()+":"+t.getSlot());
        }
        double carFee = facility.exit("TN02CD2222", 130, 11, null, LocalDate.now(), 0);
        double evFee = facility.exit("TN03EF3333", 130, 11, null, LocalDate.now(), 10);
        double passFee = facility.exit("TN01AB1111", 180, 8,
            new OneDayPass(LocalDate.now()), LocalDate.now(), 0);
        System.out.printf("Car fee: Rs %.2f%n", carFee);
        System.out.printf("Electric car fee including charging: Rs %.2f%n", evFee);
        System.out.printf("One-day pass fee: Rs %.2f%n", passFee);
        System.out.println("Current occupancy: "+facility.getOccupancy());
    }
}
