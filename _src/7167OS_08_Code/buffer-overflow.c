#include <stdio.h>
#include <string.h>
void vulnerable(char *src){
  char dest[10]; //declare a stack based buffer
  strcpy(dest,src); 
  printf("[%s]\n",dest); //print the result
  return;  }

void call_me_maybe(){
  printf("so much win!!\n");
  return;  }

int main(int argc, char **argv){
  vulnerable(argv[1]); //call vulnerable function
  return (0);  }
