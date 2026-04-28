public class App {

    // Feet class
    static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Inches class
    static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Static method for Feet comparison
    public static boolean compareFeet(double v1, double v2) {
        Feet f1 = new Feet(v1);
        Feet f2 = new Feet(v2);
        return f1.equals(f2);
    }

    // Static method for Inches comparison
    public static boolean compareInches(double v1, double v2) {
        Inches i1 = new Inches(v1);
        Inches i2 = new Inches(v2);
        return i1.equals(i2);
    }

    // Main method
    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        // Feet comparison
        System.out.println("Feet equal: " + compareFeet(5.0, 5.0));
        System.out.println("Feet equal: " + compareFeet(5.0, 6.0));

        // Inches comparison
        System.out.println("Inches equal: " + compareInches(12.0, 12.0));
        System.out.println("Inches equal: " + compareInches(12.0, 10.0));
    }
}