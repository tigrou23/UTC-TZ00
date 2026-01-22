# Partie 2 – Intégration numérique en 2D, coordonnées polaires et Green–Riemann

Ce document explique **pas à pas** la partie 2 du projet : intégrales doubles en dimension 2, calcul analytique sur un disque via coordonnées polaires, et lien avec une intégrale curviligne via le théorème de **Green–Riemann**.

On étudie en particulier la fonction gaussienne 2D :

$$
f(x,y)=e^{-(x^2+y^2)}.
$$

---

## 1. Intégrale double sur un rectangle

On considère un domaine rectangulaire :

$$
D=[a,b]\times[c,d].
$$

### 1.1. Méthode de Riemann en 2D (rectangle)

#### 1.1.1. Discrétisation du domaine

On choisit :
- $n_x$ sous-intervalles sur $[a,b]$
- $n_y$ sous-intervalles sur $[c,d]$

Les pas sont :

$$
h_x=\frac{b-a}{n_x},\qquad h_y=\frac{d-c}{n_y}.
$$

Les points de la grille :

$$
x_i=a+i\,h_x\ (i=0,\dots,n_x),\qquad
y_j=c+j\,h_y\ (j=0,\dots,n_y).
$$

#### 1.1.2. Somme de Riemann (points bas-gauche)

Une approximation classique (Riemann « bas-gauche ») est :

$$
\iint_{D} f(x,y)\,dx\,dy \approx
h_x\,h_y \sum_{i=0}^{n_x-1}\sum_{j=0}^{n_y-1} f(x_i,y_j).
$$

---

### 1.2. Algorithme (pseudo-code)

Entrées : $f$, $a,b,c,d$, $n_x,n_y$.

1. Calculer $h_x=(b-a)/n_x$ et $h_y=(d-c)/n_y$
2. `sum = 0`
3. Pour `i = 0 .. nx-1` :
   - `x = a + i*hx`
   - Pour `j = 0 .. ny-1` :
     - `y = c + j*hy`
     - `sum += f(x,y)`
4. Retourner `hx*hy*sum`

---

### 1.3. Code Java (illustration)

#### 1.3.1. Interface 2D

```java
@FunctionalInterface
public interface BivariateFunction {
    double value(double x, double y);
}
```

#### 1.3.2. Intégrateur 2D (Riemann bas-gauche)

```java
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
```

#### 1.3.3. Programme principal (tests sur $[0,b]\times[0,b]$)

```java
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
```

---

### 1.4. Calcul demandé : $\iint_{[0,b]^2} e^{-(x^2+y^2)}dxdy$ pour $b=1,10,100$

#### 1.4.1. Valeur de référence (analytique)

Sur un carré $[0,b]\times[0,b]$, la fonction se **factorise** :

$$
\iint_{[0,b]^2} e^{-(x^2+y^2)}\,dx\,dy =
\left(\int_0^b e^{-x^2}\,dx\right)^2 =
\left(\frac{\sqrt{\pi}}{2}\,\mathrm{erf}(b)\right)^2.
$$

Donc, numériquement (≈) :
- $b=1$ : $\approx 0.5577462853510335$
- $b=10$ : $\approx 0.7853981633974484$ (quasi $\pi/4$)
- $b=100$ : $\approx 0.7853981633974484$

#### 1.4.2. Interprétation

Quand $b\to +\infty$, le carré $[0,b]^2$ tend vers le **premier quadrant** $(x\ge 0, y\ge 0)$, et :

$$
\iint_{x\ge 0,\,y\ge 0} e^{-(x^2+y^2)}\,dx\,dy =
\left(\int_0^{+\infty}e^{-x^2}\,dx\right)^2 =
\frac{\pi}{4}.
$$

---

### 1.5. Exemple de résultats numériques (Riemann 2D bas-gauche)

On a exécuté le programme Java suivant :

```bash
java Main2D.java
```

avec un maillage uniforme **nx = ny = 2000** (donc $h_x=h_y=b/2000$). Résultats obtenus :

