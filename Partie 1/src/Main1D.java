/**
 * Programme de test pour les méthodes d'intégration numérique 1D.
 *
 * On teste les méthodes sur la fonction :
 *      f(x) = exp(-x^2)
 *
 * et on calcule l'intégrale sur [0, b] pour :
 *      b = 1, 10, 100
 *
 * avec les méthodes :
 *  - Riemann (gauche)
 *  - Trapèzes
 *  - Simpson
 */
public class Main1D {

    public static void main(String[] args) {
        // -----------------------------------------------------------------
        // 1) Définition de la fonction f(x) = exp(-x^2)
        // -----------------------------------------------------------------
        UnivariateFunction f = x -> Math.exp(-x * x);

        // -----------------------------------------------------------------
        // 2) Bornes supérieures b à tester : 1, 10, 100
        // -----------------------------------------------------------------
        double[] bs = {1.0, 10.0, 100.0};

        // -----------------------------------------------------------------
        // 3) Nombre de sous-intervalles n
        //
        //    Plus n est grand, plus l'approximation est précise, mais plus
        //    le calcul est long. On peut ajuster en fonction du temps de calcul.
        // -----------------------------------------------------------------
        int n = 100_000; // 100 000 sous-intervalles

        // -----------------------------------------------------------------
        // 4) Boucle sur les différentes valeurs de b
        // -----------------------------------------------------------------
        for (double b : bs) {
            double a = 0.0; // borne inférieure

            System.out.println("==============================================");
            System.out.println("Intégrale de exp(-x^2) sur [0, " + b + "]");
            System.out.println("n = " + n + " sous-intervalles");
            System.out.println("----------------------------------------------");

            // Méthode de Riemann (gauche)
            double riemann = Integrator1D.riemannLeft(f, a, b, n);
            System.out.println("Riemann (gauche) : " + riemann);

            // Méthode des trapèzes
            double trapeze = Integrator1D.trapezoidal(f, a, b, n);
            System.out.println("Trapèzes         : " + trapeze);

            // Méthode de Simpson (on force n pair pour être sûr)
            int nSimpson = (n % 2 == 0) ? n : n - 1;
            double simpson = Integrator1D.simpson(f, a, b, nSimpson);
            System.out.println("Simpson          : " + simpson);

            System.out.println();
        }
    }
}