package parking.pricing;
import parking.model.Vehicle;
public interface PricingRule {
    double calculate(Vehicle vehicle, int minutes, int entryHour);
    String description();
}
