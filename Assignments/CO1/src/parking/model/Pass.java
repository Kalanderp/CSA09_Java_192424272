package parking.model;
import java.time.LocalDate;
public interface Pass {
    boolean isValidOn(LocalDate date);
    String getPassType();
}
