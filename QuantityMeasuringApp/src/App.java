public class App {

    // Enum for units (base = FEET)
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

        // UC3/UC4: Equality
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            double thisFeet = this.unit.toFeet(this.value);
            double otherFeet = other.unit.toFeet(other.value);

            return Double.compare(thisFeet, otherFeet) == 0;
        }

        // UC5: Conversion
        public static double convert(double value, LengthUnit source, LengthUnit target) {

            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid value");
            }
            if (source == null || target == null) {
                throw new IllegalArgumentException("Units cannot be null");
            }

            double feet = source.toFeet(value);
            return target.fromFeet(feet);
        }

        // UC6: Addition (with target unit)
        public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {

            if (l1 == null || l2 == null || targetUnit == null) {
                throw new IllegalArgumentException("Invalid input");
            }

            double l1Feet = l1.unit.toFeet(l1.value);
            double l2Feet = l2.unit.toFeet(l2.value);

            double sumFeet = l1Feet + l2Feet;

            double resultValue = targetUnit.fromFeet(sumFeet);

            return new QuantityLength(resultValue, targetUnit);
        }

        // UC7: Subtraction
        public static QuantityLength subtract(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {

            if (l1 == null || l2 == null || targetUnit == null) {
                throw new IllegalArgumentException("Invalid input");
            }

            double l1Feet = l1.unit.toFeet(l1.value);
            double l2Feet = l2.unit.toFeet(l2.value);

            double diffFeet = l1Feet - l2Feet;

            double resultValue = targetUnit.fromFeet(diffFeet);

            return new QuantityLength(resultValue, targetUnit);
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // Main method (demo)
    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        // Equality
        System.out.println("12 inches == 1 foot: " +
                new QuantityLength(12, LengthUnit.INCHES)
                        .equals(new QuantityLength(1, LengthUnit.FEET)));

        // Conversion
        System.out.println("1 yard in inches: " +
                QuantityLength.convert(1, LengthUnit.YARDS, LengthUnit.INCHES));

        // Addition
        QuantityLength sum = QuantityLength.add(
                new QuantityLength(1, LengthUnit.FEET),
                new QuantityLength(12, LengthUnit.INCHES),
                LengthUnit.FEET
        );
        System.out.println("Addition: " + sum);

        // Subtraction
        QuantityLength diff = QuantityLength.subtract(
                new QuantityLength(2, LengthUnit.FEET),
                new QuantityLength(12, LengthUnit.INCHES),
                LengthUnit.FEET
        );
        System.out.println("Subtraction: " + diff);
    }
}