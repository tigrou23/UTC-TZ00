# Intégration numérique – Projet Java

Ce dépôt contient une première partie d'un projet d'intégration numérique en 1D. Le code illustre plusieurs schémas classiques pour approximer
$\int_a^b f(x)\,dx$, appliqués ici à $f(x) = e^{-x^2}$.

## Contenu
- `Partie 1/README.md` : explications détaillées des méthodes numériques et exemples de résultats.
- `Partie 1/src` : code Java (`UnivariateFunction`, `Integrator1D`, `Main1D`).
- `sujet.pdf` : énoncé fourni.

## Prérequis
- JDK 17+ (ou toute version récente supportant les lambdas).

## Compiler et exécuter
Depuis la racine du dépôt :
```bash
cd "Partie 1"
javac src/*.java
java -cp src Main1D
```
Le programme affiche les approximations de l'intégrale sur `[0, b]` pour `b = 1`, `10` et `100` en utilisant Riemann à gauche, Trapèzes et Simpson.

## Ajustements possibles
- Modifier `n` dans `Partie 1/src/Main1D.java` pour affiner la précision ou accélérer l'exécution.
- Remplacer la fonction cible en changeant la lambda `f` dans `Main1D`.
