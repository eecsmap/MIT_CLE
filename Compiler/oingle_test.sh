bash build.sh && cd tests/optimizer && bash ../../run.sh -t assembly -d ../../$1 -o ../../tmp.s && gcc -no-pie -o tmp -L lib ../../tmp.s -l6035 -lpthread && ./tmp
rm tmp