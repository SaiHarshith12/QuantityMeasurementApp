public class App {

    // Enum with conversion factors (to FEET)
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.0328084);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }
    }

    // QuantityLength class
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid numeric value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // UC5: Static conversion method
        public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {

            // Validation
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be finite");
            }
            if (sourceUnit == null || targetUnit == null) {
                throw new IllegalArgumentException("Units cannot be null");
            }

            // Step 1: Convert to base unit (feet)
            double valueInFeet = sourceUnit.toFeet(value);

            // Step 2: Convert from feet to target unit
            double result = targetUnit.fromFeet(valueInFeet);

            return result;
        }

        // Equality (from UC3/UC4)
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            double thisFeet = this.unit.toFeet(this.value);
            double otherFeet = other.unit.toFeet(other.value);

            return Double.compare(thisFeet, otherFeet) == 0;
        }
    }

    // Main method (test conversion)
    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        // Test conversions
        System.out.println("12 inches to feet: " +
                QuantityLength.convert(12, LengthUnit.INCHES, LengthUnit.FEET));

        System.out.println("1 yard to inches: " +
                QuantityLength.convert(1, LengthUnit.YARDS, LengthUnit.INCHES));

        System.out.println("2.54 cm to inches: " +
                QuantityLength.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES));

        System.out.println("1 foot to cm: " +
                QuantityLength.convert(1, LengthUnit.FEET, LengthUnit.CENTIMETERS));
    }
}