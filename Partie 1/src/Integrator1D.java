/**
 * Classe utilitaire qui regroupe plusieurs méthodes d'intégration numérique 1D :
 *
 *  - Méthode de Riemann (somme à gauche)
 *  - Méthode des trapèzes
 *  - Méthode de Simpson (ordre 2)
 *
 * Chaque méthode prend en entrée :
 *  - une fonction f (UnivariateFunction)
 *  - un intervalle [a, b]
 *  - un nombre de sous-intervalles n
 *
 * et renvoie une approximation de l'intégrale :
 *      ∫_a^b f(x) dx
 */
public final class Integrator1D {

    /**
     * Constructeur privé : cette classe ne doit pas être instanciée.
     * On l'utilise uniquement avec ses méthodes statiques.
     */
    private Integrator1D() {
    }

    // ---------------------------------------------------------------------
    // 1) Méthode de Riemann (somme à gauche)
    // ---------------------------------------------------------------------

    /**
     * Approxime l'intégrale de f sur [a, b] par la méthode de Riemann (somme à gauche).
     *
     * Idée :
     *  - On découpe [a, b] en n sous-intervalles de longueur h = (b - a) / n.
     *  - Sur chaque sous-intervalle [x_i, x_{i+1}], on prend le point de GAUCHE x_i.
     *  - On additionne f(x_i) * h.
     *
     * Formule :
     *      ∫_a^b f(x) dx ≈ h * Σ_{i=0}^{n-1} f(a + i*h)
     *
     * @param f fonction à intégrer
     * @param a borne inférieure de l'intervalle
     * @param b borne supérieure de l'intervalle
     * @param n nombre de sous-intervalles (n > 0)
     * @return approximation de l'intégrale
     */
    public static double riemannLeft(UnivariateFunction f, double a, double b, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n doit être strictement positif.");
        }

        // largeur de chaque sous-intervalle
        double h = (b - a) / n;

        // somme des valeurs f(x_i)
        double sum = 0.0;

        // on parcourt i = 0 .. n-1
        for (int i = 0; i < n; i++) {
            // point de gauche de l'intervalle i-ème : x_i = a + i*h
            double x = a + i * h;
            // on ajoute la contribution f(x_i)
            sum += f.value(x);
        }

        // on multiplie la somme par la largeur h
        return h * sum;
    }

    // ---------------------------------------------------------------------
    // 2) Méthode des trapèzes
    // ---------------------------------------------------------------------

    /**
     * Approxime l'intégrale de f sur [a, b] par la méthode des trapèzes.
     *
     * Idée :
     *  - On découpe [a, b] en n sous-intervalles de longueur h = (b - a) / n.
     *  - Sur chaque petit intervalle, on approxime la courbe par une droite
     *    entre (x_i, f(x_i)) et (x_{i+1}, f(x_{i+1})).
     *  - L'aire sous cette droite est l'aire d'un trapèze.
     *
     * Formule pratique :
     *      ∫_a^b f(x) dx ≈ h * [ 0.5*f(a) + 0.5*f(b) + Σ_{i=1}^{n-1} f(a + i*h) ]
     *
     * @param f fonction à intégrer
     * @param a borne inférieure de l'intervalle
     * @param b borne supérieure de l'intervalle
     * @param n nombre de sous-intervalles (n > 0)
     * @return approximation de l'intégrale
     */
    public static double trapezoidal(UnivariateFunction f, double a, double b, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n doit être strictement positif.");
        }

        double h = (b - a) / n;

        // on commence par 0.5*f(a) + 0.5*f(b)
        double sum = 0.5 * (f.value(a) + f.value(b));

        // on ajoute les points internes
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += f.value(x);
        }

        return h * sum;
    }

    // ---------------------------------------------------------------------
    // 3) Méthode de Simpson (ordre 2)
    // ---------------------------------------------------------------------

    /**
     * Approxime l'intégrale de f sur [a, b] par la méthode de Simpson (ordre 2).
     *
     * **Attention : n doit être PAIR.**
     *
     * Idée :
     *  - On découpe [a, b] en n sous-intervalles (n pair), largeur h.
     *  - On utilise une parabole (polynôme de degré 2) sur chaque paire de sous-intervalles.
     *  - On obtient la formule :
     *
     *      ∫_a^b f(x) dx ≈ (h / 3) * [ f(a) + f(b)
     *                                  + 4 * Σ f(x_i) (indices impairs)
     *                                  + 2 * Σ f(x_i) (indices pairs internes) ]
     *
     * @param f fonction à intégrer
     * @param a borne inférieure de l'intervalle
     * @param b borne supérieure de l'intervalle
     * @param n nombre de sous-intervalles (doit être pair)
     * @return approximation de l'intégrale
     */
    public static double simpson(UnivariateFunction f, double a, double b, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n doit être strictement positif.");
        }
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n doit être PAIR pour la méthode de Simpson.");
        }

        double h = (b - a) / n;

        // on commence par f(a) + f(b)
        double sum = f.value(a) + f.value(b);

        // terme avec coefficient 4 : indices impairs (1, 3, 5, ..., n-1)
        for (int i = 1; i < n; i += 2) {
            double x = a + i * h;
            sum += 4.0 * f.value(x);
        }

        // terme avec coefficient 2 : indices pairs internes (2, 4, ..., n-2)
        for (int i = 2; i < n; i += 2) {
            double x = a + i * h;
            sum += 2.0 * f.value(x);
        }

        return (h / 3.0) * sum;
    }
}