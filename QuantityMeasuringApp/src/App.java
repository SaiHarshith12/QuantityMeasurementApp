public class App {

    // Step 1: Enum for units
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0); // 1 inch = 1/12 feet

        private final double toFeet;

        LengthUnit(double toFeet) {
            this.toFeet = toFeet;
        }

        public double toFeet(double value) {
            return value * toFeet;
        }
    }

    // Step 2: Quantity Length class
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            this.value = value;
            this.unit = unit;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            // Convert both to feet
            double thisInFeet = this.unit.toFeet(this.value);
            double otherInFeet = other.unit.toFeet(other.value);

            return Double.compare(thisInFeet, otherInFeet) == 0;
        }
    }

    // Main method
    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        // Same unit comparison
        QuantityLength l1 = new QuantityLength(5, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(5, LengthUnit.FEET);

        // Different unit comparison (12 inches = 1 foot)
        QuantityLength l3 = new QuantityLength(12, LengthUnit.INCH);
        QuantityLength l4 = new QuantityLength(1, LengthUnit.FEET);

        // Results
        System.out.println("Feet vs Feet: " + l1.equals(l2));     // true
        System.out.println("Inch vs Feet: " + l3.equals(l4));     // true
    }
}