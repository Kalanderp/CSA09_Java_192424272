package parking.model;
import java.time.LocalDate;
public final class MonthlyPass implements Pass {
    private final LocalDate startDate;
    private final LocalDate endDate;
    public MonthlyPass(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate))
            throw new IllegalArgumentException("Invalid monthly pass period");
        this.startDate = startDate; this.endDate = endDate;
    }
    @Override public boolean isValidOn(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    @Override public String getPassType() { return "MONTHLY"; }
}