```text
=== ∬ exp(-(x^2+y^2)) sur [0,1.0]x[0,1.0] ===
nx = ny = 2000
Riemann 2D (bas-gauche) : 0.5579823288686735

=== ∬ exp(-(x^2+y^2)) sur [0,10.0]x[0,10.0] ===
nx = ny = 2000
Riemann 2D (bas-gauche) : 0.7898355480238061

=== ∬ exp(-(x^2+y^2)) sur [0,100.0]x[0,100.0] ===
nx = ny = 2000
Riemann 2D (bas-gauche) : 0.8303345096700718
```

Pour comparer, on rappelle la valeur analytique :

$$
\iint_{[0,b]^2} e^{-(x^2+y^2)}\,dx\,dy=\left(\frac{\sqrt{\pi}}{2}\,\mathrm{erf}(b)\right)^2
\approx
\begin{cases}
0.5577462853510335 & (b=1) \\
0.7853981633974484 & (b=10) \\
0.7853981633974484 & (b=100)
\end{cases}
$$

Tableau des erreurs (approximation – référence) :

| $b$ | Approx. Riemann 2D | Référence | Erreur absolue | Erreur relative |
|---:|---:|---:|---:|---:|
| 1   | 0.5579823288686735 | 0.5577462853510335 | $+2.3604\times10^{-4}$ | $\approx 4.23\times10^{-4}$ |
| 10  | 0.7898355480238061 | 0.7853981633974484 | $+4.4374\times10^{-3}$ | $\approx 5.65\times10^{-3}$ |
| 100 | 0.8303345096700718 | 0.7853981633974484 | $+4.4936\times10^{-2}$ | $\approx 5.72\times10^{-2}$ |

**Commentaires :**
- La somme de Riemann **bas-gauche** tend ici à **surestimer** l’intégrale : la fonction $e^{-(x^2+y^2)}$ est décroissante quand $x$ et $y$ augmentent, donc prendre le coin bas-gauche (valeur plus grande) donne un biais positif.
- À **$n$ fixé**, quand $b$ augmente, le pas $h=b/2000$ augmente ($h=0.0005$ pour $b=1$, $h=0.005$ pour $b=10$, $h=0.05$ pour $b=100$), ce qui dégrade la précision : pour garder une précision comparable, il faut augmenter $n$ avec $b$ (ou utiliser une méthode d’ordre supérieur / une stratégie de maillage plus adaptée).

---

## 2. Intégrale sur un disque (coordonnées polaires)

On considère le disque centré en $(0,0)$ de rayon $R$ :

$$
D_R=\{(x,y)\in\mathbb{R}^2\mid x^2+y^2\le R^2\}.
$$

### 2.1. Calcul de $\iint_{D_R} e^{-(x^2+y^2)}dxdy$ par coordonnées polaires

Changement de variables :

$$
x=r\cos\theta,\qquad y=r\sin\theta,\qquad dx\,dy=r\,dr\,d\theta.
$$

Sur le disque : $0\le r\le R$ et $0\le \theta\le 2\pi$.

Alors :

$$
\iint_{D_R} e^{-(x^2+y^2)}\,dx\,dy =
\int_0^{2\pi}\int_0^R e^{-r^2}\,r\,dr\,d\theta.
$$

Or :

$$
\int_0^R e^{-r^2}r\,dr=\frac12\left(1-e^{-R^2}\right).
$$

Donc :

$$
\boxed{
\iint_{D_R} e^{-(x^2+y^2)}\,dx\,dy =
\pi\left(1-e^{-R^2}\right).
}
$$

---

### 2.2. En déduire $\int_0^{+\infty} e^{-x^2}\,dx$

Posons :

$$
I=\int_{-\infty}^{+\infty} e^{-x^2}\,dx.
$$

Alors :

$$
I^2 =
\left(\int_{-\infty}^{+\infty} e^{-x^2}\,dx\right)
\left(\int_{-\infty}^{+\infty} e^{-y^2}\,dy\right) =
\iint_{\mathbb{R}^2} e^{-(x^2+y^2)}\,dx\,dy.
$$

En faisant tendre $R\to +\infty$ dans le résultat du disque :

$$
\iint_{\mathbb{R}^2} e^{-(x^2+y^2)}\,dx\,dy =
\lim_{R\to\infty} \pi(1-e^{-R^2}) =
\pi.
$$

Donc $I^2=\pi$ et $I=\sqrt{\pi}$ (positif). Par symétrie :

