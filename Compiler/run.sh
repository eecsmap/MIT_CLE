#!/usr/bin/env bash

java -ea -jar "$(git rev-parse --show-toplevel)/Compiler/dist/Compiler.jar" "$@"
