public class Main2D {

    public static void main(String[] args) {

        BivariateFunction f = (x, y) -> Math.exp(-(x * x + y * y));

        double[] bs = {1.0, 10.0, 100.0};

        int n = 2000; // nx = ny = n (à adapter selon machine/temps)
        for (double b : bs) {
            double approx = Integrator2D.riemannLeft2D(f, 0.0, b, 0.0, b, n, n);

            System.out.println("=== ∬ exp(-(x^2+y^2)) sur [0," + b + "]x[0," + b + "] ===");
            System.out.println("nx = ny = " + n);
            System.out.println("Riemann 2D (bas-gauche) : " + approx);
            System.out.println();
        }
    }
}
