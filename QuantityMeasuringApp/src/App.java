// ---------------- LENGTH (UC8) ----------------

enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.0328084);

    private final double factor;

    LengthUnit(double factor) {
        this.factor = factor;
    }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double base) {
        return base / factor;
    }
}

class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (!Double.isFinite(value) || unit == null)
            throw new IllegalArgumentException("Invalid input");
        this.value = value;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuantityLength other = (QuantityLength) obj;

        return Double.compare(
                unit.convertToBaseUnit(value),
                other.unit.convertToBaseUnit(other.value)
        ) == 0;
    }
}

// ---------------- WEIGHT (UC9) ----------------

enum WeightUnit {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double base) {
        return base / factor;
    }
}

class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }

    // Equality
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuantityWeight other = (QuantityWeight) obj;

        double thisBase = unit.convertToBaseUnit(value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return Double.compare(thisBase, otherBase) == 0;
    }

    // Conversion
    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double base = unit.convertToBaseUnit(value);
        double result = targetUnit.convertFromBaseUnit(base);

        return new QuantityWeight(result, targetUnit);
    }

    // Addition (result in first unit)
    public static QuantityWeight add(QuantityWeight w1, QuantityWeight w2) {
        return add(w1, w2, w1.unit);
    }

    // Addition (explicit target unit)
    public static QuantityWeight add(QuantityWeight w1, QuantityWeight w2, WeightUnit targetUnit) {

        if (w1 == null || w2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double sumBase =
                w1.unit.convertToBaseUnit(w1.value) +
                        w2.unit.convertToBaseUnit(w2.value);

        double result = targetUnit.convertFromBaseUnit(sumBase);

        return new QuantityWeight(result, targetUnit);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}

// ---------------- MAIN APP ----------------

public class App {

    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        // Equality
        System.out.println("1 kg == 1000 g: " +
                new QuantityWeight(1, WeightUnit.KILOGRAM)
                        .equals(new QuantityWeight(1000, WeightUnit.GRAM)));

        // Conversion
        System.out.println("1 kg to pound: " +
                new QuantityWeight(1, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.POUND));

        // Addition
        QuantityWeight sum = QuantityWeight.add(
                new QuantityWeight(1, WeightUnit.KILOGRAM),
                new QuantityWeight(500, WeightUnit.GRAM),
                WeightUnit.KILOGRAM
        );
        System.out.println("Addition: " + sum);

        // Category safety check
        System.out.println("Length vs Weight comparison: " +
                new QuantityLength(1, LengthUnit.FEET)
                        .equals(new QuantityWeight(1, WeightUnit.KILOGRAM)));
    }
}