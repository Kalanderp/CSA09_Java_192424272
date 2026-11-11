package parking.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public final class ParkingTicket {
    private static final AtomicLong NEXT_ID = new AtomicLong(1000);
    private final long ticketNumber;
    private final Vehicle vehicle;
    private final int floor;
    private final int slot;
    private final LocalDateTime entryTime;

    public ParkingTicket(Vehicle vehicle, int floor, int slot) {
        this(NEXT_ID.incrementAndGet(), vehicle, floor, slot, LocalDateTime.now());
    }
    public ParkingTicket(long id, Vehicle vehicle, int floor, int slot, LocalDateTime entryTime) {
        if (vehicle == null || entryTime == null || floor < 0 || slot < 0)
            throw new IllegalArgumentException("Invalid ticket data");
        this.ticketNumber = id;
        this.vehicle = vehicle;
        this.floor = floor;
        this.slot = slot;
        this.entryTime = entryTime;
    }
    public long getTicketNumber() { return ticketNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }
}
