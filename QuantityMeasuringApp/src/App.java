enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.0328084);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toFeetFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toFeetFactor;
    }
}

// Not public (important!)
class QuantityLength {

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuantityLength other = (QuantityLength) obj;

        double thisBase = unit.convertToBaseUnit(value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return Double.compare(thisBase, otherBase) == 0;
    }

    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value) || source == null || target == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double base = source.convertToBaseUnit(value);
        return target.convertFromBaseUnit(base);
    }

    public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {
        if (l1 == null || l2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double sumBase =
                l1.unit.convertToBaseUnit(l1.value) +
                        l2.unit.convertToBaseUnit(l2.value);

        double result = targetUnit.convertFromBaseUnit(sumBase);

        return new QuantityLength(result, targetUnit);
    }

    public static QuantityLength subtract(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {
        if (l1 == null || l2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double diffBase =
                l1.unit.convertToBaseUnit(l1.value) -
                        l2.unit.convertToBaseUnit(l2.value);

        double result = targetUnit.convertFromBaseUnit(diffBase);

        return new QuantityLength(result, targetUnit);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}

// Only ONE public class allowed
public class App {

    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        System.out.println("12 inches == 1 foot: " +
                new QuantityLength(12, LengthUnit.INCHES)
                        .equals(new QuantityLength(1, LengthUnit.FEET)));

        System.out.println("1 yard to inches: " +
                QuantityLength.convert(1, LengthUnit.YARDS, LengthUnit.INCHES));

        QuantityLength sum = QuantityLength.add(
                new QuantityLength(1, LengthUnit.FEET),
                new QuantityLength(12, LengthUnit.INCHES),
                LengthUnit.FEET
        );
        System.out.println("Addition: " + sum);

        QuantityLength diff = QuantityLength.subtract(
                new QuantityLength(2, LengthUnit.FEET),
                new QuantityLength(12, LengthUnit.INCHES),
                LengthUnit.FEET
        );
        System.out.println("Subtraction: " + diff);
    }
}