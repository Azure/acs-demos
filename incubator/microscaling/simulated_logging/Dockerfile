FROM rgardler/acs-logging-test-base

RUN pip install requests

COPY src src

CMD [ "python", "src/simulatedLogs.py" ]