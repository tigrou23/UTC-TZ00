public class Integrator2D {

    public static double riemannLeft2D(BivariateFunction f,
                                       double a, double b,
                                       double c, double d,
                                       int nx, int ny) {

        double hx = (b - a) / nx;
        double hy = (d - c) / ny;

        double sum = 0.0;

        for (int i = 0; i < nx; i++) {
            double x = a + i * hx;
            for (int j = 0; j < ny; j++) {
                double y = c + j * hy;
                sum += f.value(x, y);
            }
        }

        return hx * hy * sum;
    }
}