# MIT_CLE

## Progress
✔Passed all tests: -\
❌Partially passed: -\
❓WIP: p1, p2, p3, p4, p5

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
$ javac -version
javac 10.0.2
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
bash grade_all.sh scanner # run all scanner grades
bash test_all.sh # run all tests
bash test_all.sh scanner # run all scanner tests
```

## Learn
`Compiler` => lab source code\
`fa19` => course materials\
`Labs` => my implementations on lab questions\
`Notes` => summary about what I've learnt

## Join and Contribute
You are welcome to initate a pull request if you find a better idea/implementation to a specific lab question.