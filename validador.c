#include <stdio.h>
#include <string.h>
#include <ctype.h>

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("ERROR");
        return 1;
    }
    char *password = argv[1];
    int len = strlen(password);
    int tieneMayus = 0;
    int tieneMinus = 0;
    int tieneNumero = 0;
    if (len < 8) {
        printf("WEAK");
        return 0;
    }
    for (int i = 0; i < len; i++) {
        if (isupper(password[i])) tieneMayus = 1;
        if (islower(password[i])) tieneMinus = 1;
        if (isdigit(password[i])) tieneNumero = 1;
    }
    if (tieneMayus && tieneMinus && tieneNumero) {
        printf("SECURE");
    } else {
        printf("WEAK");
    }
    return 0;
}