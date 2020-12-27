#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#define MAX_COMMANDSIZE 100
int main(int argc,char *argv[],char **envp){
  char opt_buf[MAX_COMMANDSIZE];
  char *args[2];
  args[0] = opt_buf;
  args[1] = NULL;
  int opt_int;
  const char *command_filename = "/data/race-condition/commands.txt";
  FILE *command_file;
  printf("option: ");
  opt_int = atoi(gets(opt_buf));
  printf("[*] option %d selected...\n",opt_int);
  if (access(command_filename,R_OK|F_OK) == 0){
    printf("[*] access okay...\n");
    command_file = fopen(command_filename,"r");
    for (;opt_int>0;opt_int--){
      fscanf(command_file,"%s",opt_buf);
    }
    printf("[*] executing [%s]...\n",opt_buf);
    fclose(command_file);
  }
  else{
    printf("[x] access not granted...\n");
  }
  int ret = execve(args[0],&args,(char **)NULL);
  if (ret != NULL){
    perror("[x] execve");
  }
  return 0;
}
