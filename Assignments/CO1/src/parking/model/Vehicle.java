package parking.model;

import java.util.Objects;

/** Common state and behaviour shared by every vehicle in the parking system. */
public abstract class Vehicle {
    private final String registrationNumber;
    private final String ownerName;

    protected Vehicle(String registrationNumber, String ownerName) {
        if (registrationNumber == null || registrationNumber.isBlank())
            throw new IllegalArgumentException("Registration number is required");
        if (ownerName == null || ownerName.isBlank())
            throw new IllegalArgumentException("Owner name is required");
        this.registrationNumber = registrationNumber.trim().toUpperCase();
        this.ownerName = ownerName.trim();
    }
    public final String getRegistrationNumber() { return registrationNumber; }
    public final String getOwnerName() { return ownerName; }
    public abstract double getHourlyRate();
    public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        int billableHours = (minutes + 59) / 60; // every started hour is charged
        return billableHours * getHourlyRate();
    }
    @Override public String toString() {
        return getClass().getSimpleName() + "[" + registrationNumber + ", owner=" + ownerName + "]";
    }
    @Override public final boolean equals(Object other) {
        return other instanceof Vehicle v &&
               registrationNumber.equals(v.registrationNumber);
    }
    @Override public final int hashCode() { return Objects.hash(registrationNumber); }
}
