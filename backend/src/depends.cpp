#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define MAXLINE 1024

void
cleanArrow(const char *filename)
{
    FILE *fd;
    FILE *fd2;
    fd = fopen(filename, "rt");
    if ( !fd ) {
        return;
    }

    fd2 = fopen("./tmp/b", "wt");
    if ( !fd2 ) {
        fclose(fd);
        return;
    }

    char line[MAXLINE];
    char *l;

    while ( !feof(fd) ) {
        l = fgets(line, MAXLINE, fd);
        if ( !l ) {
            break;
	}
        int i;
        for ( i = 0; l[0] != '\0'; i++, l++ ) {
	    if ( l[0] == ' ' && l[1] == '-' && l[2] == '>' &&
                 l[3] == ' ') {
                l = l + 4;
                break;
	    }
	}
        fprintf(fd2, "%s", l);
    }

    fclose(fd);
    fclose(fd2);
}

int
main(int argc, char *argv[])
{
    if ( argc != 2 ) {
        fprintf(stderr, "Usage:\n\t%s search_pattern\n", argv[0]);
        return 0;
    }

    char command[1024];

    sprintf(command, "grep %s e > tmp/a", argv[1]);
    system(command);

    cleanArrow("./tmp/a");

    system("sort tmp/b > tmp/c");
    sprintf(command, "lineas_repetidas tmp/c | grep -v %s > tmp/d", argv[1]);
    system(command);
    system("more tmp/d");

    return 0;
}
