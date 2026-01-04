/**
 * Interface qui représente une fonction mathématique d'une variable réelle :
 *      f : R -> R
 *
 * En pratique : on pourra écrire f.value(x) pour obtenir f(x).
 *
 * On la définit comme "fonctionnelle" : on pourra utiliser des lambdas.
 */
@FunctionalInterface
public interface UnivariateFunction {

    /**
     * Calcule la valeur de la fonction en un point x.
     *
     * @param x point où l'on évalue la fonction
     * @return f(x)
     */
    double value(double x);
}