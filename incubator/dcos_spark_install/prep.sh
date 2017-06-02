# sudo python3 ~/projects/simdem/run.py run --path ./ preparation

pushd ~/projects/simdem/
./build.sh
popd

docker rm simdem
docker run -it --name simdem -v ~/.ssh:/root/.ssh -v ~/.azure:/root/.azure  -v $(pwd):/demo_scripts rgardler/simdem run preparation
