// ---------------- INTERFACE ----------------

interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

// ---------------- LENGTH ----------------

enum LengthUnit implements IMeasurable {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.0328084);

    private final double factor;

    LengthUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() { return factor; }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double base) {
        return base / factor;
    }

    public String getUnitName() { return name(); }
}

// ---------------- WEIGHT ----------------

enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() { return factor; }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double base) {
        return base / factor;
    }

    public String getUnitName() { return name(); }
}

// ---------------- VOLUME ----------------

enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() { return factor; }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double base) {
        return base / factor;
    }

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

    // Equality
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

    // Conversion
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Invalid unit");

        double base = unit.convertToBaseUnit(value);
        double result = targetUnit.convertFromBaseUnit(base);

        result = Math.round(result * 100.0) / 100.0;

        return new Quantity<>(result, targetUnit);
    }

    // Addition
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        double sumBase =
                unit.convertToBaseUnit(value) +
                        other.unit.convertToBaseUnit(other.value);

        double result = targetUnit.convertFromBaseUnit(sumBase);

        result = Math.round(result * 100.0) / 100.0;

        return new Quantity<>(result, targetUnit);
    }

    // Subtraction
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        double diffBase =
                unit.convertToBaseUnit(value) -
                        other.unit.convertToBaseUnit(other.value);

        double result = targetUnit.convertFromBaseUnit(diffBase);

        result = Math.round(result * 100.0) / 100.0;

        return new Quantity<>(result, targetUnit);
    }

    // Division
    public double divide(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Invalid input");

        double divisor = other.unit.convertToBaseUnit(other.value);

        if (divisor == 0) throw new ArithmeticException("Division by zero");

        double dividend = unit.convertToBaseUnit(value);

        return dividend / divisor;
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

// ---------------- MAIN APP ----------------

public class App {

    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        // -------- LENGTH --------
        Quantity<LengthUnit> l1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCHES);

        System.out.println("Length Equality: " + l1.equals(l2));
        System.out.println("Length Conversion: " + l1.convertTo(LengthUnit.INCHES));
        System.out.println("Length Addition: " + l1.add(l2));
        System.out.println("Length Subtraction: " + l1.subtract(l2));
        System.out.println("Length Division: " + l1.divide(l2));

        // -------- WEIGHT --------
        Quantity<WeightUnit> w1 = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000, WeightUnit.GRAM);

        System.out.println("Weight Equality: " + w1.equals(w2));
        System.out.println("Weight Conversion: " + w1.convertTo(WeightUnit.POUND));
        System.out.println("Weight Addition: " + w1.add(w2));
        System.out.println("Weight Subtraction: " + w1.subtract(w2));
        System.out.println("Weight Division: " + w1.divide(w2));

        // -------- VOLUME --------
        Quantity<VolumeUnit> v1 = new Quantity<>(1, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000, VolumeUnit.MILLILITRE);

        System.out.println("Volume Equality: " + v1.equals(v2));
        System.out.println("Volume Conversion: " + v1.convertTo(VolumeUnit.GALLON));
        System.out.println("Volume Addition: " + v1.add(v2));
        System.out.println("Volume Subtraction: " + v1.subtract(v2));
        System.out.println("Volume Division: " + v1.divide(v2));

        // -------- TYPE SAFETY --------
        System.out.println("Length vs Weight: " + l1.equals(w1)); // false
    }
}