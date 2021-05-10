# MIT_CLE

## Progress
✔Passed all tests: p1, p2, p3\
❌Partially passed: p4\
❓WIP: p5
### Note
1. implemented local CSE for p4, supports `addq, subq, imulq` only, no significant efficiency change
2. the global CSE was done 80%, but no time already, will consider finishing it in the future

## Course Link
https://github.com/6035/fa19

## Environment
- WSL2: Ubuntu 20.04
- Java: openjdk 11.0.9

## Setting up - for Java
### I. install ant
this will automatically install openjdk 11 for you
```
$ sudo apt-get update && sudo apt install ant
```
### II. check if installed successfully
```
$ java -version
openjdk version "11.0.9.1" 2020-11-04
OpenJDK Runtime Environment (build 11.0.9.1+1-Ubuntu-0ubuntu1.20.04)
OpenJDK 64-Bit Server VM (build 11.0.9.1+1-Ubuntu-0ubuntu1.20.04, mixed mode, sharing)
```
```
$ ant -version
Apache Ant(TM) version 1.10.7 compiled on October 24 2019
```
### III. running parser
```
$ bash build.sh
$ bash run.sh
```
### IV. build and grade all
```
bash grade_all.sh # run all grades
bash grade_all.sh scanner # run grades for scanner only
bash test_all.sh # run all tests
bash test_all.sh scanner # run tests for scanner only
```

## Note
Didn't find test cases on 6.035 git repo, downloaded from https://github.com/mayasankar/6035-compiler and modified some by myself

## Learn
`Compiler` => lab source code\
`fa19` => course materials\
`Labs` => my implementations on lab questions\
`Notes` => summary about what I've learnt

## Join and Contribute
You are welcome to initate a pull request if you find a better idea/implementation to a specific lab question.