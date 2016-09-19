import config
from messageQueue import Queue
from summaryTable import SummaryTable

from flask import Flask, render_template
from flask_restful import Resource, Api
import time

app = Flask(__name__)
api = Api(app)

class QueueAPI(Resource):

    @app.route("/")
    def hello():
        return render_template('index.html')

    def get(self, queue_id):
        """Get details of a queue. 
        """
        
        if not queue_id:
            queue_id = config.AZURE_STORAGE_QUEUE_NAME

        queue = self.getMessageQueue(queue_id)
        length = self.queue.getLength()
        duration = self.getTableService().getLastProcessingTime()
        
        resp = {
            'queue_name': queue_id,
            'queue_length': length,
            'last_duration': duration,
            'time': time.strftime("%H:%M")
            }
        return resp

    def getTableService(self):
        return SummaryTable(config.AZURE_STORAGE_ACCOUNT_NAME, config.AZURE_STORAGE_ACCOUNT_KEY, config.AZURE_STORAGE_SUMMARY_TABLE_NAME)
    
    def getMessageQueue(self, queue_id):
        self.queue = Queue(account_name = config.AZURE_STORAGE_ACCOUNT_NAME, account_key=config.AZURE_STORAGE_ACCOUNT_KEY, queue_name=queue_id)
    
api.add_resource(QueueAPI, '/queue',
                           '/queue/<string:queue_id>')

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0')
