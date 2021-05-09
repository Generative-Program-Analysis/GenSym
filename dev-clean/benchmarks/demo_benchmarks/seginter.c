#ifdef KLEE
#include "klee/klee.h"
#endif
//https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/ 
typedef struct Point {
    int x; int y;
} Point;

int max(int x, int y) {
  if (x > y) return x;
  return y;
}

int min(int x, int y) {
  if (x < y) return x;
  return y;
}
  
int onSegment(Point p, Point q, Point r) {
    if (q.x <= max(p.x, r.x) && q.x >= min(p.x, r.x) &&
        q.y <= max(p.y, r.y) && q.y >= min(p.y, r.y))
       return 1;
    return 0;
}

int orientation(Point p, Point q, Point r) {
    int val = (q.y - p.y) * (r.x - q.x) -
              (q.x - p.x) * (r.y - q.y);
    if (val == 0) return 0;  // colinear
    return (val > 0)? 1: 2; // clock or counterclock wise
}

int doIntersect(Point p1, Point q1, Point p2, Point q2) {
    int o1 = orientation(p1, q1, p2);
    int o2 = orientation(p1, q1, q2);
    int o3 = orientation(p2, q2, p1);
    int o4 = orientation(p2, q2, q1);
    // General case
    if (o1 != o2 && o3 != o4) return 1;
    // Special Cases
    // p1, q1 and p2 are colinear and p2 lies on segment p1q1
    if (o1 == 0 && onSegment(p1, p2, q1)) return 1;
    // p1, q1 and q2 are colinear and q2 lies on segment p1q1
    if (o2 == 0 && onSegment(p1, q2, q1)) return 1;
    // p2, q2 and p1 are colinear and p1 lies on segment p2q2
    if (o3 == 0 && onSegment(p2, p1, q2)) return 1;
     // p2, q2 and q1 are colinear and q1 lies on segment p2q2
    if (o4 == 0 && onSegment(p2, q1, q2)) return 1;
    return 0; // Doesn't fall in any of the above cases
}
  
// Driver program to test above functions
int main()
{
    struct Point p1 = {1, 1}, q1 = {10, 1};
    struct Point p2 = {1, 2}, q2 = {10, 2};
#ifdef KLEE
  klee_make_symbolic(&p2, sizeof(Point), "p2");
  //klee_make_symbolic(&q2, sizeof(Point), "q2");
#else
	make_symbolic(&p2.x, sizeof(int));
	make_symbolic(&p2.y, sizeof(int));
	//make_symbolic(&q2, sizeof(Point)); //FIXME
#endif
    doIntersect(p1, q1, p2, q2);
    return 0;
}
