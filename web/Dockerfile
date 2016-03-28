FROM rgardler/acs-logging-test-base

COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt

EXPOSE 80

COPY src src

ENTRYPOINT [ "python", "src/server.py"]