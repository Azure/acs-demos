FROM rgardler/simdem_novnc:0.7.4-dev

ARG SCRIPTS_DIR

### VNC config
ENV NO_VNC_PORT 8080
ENV VNC_COL_DEPTH 24
ENV VNC_RESOLUTION 1024x768
ENV VNC_PW vncpassword

USER 0

RUN rm -Rf demo_scripts
COPY $SCRIPTS_DIR demo_scripts

USER 1984
