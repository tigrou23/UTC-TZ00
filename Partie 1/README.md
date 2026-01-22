# Partie 1 – Intégration numérique en 1D

Ce document explique **pas à pas** ce que fait la partie 1 du projet, et comment le code Java est organisé.  
Objectif : **approcher numériquement une intégrale** du type

$$
\int_a^b f(x)\,\mathrm{d}x
$$

en utilisant trois méthodes :

- Sommes de **Riemann (gauche)**  
- Méthode des **trapèzes**  
- Méthode de **Simpson**

Nous testons ces méthodes sur la fonction

$$
f(x) = e^{-x^2}
$$

pour différentes bornes supérieures $b \in \{1, 10, 100\}$.

---

## 1. Rappels théoriques

### 1.1. Intégrale définie

L’intégrale définie $$\int_a^b f(x)\,dx$$ peut être vue comme **l’aire sous la courbe** de $f$ entre $x = a$ et $x = b$.

En pratique, on ne sait pas toujours calculer cette intégrale **exactement**.  
On va donc l’**approcher** en découpant l’intervalle $[a, b]$ en petits segments.

---

### 1.2. Discrétisation de l’intervalle

On choisit un **nombre de sous-intervalles** $n$ (par exemple $n = 1000$).

- La longueur de chaque sous-intervalle est :

$$
h = \frac{b-a}{n}
$$

- Les points de discrétisation sont :

$$
x_i = a + i \cdot h \quad \text{pour } i = 0, 1, \dots, n
$$

---

### 1.3. Méthode de Riemann (somme à gauche)

