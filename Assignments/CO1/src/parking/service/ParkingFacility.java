package parking.service;

import parking.model.*;
import parking.pricing.*;
import java.time.LocalDate;
import java.util.*;

/** Facility with variable slot counts per floor and registration-indexed active tickets. */
public class ParkingFacility {
    private final boolean[][] occupied;
    private final Map<String, ParkingTicket> activeTickets = new LinkedHashMap<>();
    private final PricingRule pricingRule;
    private int occupancy;

    public ParkingFacility(int[] slotsPerFloor) { this(slotsPerFloor, new StandardPricingRule()); }
    public ParkingFacility(int[] slotsPerFloor, PricingRule pricingRule) {
        if (slotsPerFloor == null || slotsPerFloor.length == 0)
            throw new IllegalArgumentException("At least one floor is required");
        this.occupied = new boolean[slotsPerFloor.length][];
        for (int i=0; i<slotsPerFloor.length; i++) {
            if (slotsPerFloor[i] <= 0) throw new IllegalArgumentException("Each floor needs slots");
            occupied[i] = new boolean[slotsPerFloor[i]];
        }
        this.pricingRule = Objects.requireNonNull(pricingRule);
    }
    public synchronized ParkingTicket enter(Vehicle vehicle) {
        Objects.requireNonNull(vehicle);
        String key = vehicle.getRegistrationNumber();
        if (activeTickets.containsKey(key)) throw new IllegalArgumentException("Vehicle is already parked");
        for (int f=0; f<occupied.length; f++) {
            for (int s=0; s<occupied[f].length; s++) {
                if (!occupied[f][s]) {
                    occupied[f][s] = true; occupancy++;
                    ParkingTicket ticket = new ParkingTicket(vehicle, f, s);
                    activeTickets.put(key, ticket);
                    return ticket;
                }
            }
        }
        return null;
    }
    public synchronized double exit(String registrationNumber, int minutes, int entryHour,
                                    Pass pass, LocalDate exitDate, double energyKWh) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        String key = registrationNumber.toUpperCase();
        ParkingTicket ticket = activeTickets.get(key);
        if (ticket == null) throw new IllegalArgumentException("No active ticket for vehicle");
        Vehicle vehicle = ticket.getVehicle();
        double fee = 0.0;
        if (pass == null || !pass.isValidOn(exitDate)) {
            fee = pricingRule.calculate(vehicle, minutes, entryHour);
            if (vehicle instanceof Chargeable c) fee += c.chargingFee(energyKWh);
        }
        occupied[ticket.getFloor()][ticket.getSlot()] = false;
        activeTickets.remove(key);
        occupancy--;
        return fee;
    }
    public synchronized boolean release(int floor, int slot) {
        if (floor < 0 || floor >= occupied.length || slot < 0 || slot >= occupied[floor].length)
            return false;
        if (!occupied[floor][slot]) return false;
        occupied[floor][slot] = false; occupancy--;
        return true;
    }
    public int getOccupancy() { return occupancy; }
    public int getFloorCount() { return occupied.length; }
    public int getSlotsOnFloor(int floor) { return occupied[floor].length; }
    public int getTotalSlots() { return Arrays.stream(occupied).mapToInt(a -> a.length).sum(); }
    public ParkingTicket findTicket(String registrationNumber) {
        return activeTickets.get(registrationNumber.toUpperCase());
    }
}
