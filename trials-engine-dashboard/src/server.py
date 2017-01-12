"""
A simple web dashboard for examining the state of a game engine.

"""
from flask import Flask, jsonify, redirect, request, render_template, url_for, send_from_directory, json
from flask.ext.socketio import SocketIO, emit
import json
import urllib

# Set this variable to "threading", "eventlet" or "gevent" to test the
# different async modes, or leave it set to None for the application to choose
# the best option based on installed packages.
async_mode = None

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app, async_mode=async_mode)
thread = None
engine_url = "http://engine:8080/api/v0.1/tournament/status"

def background_thread():
    old_data = None;
    while True:
        socketio.sleep(1)
        resp_body = urllib.request.urlopen(engine_url).read()
        status = resp_body.decode('UTF-8')

        socketio.emit('game_status',
                      status,
                      namespace='/engine')

@app.route("/")
def index(name=None):
    status = { 'state': 'Initializing Dashboard...'}
    return render_template('index.html',
                           status = status,
                           async_mode=socketio.async_mode
    )

@socketio.on('connect', namespace='/engine')
def test_connect():
    global thread
    if thread is None:
        thread = socketio.start_background_task(target=background_thread)
    emit('log', 'Connected to engine')

@socketio.on('engine_ping', namespace='/engine')
def ping_pong():
    emit('engine_pong')

@app.route('/js/<path:filename>')
def send_js(filename):
    return send_from_directory('js', filename)

if __name__ == "__main__":
    app.debug = True
    socketio.run(app, host='0.0.0.0', port=80)
