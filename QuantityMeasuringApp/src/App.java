// ---------------- INTERFACE ----------------

interface IMeasurable {

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();
}

// ---------------- LENGTH ENUM ----------------

enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.0328084);

    private final double factor;

    LengthUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    public String getUnitName() {
        return name();
    }
}

// ---------------- WEIGHT ENUM ----------------
enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double getConversionFactor() {
        return factor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * factor; // → litres
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor; // litres → unit
    }

    @Override
    public String getUnitName() {
        return name();
    }
}

enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    public String getUnitName() {
        return name();
    }
}

// ---------------- GENERIC QUANTITY ----------------

class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
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

        Quantity<?> other = (Quantity<?>) obj;

        // Prevent cross-category comparison
        if (this.unit.getClass() != other.unit.getClass()) return false;

        double thisBase = unit.convertToBaseUnit(value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return Double.compare(thisBase, otherBase) == 0;
    }

    // Conversion
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double base = unit.convertToBaseUnit(value);
        double result = targetUnit.convertFromBaseUnit(base);

        // Round to 2 decimal places
        result = Math.round(result * 100.0) / 100.0;

        return new Quantity<>(result, targetUnit);
    }

    // Addition (same unit as first)
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    // Addition (explicit target unit)
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double sumBase =
                this.unit.convertToBaseUnit(this.value) +
                        other.unit.convertToBaseUnit(other.value);

        double result = targetUnit.convertFromBaseUnit(sumBase);

        result = Math.round(result * 100.0) / 100.0;

        return new Quantity<>(result, targetUnit);
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

        System.out.println("Welcome to Generic Quantity Measurement App");
        // -------- VOLUME --------
        Quantity<VolumeUnit> v1 = new Quantity<>(1, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> v3 = new Quantity<>(1, VolumeUnit.GALLON);

// Equality
        System.out.println("Volume equality (1L == 1000mL): " + v1.equals(v2));

// Conversion
        System.out.println("1 gallon to litre: " + v3.convertTo(VolumeUnit.LITRE));

// Addition (default unit)
        System.out.println("Addition (L): " + v1.add(v2));

// Addition (explicit unit)
        System.out.println("Addition (to mL): " + v1.add(v3, VolumeUnit.MILLILITRE));

// Cross-category safety
        System.out.println("Volume vs Length: " +
                v1.equals(new Quantity<>(1, LengthUnit.FEET))); // false

        // -------- LENGTH --------
        Quantity<LengthUnit> l1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCHES);

        System.out.println("Length equality: " + l1.equals(l2));
        System.out.println("Length conversion: " + l1.convertTo(LengthUnit.INCHES));
        System.out.println("Length addition: " + l1.add(l2, LengthUnit.FEET));

        // -------- WEIGHT --------
        Quantity<WeightUnit> w1 = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000, WeightUnit.GRAM);

        System.out.println("Weight equality: " + w1.equals(w2));
        System.out.println("Weight conversion: " + w1.convertTo(WeightUnit.POUND));
        System.out.println("Weight addition: " + w1.add(w2, WeightUnit.KILOGRAM));

        // -------- TYPE SAFETY --------
        System.out.println("Cross comparison (Length vs Weight): " +
                l1.equals(w1)); // false
    }
}