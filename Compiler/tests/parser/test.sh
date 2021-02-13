#!/bin/bash

runparser() {
  bash $(git rev-parse --show-toplevel)/Compiler/run.sh -t parse $1;
}

exitcode=0;
fail=0;
count=0;

for file in `dirname $0`/illegal/*; do
  if runparser > /dev/null 2>&1 $file; then
    echo "Illegal file $file parsed successfully.";
    exitcode=1;
    fail=$((fail+1));
  fi
  count=$((count+1));
done

for file in `dirname $0`/legal/*; do
  if ! runparser $file; then
    echo "Legal file $file failed to parse.";
    exitcode=1;
    fail=$((fail+1));
  fi
  count=$((count+1));
done

echo "Failed $fail tests out of $count";
exit $exitcode;