Ressource utile : [Sommes de Riemann - Youtube](https://www.youtube.com/watch?v=R6ge0QBu3Nk)

Idée : on approxime l’aire sous la courbe par une somme de **rectangles**.

- Sur chaque sous-intervalle $[x_i, x_{i+1}]$, on prend la valeur de $f$ au **bord gauche** $x_i$.
- L’aire de ce rectangle est : $f(x_i) \cdot h$.

Formule :

$$
\int_a^b f(x)\,dx \approx h \sum_{i=0}^{n-1} f(x_i)
$$

Pseudo-code :

1. Calculer $h = (b - a)/n$
2. `sum = 0`
3. Pour `i` de 0 à `n - 1` :
   - `x = a + i * h`
   - `sum += f(x)`
4. Retourner `h * sum`

---

### 1.4. Méthode des trapèzes

Idée : au lieu d’un rectangle, on approxime la courbe par un **segment** entre $x_i$ et $x_{i+1}$.  
Cela forme un **trapèze** dont l’aire est :

$$
\text{Aire} = \frac{h}{2} \left(f(x_i) + f(x_{i+1})\right)
$$

Formule globale :

$$
\int_a^b f(x)\,dx \approx \frac{h}{2} \left[f(x_0) + 2 \sum_{i=1}^{n-1} f(x_i) + f(x_n)\right]
$$

Pseudo-code :

1. Calculer $h = (b-a)/n$
2. `sum = 0.5 * (f(a) + f(b))`
3. Pour `i` de 1 à `n - 1` :
   - `x = a + i * h`
   - `sum += f(x)`
4. Retourner `h * sum`

---

### 1.5. Méthode de Simpson (ordre 2)

Simpson combine l’idée des trapèzes avec une approximation **quadratique** (parabole) sur chaque paire de sous-intervalles.

**Attention :** on doit avoir **n pair**.

Formule :

$$
\int_a^b f(x)\,dx \approx \frac{h}{3} \left[(x_0) + 4 \sum_{i=1,3,5,\dots}^{n-1} f(x_i) + 2 \sum_{i=2,4,6,\dots}^{n-2} f(x_i) + f(x_n)
\right]
$$

Pseudo-code :

1. Vérifier que `n` est pair, sinon erreur.
2. Calculer $h = (b-a)/n$
3. `sum = f(a) + f(b)`
4. Pour `i` impair de 1 à `n - 1` :
   - `x = a + i * h`
   - `sum += 4 * f(x)`
5. Pour `i` pair de 2 à `n - 2` :
   - `x = a + i * h`
   - `sum += 2 * f(x)`
6. Retourner `(h / 3) * sum`

---

### 1.6. Comparaison des méthodes (idée générale)

- **Riemann (gauche)** : simple, mais précision limitée.
- **Trapèzes** : plus précis, erreur $\mathcal{O}(h^2)$.
- **Simpson** : encore plus précis, erreur $\mathcal{O}(h^4)$ (si $f$ est assez régulière).

Plus $n$ est grand (donc $h$ petit), plus le résultat est précis… mais plus le calcul est long.

---

## 2. Structure du code Java

### 2.1. Organisation des fichiers

- `UnivariateFunction.java`  
  → Interface fonctionnelle pour représenter une fonction

$$
f : \mathbb{R} \rightarrow \mathbb{R}
$$

- `Integrator1D.java`  
  → Contient les trois méthodes d’intégration :
  - `riemannLeft(...)`
  - `trapezoidal(...)`
  - `simpson(...)`

- `Main1D.java`  
  → Programme principal pour :
  - définir la fonction $f(x) = e^{-x^2}$,
  - choisir les bornes $[0, b]$,
  - appeler les différentes méthodes,
  - afficher les résultats.

---

### 2.2. `UnivariateFunction`

```java
@FunctionalInterface
public interface UnivariateFunction {
    double value(double x);
}
```

- C’est une **interface fonctionnelle** : une seule méthode abstraite.
- On peut passer une fonction comme **lambda**, par exemple :
  ```java
  UnivariateFunction f = x -> Math.exp(-x * x);
  ```
- Avantage : on peut réutiliser `Integrator1D` avec **n’importe quelle fonction** $f$.

---

### 2.3. `Integrator1D`

Cette classe fournit des **méthodes statiques**.  
On ne crée pas d’objets `Integrator1D`, on appelle directement :

```java
double integral = Integrator1D.trapezoidal(f, a, b, n);
```

#### 2.3.1. Méthode de Riemann (gauche)

```java
public static double riemannLeft(UnivariateFunction f, double a, double b, int n) {
    double h = (b - a) / n;
    double sum = 0.0;

    for (int i = 0; i < n; i++) {
        double x = a + i * h;  // point gauche
        sum += f.value(x);
    }

    return h * sum;
}
```

- `a`, `b` : bornes de l’intégrale
- `n` : nombre de sous-intervalles
- `h` : largeur de chaque sous-intervalle
- Boucle `i = 0` à `n - 1` → on prend le **bord gauche** `x = a + i * h`.

#### 2.3.2. Trapèzes

```java
public static double trapezoidal(UnivariateFunction f, double a, double b, int n) {
    double h = (b - a) / n;
    double sum = 0.5 * (f.value(a) + f.value(b));

    for (int i = 1; i < n; i++) {
        double x = a + i * h;
        sum += f.value(x);
    }

    return h * sum;
}
```

- On commence par la moyenne des extrémités : `0.5 * (f(a) + f(b))`.
- Puis on ajoute toutes les valeurs intermédiaires : `f(x_i)` pour `i = 1` à `n - 1`.
- Enfin, on multiplie par `h`.

#### 2.3.3. Simpson

```java
public static double simpson(UnivariateFunction f, double a, double b, int n) {
    if (n % 2 != 0) {
        throw new IllegalArgumentException("n doit être pair pour Simpson.");
    }

    double h = (b - a) / n;
    double sum = f.value(a) + f.value(b);

    // coefficients 4 pour les indices impairs
    for (int i = 1; i < n; i += 2) {
        double x = a + i * h;
        sum += 4.0 * f.value(x);
    }

    // coefficients 2 pour les indices pairs (sauf a et b)
    for (int i = 2; i < n; i += 2) {
        double x = a + i * h;
        sum += 2.0 * f.value(x);
    }

    return (h / 3.0) * sum;
}
```

- `n` doit être **pair**.
- On sépare la somme en :
  - indices **impairs** → coefficient 4,
  - indices **pairs** (sauf 0 et n) → coefficient 2.

---

### 2.4. `Main1D`

But : **tester** les méthodes sur $f(x) = e^{-x^2}$ avec plusieurs valeurs de $b$.

```java
public class Main1D {

    public static void main(String[] args) {
        UnivariateFunction f = x -> Math.exp(-x * x);

        double[] bs = {1.0, 10.0, 100.0};
        int n = 1_000_000; // nombre de sous-intervalles (à adapter)

        for (double b : bs) {
            System.out.println("=== Intégrale de exp(-x^2) sur [0, " + b + "] ===");
            runAllMethods(f, 0.0, b, n);
            System.out.println();
        }
    }

    private static void runAllMethods(UnivariateFunction f, double a, double b, int n) {
        double riemann = Integrator1D.riemannLeft(f, a, b, n);
        double trapeze = Integrator1D.trapezoidal(f, a, b, n);
        int nSimpson = (n % 2 == 0) ? n : n - 1;
        double simpson = Integrator1D.simpson(f, a, b, nSimpson);

        System.out.println("Riemann gauche   : " + riemann);
        System.out.println("Trapèzes         : " + trapeze);
        System.out.println("Simpson (n pair) : " + simpson);
    }
}
```

---

## 3. Exécution du code

Pour compiler le code, depuis la racine du projet :
```bash
javac src/*.java
```
Puis lancer le programme principal :
```bash
java src/Main1D
```

---

## 4. Valeurs de référence pour $\int_0^b e^{-x^2} dx$

Mathématiquement, on sait que :

$$
\int_0^b e^{-x^2} dx = \frac{\sqrt{\pi}}{2} \, \text{erf}(b)
$$

où `erf` est la **fonction d’erreur**.

Quelques valeurs (approx.) :

- $\int_0^1 e^{-x^2} dx \approx 0{,}746824$
- $\int_0^{10} e^{-x^2} dx \approx 0{,}886227$
- $\int_0^{100} e^{-x^2} dx \approx 0{,}886227$

Remarque :  
$\int_0^{+\infty} e^{-x^2} dx = \frac{\sqrt{\pi}}{2} \approx 0{,}886227$
Donc pour $b = 10$ ou $b = 100$, on est **très proche** de l’intégrale sur $[0, +\infty[$.

---

## 5. Comment interpréter les résultats

- Pour **b = 1** :
  - L’intégrale vaut ~0.7468.
  - Toutes les méthodes devraient s’en approcher si `n` est grand.
  - Simpson sera en général le plus précis pour un même `n`.

- Pour **b = 10** et **b = 100** :
  - Le résultat réel est proche de 0.886227.
  - La difficulté principale vient du fait que, sur un grand intervalle, il faut en général **plus de sous-intervalles** pour garder une bonne précision.

---

## 6. Résumé

- On approxime une intégrale par une **somme finie**.
- Trois méthodes sont implémentées :
  - Riemann (gauche) : simple mais moins précis.
  - Trapèzes : bon compromis.
  - Simpson : très précis si `n` est pair.
- Le code est structuré pour être **réutilisable** :
  - `UnivariateFunction` : abstraction de la fonction $f$.
  - `Integrator1D` : implémente les schémas numériques.
  - `Main1D` : exemple concret avec $f(x) = e^{-x^2}$.

---

## 7. Exemple de résultats numériques (n = 100 000)

Avec le code actuel, on a lancé :

```bash
java Main1D
```

et obtenu :

```text
Intégrale de exp(-x^2) sur [0, 1.0]
n = 100000 sous-intervalles
----------------------------------------------
Riemann (gauche) : 0.746827293409081
Trapèzes         : 0.746824132806287
Simpson          : 0.7468241328124311

Intégrale de exp(-x^2) sur [0, 10.0]
n = 100000 sous-intervalles
----------------------------------------------
Riemann (gauche) : 0.8862769254527015
Trapèzes         : 0.8862269254527015
Simpson          : 0.8862269254527176

Intégrale de exp(-x^2) sur [0, 100.0]
n = 100000 sous-intervalles
----------------------------------------------
Riemann (gauche) : 0.886726925452754
Trapèzes         : 0.886226925452754
Simpson          : 0.8862269254527572
```

### 7.1. Valeurs « vraies » (référence)

On rappelle que :

$$
\int_0^b e^{-x^2} dx = \frac{\sqrt{\pi}}{2} \, \text{erf}(b)
$$

Ce qui donne numériquement :

| Intervalle                              | Valeur de référence (≈) |
|----------------------------------------|--------------------------|
| $\displaystyle \int_0^1 e^{-x^2} dx$   | 0,746824132812427        |
| $\displaystyle \int_0^{10} e^{-x^2} dx$ | 0,886226925452758        |
| $\displaystyle \int_0^{100} e^{-x^2} dx$ | 0,886226925452758        |

Remarque : pour $b = 10$ et $b = 100$, on est quasiment à la valeur de l’intégrale sur $[0, +\infty[$.

---

### 7.2. Erreurs observées

On peut comparer chaque méthode à la valeur de référence et calculer l’**erreur absolue**  
(erreur = approximation – valeur de référence).

#### a) Intervalle $[0, 1]$

| Méthode         | Approximation              | Erreur absolue (≈)       |
|-----------------|----------------------------|---------------------------|
| Riemann gauche  | 0,746827293409081          | $+3,16 \times 10^{-6}$  |
| Trapèzes        | 0,746824132806287          | $-6,14 \times 10^{-12}$ |
| Simpson         | 0,7468241328124311         | $+4,11 \times 10^{-15}$ |

#### b) Intervalle $[0, 10]$

| Méthode         | Approximation              | Erreur absolue (≈)       |
|-----------------|----------------------------|---------------------------|
| Riemann gauche  | 0,8862769254527015         | $+5,0 \times 10^{-5}$    |
| Trapèzes        | 0,8862269254527015         | $-5,64 \times 10^{-14}$ |
| Simpson         | 0,8862269254527176         | $-4,03 \times 10^{-14}$ |

#### c) Intervalle $[0, 100]$

| Méthode         | Approximation              | Erreur absolue (≈)       |
|-----------------|----------------------------|---------------------------|
| Riemann gauche  | 0,886726925452754          | $+5,0 \times 10^{-4}$   |
| Trapèzes        | 0,886226925452754          | $-3,89 \times 10^{-15}$ |
| Simpson         | 0,8862269254527572         | $-7,77 \times 10^{-16}$ |

---

### 7.3. Ce qu’on en conclut

- **Riemann (gauche)** :
  - L’erreur augmente quand l’intervalle s’agrandit.
  - Sur $[0, 1]$, l’erreur est petite ($\approx 10^{-6}$), mais sur $[0, 100]$ elle passe à $\approx 5 \times 10^{-4}$.
  - La convergence est seulement d’ordre $\mathcal{O}(h)$ : il faut énormément de sous-intervalles pour être très précis.

- **Trapèzes** :
  - Déjà très bon : erreurs de l’ordre de $10^{-12}$ à $10^{-14}$ avec $n = 100\,000$.
  - Convergence d’ordre $\mathcal{O}(h^2)$ → la précision augmente beaucoup plus vite quand on raffine le pas.

- **Simpson** :
  - Encore mieux : erreurs de l’ordre de $10^{-15}$, donc **proches de la limite de la précision en double** (limitations du type `double` en Java).
  - Convergence d’ordre $\mathcal{O}(h^4)$, donc extrêmement efficace si la fonction est régulière (ce qui est le cas de $e^{-x^2}$).

En résumé :

- Pour une **intuition simple**, Riemann gauche suffit.
- Pour des **résultats corrects** sans trop de calcul, les trapèzes sont déjà très bons.
- Pour des **résultats très précis**, Simpson est le meilleur choix parmi les trois méthodes implémentées ici.
