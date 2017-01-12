docker build -t biglittlechallenge/trials-base -f Dockerfile-trials-base .

docker build -t biglittlechallenge/trials-engine -f Dockerfile-trials-engine .

docker build -t biglittlechallenge/trials-engine-dashboard -f Dockerfile-trials-engine-dashboard .

docker build -t biglittlechallenge/trials-ai -f Dockerfile-trials-ai .
