FROM rgardler/simdem_cli:0.7.4-dev

ARG SCRIPTS_DIR

USER 0

RUN rm -Rf demo_scripts
COPY $SCRIPTS_DIR demo_scripts

USER 1984
