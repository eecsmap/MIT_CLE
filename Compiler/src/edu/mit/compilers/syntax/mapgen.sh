# use to generate tokens map in AstUtils.java
f="$(git rev-parse --show-toplevel)/Compiler/autogen/edu/mit/compilers/grammar/DecafParserTokenTypes.txt"
c=$(cat $f | grep =)
if [ ! -f $f ]; then
    echo "token file not exists"
    exit 1
fi
for l in $c; do
    k=$(echo $l | rev | cut -d = -f 1 | rev)
    v=$(echo $l | cut -d = -f 1)
    echo "put($k, \"$v\");"
done

