"""
A simple web dashboard for examining the state of a game engine.

"""
from flask import Flask, jsonify, redirect, request, render_template, url_for, send_from_directory

app = Flask(__name__)
@app.route("/")
def index(name=None):
    status = { 'state': 'Fetching...'}
    return render_template('index.html',
                           status = status
    )
@app.route('/js/<path:filename>')
def send_js(filename):
    return send_from_directory('js', filename)

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=80)
