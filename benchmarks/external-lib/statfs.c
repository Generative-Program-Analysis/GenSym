#include <sys/vfs.h>
#include <errno.h>

int main(){
    struct statfs sfile;
    int status;
    status = statfs("/", &sfile);
    gs_assert_eager(status == 0); // statfs should succeed

    status = statfs("/non/existing/dir", &sfile);
    gs_assert_eager(status == -1); // statfs should fail
    gs_assert_eager(errno == ENOENT); // errno should be ENOENT

    return 0;
}
