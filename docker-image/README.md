## Build the Docker Image

Run `docker build -t <tag> .` under this directory.

## Get the Prebuild Docker Image

To get the prebuild docker iamge:

`docker pull guannanwei/gensym:dev`

To instantiate the container:

`docker run --name <container_name> --ulimit='stack=-1:-1' -it guannanwei/gensym:dev bash`

Add option `--rm` to start the container to automatically cleanup after each run.

See `/icse23/icse23-artifact-evaluation/README.md` for further information.