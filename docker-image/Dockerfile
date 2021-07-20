FROM ubuntu:21.04
LABEL maintainer="guannanwei@outlook.com"
LABEL version="0.1"
LABEL description="Development environment for SAI/LLSC"

# Disable Prompt During Packages Installation
ARG DEBIAN_FRONTEND=noninteractive

# Volume configuration
VOLUME ["/var/tmp"]

# Initial setup
RUN mkdir /llsc
COPY init_setup.sh /llsc/init_setup.sh
RUN bash /llsc/init_setup.sh

CMD cd /llsc

