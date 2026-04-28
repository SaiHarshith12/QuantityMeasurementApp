// ---------------- INTERFACE ----------------

interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

// ---------------- LENGTH ----------------

enum LengthUnit implements IMeasurable {
    FEET(1.0), INCHES(1.0 / 12.0), YARDS(3.0), CENTIMETERS(0.0328084);

    private final double factor;

    LengthUnit(double factor) { this.factor = factor; }

    public double getConversionFactor() { return factor; }
    public double convertToBaseUnit(double value) { return value * factor; }
    public double convertFromBaseUnit(double base) { return base / factor; }
    public String getUnitName() { return name(); }
}

// ---------------- WEIGHT ----------------

enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0), GRAM(0.001), POUND(0.453592);

    private final double factor;

    WeightUnit(double factor) { this.factor = factor; }

    public double getConversionFactor() { return factor; }
    public double convertToBaseUnit(double value) { return value * factor; }
    public double convertFromBaseUnit(double base) { return base / factor; }
    public String getUnitName() { return name(); }
}

// ---------------- VOLUME ----------------

enum VolumeUnit implements IMeasurable {
    LITRE(1.0), MILLILITRE(0.001), GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) { this.factor = factor; }

    public double getConversionFactor() { return factor; }
    public double convertToBaseUnit(double value) { return value * factor; }
    public double convertFromBaseUnit(double base) { return base / factor; }
    public String getUnitName() { return name(); }
}

// ---------------- GENERIC QUANTITY ----------------

class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit = unit;
    }

    // ---------- ARITHMETIC ENUM ----------
    private enum ArithmeticOperation {
        ADD, SUBTRACT, DIVIDE
    }

    // ---------- CENTRAL HELPER ----------
    private double operate(Quantity<U> other, ArithmeticOperation op) {

        if (other == null) throw new IllegalArgumentException("Null operand");

        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Different measurement categories");
        }

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        switch (op) {
            case ADD:
                return base1 + base2;
            case SUBTRACT:
                return base1 - base2;
            case DIVIDE:
                if (base2 == 0) throw new ArithmeticException("Division by zero");
                return base1 / base2;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    // ---------- EQUALITY ----------
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Quantity<?> other = (Quantity<?>) obj;

        if (this.unit.getClass() != other.unit.getClass()) return false;

        return Double.compare(
                unit.convertToBaseUnit(value),
                other.unit.convertToBaseUnit(other.value)
        ) == 0;
    }

    // ---------- CONVERSION ----------
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Invalid unit");

        double base = unit.convertToBaseUnit(value);
        double result = targetUnit.convertFromBaseUnit(base);

        result = Math.round(result * 100.0) / 100.0;

        return new Quantity<>(result, targetUnit);
    }

    // ---------- ADD ----------
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double baseResult = operate(other, ArithmeticOperation.ADD);

        double result = targetUnit.convertFromBaseUnit(baseResult);
        result = Math.round(result * 100.0) / 100.0;

        return new Quantity<>(result, targetUnit);
    }

    // ---------- SUBTRACT ----------
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        double baseResult = operate(other, ArithmeticOperation.SUBTRACT);

        double result = targetUnit.convertFromBaseUnit(baseResult);
        result = Math.round(result * 100.0) / 100.0;

        return new Quantity<>(result, targetUnit);
    }

    // ---------- DIVIDE ----------
    public double divide(Quantity<U> other) {
        return operate(other, ArithmeticOperation.DIVIDE);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(unit.convertToBaseUnit(value));
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}

// ---------------- MAIN ----------------

public class App {

    public static void main(String[] args) {

        System.out.println("Welcome to Final Quantity App (UC12)");

        Quantity<LengthUnit> l1 = new Quantity<>(2, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCHES);

        System.out.println("Add: " + l1.add(l2));
        System.out.println("Subtract: " + l1.subtract(l2));
        System.out.println("Divide: " + l1.divide(l2));

        Quantity<WeightUnit> w1 = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(500, WeightUnit.GRAM);

        System.out.println("Weight Add: " + w1.add(w2));
        System.out.println("Weight Divide: " + w1.divide(w2));

        Quantity<VolumeUnit> v1 = new Quantity<>(1, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(500, VolumeUnit.MILLILITRE);

        System.out.println("Volume Add: " + v1.add(v2));
        System.out.println("Volume Subtract: " + v1.subtract(v2));
    }
}