public class App {

    // Step 1: Updated Enum
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),          // 1 inch = 1/12 feet
        YARD(3.0),                 // 1 yard = 3 feet
        CM(0.0328084);             // 1 cm ≈ 0.0328084 feet

        private final double toFeet;

        LengthUnit(double toFeet) {
            this.toFeet = toFeet;
        }

        public double toFeet(double value) {
            return value * toFeet;
        }
    }

    // Step 2: QuantityLength class (UNCHANGED)
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

            double thisInFeet = this.unit.toFeet(this.value);
            double otherInFeet = other.unit.toFeet(other.value);

            return Double.compare(thisInFeet, otherInFeet) == 0;
        }
    }

    // Main method (test cases)
    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        // Feet ↔ Feet
        System.out.println(new QuantityLength(5, LengthUnit.FEET)
                .equals(new QuantityLength(5, LengthUnit.FEET))); // true

        // Inches ↔ Feet (12 in = 1 ft)
        System.out.println(new QuantityLength(12, LengthUnit.INCH)
                .equals(new QuantityLength(1, LengthUnit.FEET))); // true

        // Yard ↔ Feet (1 yd = 3 ft)
        System.out.println(new QuantityLength(1, LengthUnit.YARD)
                .equals(new QuantityLength(3, LengthUnit.FEET))); // true

        // Yard ↔ Inches (1 yd = 36 in)
        System.out.println(new QuantityLength(1, LengthUnit.YARD)
                .equals(new QuantityLength(36, LengthUnit.INCH))); // true

        // CM ↔ Inches (approx)
        System.out.println(new QuantityLength(2.54, LengthUnit.CM)
                .equals(new QuantityLength(1, LengthUnit.INCH))); // true

        // CM ↔ Feet
        System.out.println(new QuantityLength(30.48, LengthUnit.CM)
                .equals(new QuantityLength(1, LengthUnit.FEET))); // true
    }
}