$$
\boxed{
\int_0^{+\infty} e^{-x^2}\,dx=\frac{\sqrt{\pi}}{2}.
}
$$

---

### 2.3. Comparaison avec les résultats précédents

- Partie 1 : on trouve déjà numériquement, pour $b=10$ ou $100$ : $\int_0^b e^{-x^2}\,dx \approx \frac{\sqrt{\pi}}{2}\approx 0.8862269254$.

- Sur le carré $[0,b]^2$ :

$$
\iint_{[0,b]^2} e^{-(x^2+y^2)}dxdy =
\left(\int_0^b e^{-x^2}dx\right)^2
\to \left(\frac{\sqrt{\pi}}{2}\right)^2=\frac{\pi}{4}.
$$

- Sur le disque $D_R$ :
  
$$
\iint_{D_R} e^{-(x^2+y^2)}dxdy
=\pi(1-e^{-R^2}) \to \pi
$$

  car le disque couvre **tout le plan** quand $R\to+\infty$ (et pas seulement un quadrant).

---

## 3. Forme de Green–Riemann

### 3.1. Circulation d’un champ le long d’une courbe

Soit une courbe paramétrée $C : (x(t),y(t))$, $t_1\le t\le t_2$, et un champ :

$$
\vec V=(P(x,y),Q(x,y)).
$$

La circulation est :

$$
\boxed{
\int_C \vec V\cdot d\vec r =
\int_C P\,dx + Q\,dy =
\int_{t_1}^{t_2}\Big(P(x(t),y(t))\,x'(t)+Q(x(t),y(t))\,y'(t)\Big)\,dt.
}
$$

---

### 3.2. Cas particulier : $x(t)=t$, $y(t)=t$, pour $0<t<+\infty$

On suppose :

$$
P(x,y)=-\int_0^y e^{-(x^2+s^2)}\,ds,
\qquad
Q(x,y)=\int_0^x e^{-(t^2+y^2)}\,dt.
$$

Sur la diagonale $x=y=t$ :

$$
P(t,t)=-\int_0^t e^{-(t^2+s^2)}ds
=-e^{-t^2}\int_0^t e^{-s^2}ds,
$$

$$
Q(t,t)=\int_0^t e^{-(u^2+t^2)}du
=e^{-t^2}\int_0^t e^{-u^2}du.
$$

Donc :

$$
P(t,t)=-Q(t,t).
$$

Comme $x'(t)=1$ et $y'(t)=1$ :

$$
P(t,t)\,x'(t)+Q(t,t)\,y'(t)=P(t,t)+Q(t,t)=0.
$$

Ainsi, la circulation sur cette courbe vaut :

$$
\boxed{
\int_C P\,dx+Q\,dy=0.
}
$$

---

### 3.3. Lien avec l’intégrale surfacique via Green–Riemann

Théorème de Green–Riemann (orientation positive) :

$$
\boxed{
\oint_{\partial D} P\,dx + Q\,dy =
\iint_D\left(\frac{\partial Q}{\partial x}-\frac{\partial P}{\partial y}\right)\,dx\,dy.
}
$$

Ici :
- Comme $Q(x,y)=\int_0^x e^{-(t^2+y^2)}dt$, on a
  $\frac{\partial Q}{\partial x}=e^{-(x^2+y^2)}.$
- Comme $P(x,y)=-\int_0^y e^{-(x^2+s^2)}ds$, on a
  $\frac{\partial P}{\partial y}=-e^{-(x^2+y^2)}.$

Donc :

$$
\frac{\partial Q}{\partial x}-\frac{\partial P}{\partial y} =
e^{-(x^2+y^2)}-(-e^{-(x^2+y^2)}) =
2e^{-(x^2+y^2)}.
$$

Ainsi :

$$
\boxed{
\iint_D e^{-(x^2+y^2)}\,dx\,dy =
\frac12\oint_{\partial D}\big(P\,dx+Q\,dy\big).
}
$$

**Conclusion :** l’intégrale double de $e^{-(x^2+y^2)}$ sur un domaine $D$ peut être ramenée à une intégrale curviligne sur son bord $\partial D$, ce qui fournit une autre manière (théorique) de retrouver/comparer les résultats obtenus sur le carré ou sur le disque.

---
