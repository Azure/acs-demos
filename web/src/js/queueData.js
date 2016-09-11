function plotSummary(error, warning, debug, info) {
    var trace1 = {
        x: ['Error', 'Warning', 'Debug', 'Info'],
        y: [error, warning, debug, info],
        name: 'Worker/Consumer Demo',
        type: 'bar'
    };
    
    var data = [trace1];
    var layout = {barmode: 'group'};
    
    Plotly.newPlot('summaryChart', data, layout);
}

function plotQueue(queueId) {
    function updateLength() {
        queueEndpoint = "http://" + window.location.hostname + ":5555/queue/" + queueId;
        $.ajax({url: queueEndpoint, success: function(result){
            $("#queueLength").text(result.queuelength);
	    if (typeof dataX == 'undefined') {
		dataX = [0];
		dataY = [0];
	    } else {
		dataX.push(dataX.length + 1);
		dataY.push(result.queue_length);
	    }
	    trace = {
		x: dataX,
		y: dataY,
		type: 'scatter'
	    };
	    var data = [trace];
	    Plotly.newPlot('lengthChart', data);
	    setTimeout(function() {
		updateLength(queueId);
	    }, 1000);
        }});
    }

    updateLength();
}

