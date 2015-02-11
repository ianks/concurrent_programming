# Homework 2

_Author: Ian Ker-Seymer_

## Instructions for running

1. `cd` in to directory
  - `cd PetersonTree`
  - `cd Bakery`

1. Compile the code
  - `make`

1. Run the test
  - `java TestPetersonTree`
  - `java TestBakery`

## Filter lock vs Peterson tree

| Threads       | Peterson Tree | Filter Lock                        |
| ------------- |:-------------:|:----------------------------------:|
| 8             | 55 ms         | 155 ms                             |
| 64            | 9780 ms       | Unknown (Too long for my computer) |
