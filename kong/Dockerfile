FROM kong:3.9-ubuntu
USER root

RUN apt-get update && \
    apt-get install -y --no-install-recommends gettext && \
    rm -rf /var/lib/apt/lists/*

COPY kong.yml.template /etc/kong/kong.yml.template
USER kong
