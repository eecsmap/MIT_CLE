# MIT_CLE

## Progress
✔Passed all tests: -\
❌Partially passed: -\
❓WIP: p1, p2, p3, p4, p5

## Course Link
https://github.com/6035/fa19

## Setting up - for Java
### I. install java 10
```
wget https://download.java.net/java/GA/jdk10/10.0.2/19aef61b38124481863b1413dce1855f/13/openjdk-10.0.2_linux-x64_bin.tar.gz
tar -xvf openjdk-10.0.2_linux-x64_bin.tar.gz
sudo mkdir -p /usr/lib/jdk
sudo mv jdk-10.0.2 /usr/lib/jdk
sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jdk/jdk-10.0.2/bin/java" 1
sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jdk/jdk-10.0.2/bin/javac" 1
```
### II. verify you installed successfully
```
$ java -version
openjdk version "10.0.2" 2018-07-17
OpenJDK Runtime Environment 18.3 (build 10.0.2+13)
OpenJDK 64-Bit Server VM 18.3 (build 10.0.2+13, mixed mode)
```
```
$ javac -version
javac 10.0.2
```
### III. install ant


## Learn
`Compiler` => lab source code\
`fa19` => course materials\
`Labs` => my implementations on lab questions\
`Notes` => summary about what I've learnt

## Join and Contribute
You are welcome to initate a pull request if you find a better idea/implementation to a specific lab question.