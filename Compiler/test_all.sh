RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

function green {
    echo -n -e ${GREEN}"[+] "
    echo -n $@
    echo -e ${NC}
}

function red {
    echo -n -e ${RED}"[x] "
    echo -n $@
    echo -e ${NC}
}

green "building"
bash build.sh
grades=$(find . -regextype sed -regex ".*/test.sh$" | grep "$1")
tot=$(find . -regextype sed -regex ".*/test.sh$" | grep "$1" | wc -l)
idx=1
failed=0

for grade in $grades; do
    green "grading $grade ($idx/$tot)"
    bash $grade
    if [ $? -eq 0 ];then
        echo "..PASS"
    else
        red "..FAIL"
        ((failed++))
    fi
    ((idx++))
done

if [ $failed -eq 0 ];then
    green "passed $tot tests out of $tot"
else
    red "failed $failed tests out of $tot"
fi