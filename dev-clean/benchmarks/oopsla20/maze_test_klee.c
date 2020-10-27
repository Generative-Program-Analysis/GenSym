// http://feliam.wordpress.com/2010/10/07/the-symbolic-maze/ ‎
// twitter.com/feliam
/*
 * It's a maze!
 * Use a,s,d,w to move "through" it.
 */

#include<string.h>
#include<stdio.h>
#include<stdlib.h>
#include <klee/klee.h>


/**
 * Maze hardcoded dimensions
 */
#define H 7
#define W 11
/**
 * Tha maze map
 */
char maze[H][W] = {
  "+-+---+---+",
  "| |     |#|",
  "| | --+ | |",
  "| |   | | |",
  "| +-- | | |",
  "|     |   |",
  "+-----+---+" };

/**
 * Draw the maze state in the screen!
 */
void draw ()
{
  int i, j;
  for (i = 0; i < H; i++)
  {
    for (j = 0; j < W; j++);
    //   printf ("%c", maze[i][j]);
    // printf ("\n");
  }
  // printf ("\n");
}

/**
 * The main function
 */
int main (int argc, char *argv[])
{
  int x, y;     //Player position
  int ox, oy;   //Old player position
  int i = 0;    //Iteration number
  #define ITERS 28
  char program[ITERS];

  //Initial position
  x = 1;
  y = 1;
  maze[y][x]='X';

  //Print some info
  // printf ("Maze dimensions: %dx%d\n", W, H);
  // printf ("Player pos: %dx%d\n", x, y);
  // printf ("Iteration no. %d\n",i);
  // printf ("Program the player moves with a sequence of 'w', 's', 'a' and 'd'\n");
  // printf ("Try to reach the price(#)!\n");

  //Draw the maze
  draw ();    
  //Read the directions 'program' to execute...
  klee_make_symbolic(program, ITERS, "program");

  //Iterate and run 'program'
  while(i < ITERS)
  {
    //Save old player position
    ox = x;
    oy = y;
    //Move polayer position depending on the actual command
    switch (program[i])
    {
      case 'w':
        y--;
        break;
      case 's':
        y++;
        break;
      case 'a':
        x--;
        break;
      case 'd':
        x++;
        break;
      default:
        // printf("Wrong command!(only w,s,a,d accepted!)\n");
        // printf("You loose!\n");
        exit(-1);
    }

    //If hit the price, You Win!!   
    if (maze[y][x] == '#')
    {
      // printf ("You win!\n");
      // printf ("Your solution <%42s>\n",program);
      // klee_assert(0);
      exit (1);
    }
    //If something is wrong do not advance
    if (maze[y][x] != ' '
        &&
        !((y == 2 && maze[y][x] == '|' && x > 0 && x < W)))
    {
      x = ox;
      y = oy;
    }

    //Print new maze state and info...
    // printf ("Player pos: %dx%d\n", x, y);
    // printf ("Iteration no. %d. Action: %c. %s\n",i,program[i], ((ox==x && oy==y)?"Blocked!":""));

    //If crashed to a wall! Exit, you loose
    if (ox==x && oy==y){
      // printf("You loose\n");
      exit(-2);
    }
    //put the player on the maze...
    maze[y][x]='X';
    //draw it
    draw ();
    //increment iteration
    i++;
    //me wait to human
    // sleep(1);
  }
  //You couldn't make it! You loose!	  
  // printf("You loose\n");
}
