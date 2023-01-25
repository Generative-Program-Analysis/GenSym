FROM ubuntu:20.04
LABEL maintainer="guannanwei@purdue.edu"
LABEL version="0.1"
LABEL description="ICSE 23 GenSym Artifact"

# Disable Prompt During Packages Installation
ARG DEBIAN_FRONTEND=noninteractive

# Volume configuration
VOLUME ["/var/tmp"]

# Initial setup
RUN mkdir /icse23
COPY init_setup.sh /icse23/init_setup.sh
COPY sync.sh /icse23/sync.sh
RUN bash /icse23/init_setup.sh

ENV PATH="/icse23/scala-2.12.10/bin:${PATH}"

CMD cd /icse23

