FROM python

RUN pip install --upgrade pip

COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt

EXPOSE 80

COPY src src

ENTRYPOINT [ "python", "src/server.py"]