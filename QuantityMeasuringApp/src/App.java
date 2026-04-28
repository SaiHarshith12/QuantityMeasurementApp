public class App {

    // Enum (same as UC5)
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
                throw new IllegalArgumentException("Invalid value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // UC6: Add method
        public static QuantityLength add(QuantityLength l1, QuantityLength l2) {

            // Validation
            if (l1 == null || l2 == null) {
                throw new IllegalArgumentException("Length cannot be null");
            }

            // Convert both to feet
            double l1Feet = l1.unit.toFeet(l1.value);
            double l2Feet = l2.unit.toFeet(l2.value);

            // Add
            double sumFeet = l1Feet + l2Feet;

            // Convert back to unit of first operand
            double resultValue = l1.unit.fromFeet(sumFeet);

            return new QuantityLength(resultValue, l1.unit);
        }

        // Equals (same as before)
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            double thisFeet = this.unit.toFeet(this.value);
            double otherFeet = other.unit.toFeet(other.value);

            return Double.compare(thisFeet, otherFeet) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // Main method (test)
    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        QuantityLength l1 = new QuantityLength(1, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(12, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(l1, l2);

        System.out.println("Result: " + result); // 2 FEET
    }
}