public class App {

    // Inner class to represent Feet
    static class Feet {
        private final double value;

        // Constructor
        public Feet(double value) {
            this.value = value;
        }

        // Override equals method
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Feet other = (Feet) obj;

            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Main method
    public static void main(String[] args) {

        System.out.println("Welcome to Quantity Measurement App");

        // Create objects
        Feet f1 = new Feet(5.0);
        Feet f2 = new Feet(5.0);

        // Compare and print result
        System.out.println("Are the two values equal? " + f1.equals(f2));
    }
}