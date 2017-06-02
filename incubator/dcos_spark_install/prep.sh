docker rm simdem
docker run -it --name simdem -v ~/.ssh:/root/.ssh -v ~/.azure:/root/.azure  -v $(pwd):/demo_scripts rgardler/simdem run preparation
