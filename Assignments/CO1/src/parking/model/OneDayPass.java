package parking.model;
import java.time.LocalDate;
public final class OneDayPass implements Pass {
    private final LocalDate validDate;
    public OneDayPass(LocalDate validDate) {
        if (validDate == null) throw new IllegalArgumentException("Pass date required");
        this.validDate = validDate;
    }
    @Override public boolean isValidOn(LocalDate date) { return validDate.equals(date); }
    @Override public String getPassType() { return "ONE_DAY"; }
}
