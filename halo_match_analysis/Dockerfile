FROM rgardler/acs-logging-test-base

COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt

COPY src src

CMD [ "python", "src/app.py" ]