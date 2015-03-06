# Homework 3

_Author: Ian Ker-Seymer_

## Instructions for running

1. `cd` in to directory

2. Compile the code
  - `make`

3. Run the tests

## Filter lock vs Peterson tree

| Threads       | Peterson Tree | Filter Lock                        |
| ------------- |:-------------:|:----------------------------------:|
| 8             | 55 ms         | 155 ms                             |
| 64            | 9780 ms       | Unknown (Too long for my computer) |
