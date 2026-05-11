public abstract class ShipmentOrder implements SummaryPrintable {

    protected String orderNumber;
    protected String customerName;
    protected double distanceKm;
    protected double baseFee;
    protected boolean insured;
    protected double lastCalculatedPrice;

    public ShipmentOrder(String orderNumber, String customerName, double distanceKm,
                         double baseFee, boolean insured) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.distanceKm = distanceKm;
        this.baseFee = baseFee;
        this.insured = insured;
    }

    // --- Getters ---
    public String getOrderNumber()       { return orderNumber; }
    public String getCustomerName()      { return customerName; }
    public double getDistanceKm()        { return distanceKm; }
    public double getBaseFee()           { return baseFee; }
    public boolean isInsured()           { return insured; }
    public double getLastCalculatedPrice() { return lastCalculatedPrice; }

    // --- Template Method ---
    public final void processOrder() {
        validateOrder();
        validateSpecificRules();

        double price = calculateBasePrice();
        price += calculateAdditionalFee();
        price = applyInsurance(price);
        price = applyBusinessDiscount(price);

        lastCalculatedPrice = price;
        printProcessingResult();
    }

    // --- Common private steps ---
    private void validateOrder() {
        if (orderNumber == null || orderNumber.isBlank()) {
            throw new IllegalArgumentException("Order number cannot be empty.");
        }
        if (distanceKm <= 0) {
            throw new IllegalArgumentException("Distance must be positive.");
        }
    }

    private double applyInsurance(double price) {
        if (insured) {
            price *= 1.07;
        }
        return price;
    }

    private void printProcessingResult() {
        System.out.printf("Processed order %s for %s | Type: %s | Price: %.2f PLN%n",
                orderNumber, customerName, getShipmentType(), lastCalculatedPrice);
    }

    // --- Hook methods (optional override) ---
    protected void validateSpecificRules() {
        // empty default implementation
    }

    protected double applyBusinessDiscount(double price) {
        return price;
    }

    // --- Abstract methods ---
    protected abstract double calculateBasePrice();
    protected abstract double calculateAdditionalFee();
    public abstract String getShipmentType();

    // --- SummaryPrintable ---
    @Override
    public String buildSummaryLine() {
        return String.format("SUMMARY | Order: %s | Customer: %s | Type: %s | Final price: %.2f PLN",
                orderNumber, customerName, getShipmentType(), lastCalculatedPrice);
    }
}
