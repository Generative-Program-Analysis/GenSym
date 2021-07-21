## Build the Docker Image

Run `docker build -t <tag> .` under this directory.

## Get the Prebuild Docker Image

Get the prebuild docker iamge:

`docker pull guannanwei/llsc-dev:0.1`

Instantiate the container:

`docker run --name <container_name> --ulimit='stack=-1:-1' -it guannanwei/llsc-dev:0.1 bash`

Add option `--rm` to start the container to automatically cleanup after each run.

After entering the interactive `bash`, pull the latest code of the `sai` repository:

```
cd /llsc/sai
git pull origin master
```

To check if all Scala code can be successfully compiled, first run `sbt` interactively:

```
cd /llsc/sai/dev-clean
./start_sbt
```

Then run `compile` in `sbt`.
