#! /bin/sh

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# exemple de définition d'une fonction
test_lex_invalide () {
    # $1 = premier argument.
    if test_lex "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo -e "\033[32m$1 : PASS.\033[0m"
    else
        echo -e "\033[31m$1 : ERROR.\033[0m"
        test_lex "$1";
        exit 1
    fi
}


test_lex_valid() {

if test_lex "$1" 2>&1 | grep -q -e ':[0-9][0-9]*:'
then
    echo -e "\033[31m$1 : ERROR.\033[0m"
    test_lex "$1" 2>&1;
    exit 1
else
    if test_lex "$1" 2>&1 | grep -q -e 'Exception in thread'
    then
        echo -e "\033[31m$1 : ERROR.\033[0m"
        test_lex "$1" 2>&1;
        exit 1
    else
        echo -e "\033[32m$1 : PASS.\033[0m"
    fi
fi
}

for cas_de_test in src/test/deca/lex/invalid/*.deca
do
    test_lex_invalide "$cas_de_test"
done

for cas_de_test in src/test/deca/lex/valid/*.deca
do
    test_lex_valid "$cas_de_test"
done

echo "OK !"
