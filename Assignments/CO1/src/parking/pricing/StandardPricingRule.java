package parking.pricing;
import parking.model.*;
public class StandardPricingRule implements PricingRule {
    @Override public double calculate(Vehicle vehicle, int minutes, int entryHour) {
        if (vehicle instanceof DeliveryVan van) return van.calculateFee(minutes, entryHour);
        return vehicle.calculateFee(minutes);
    }
    @Override public String description() { return "Vehicle-specific hourly rate"; }
}
