# Bakery Algorithm

_Author: Ian Ker-Seymer_

## What is it

The [bakery
algorithm](http://en.wikipedia.org/wiki/Lamport%27s_bakery_algorithm) is a
method for achieving mutual exclusion among multiple threads using mutual
exclusion.

## But.. Bakery... WTF

Have you ever been to a bakery? Well before these places had those fancy buzzers
to tell you that your bread was ready, they used to give you a number which
signified which number in the queue you were.

The Bakery Algorithm does the same thing. It gives each thread a unique number
which guarantees its place in line. The idea behind this is to ensure fairness.
All threads will eventually be served their bread, preventing starvation. This
is a component of concurrent programming that is not guaranteed by more naive
algorithms.

## How to run

1. `make`
1. `java TestBakery`
