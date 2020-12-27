#include <stdio.h>
#include <string.h>
void 
vulnerable(char *src){
  char dest[10]; //declare a stack based buffer
  strcpy(dest,src); //always good not to do bounds checking
  #printf("[%s]\n",dest); //please note this has been removed because it affects the exploit-ability of the stack overflow
  return;  }


int 
main(int argc, char **argv){
  vulnerable(argv[1]); //call vulnerable function
  printf("you lose...\n");
  return (0);  }
