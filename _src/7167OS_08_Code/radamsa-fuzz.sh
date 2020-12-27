#!/bin/bash
ROOT=$1
TARGET=dexopt
ITER=$2
for ((c=0;1;c++))
do
  cat $ROOT | radamsa -m bf,br,sr -p bu > fuzz.dex
  $TARGET -d fuzz.dex 2>&1 > /dev/null
  RET_CODE=$?
  echo "[$c] {$RET_CODE} ($WINS)" 
  test $RET_CODE -gt 127 && cp fuzz.dex win-
    dexdump_$ITER"_"$c.dex && WINS=`expr $WINS + 1`
done
