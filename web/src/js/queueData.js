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
            $("#queue_length").text(result.queue_length);
	    duration = parseFloat(result.last_duration);
	    duration = Math.round(duration) / 1000;
	    $("#processing_time").text(duration);
	    if (typeof length_dataX == 'undefined') {
		length_dataX = [0];
		length_dataY = [0];
		time_dataX = [0];
		time_dataY = [0];
	    } else {
		x = length_dataX[length_dataX.length - 1] + 1;
		length_dataX.push(x);
		length_dataY.push(result.queue_length);
		time_dataX.push(x);
		time_dataY.push(duration);
	    }
	    if (length_dataX.length > 60) {
		length_dataX = length_dataX.splice(1, 59);
		length_dataY = length_dataY.splice(1, 59);
		time_dataX = time_dataX.splice(1, 59);
		time_dataY = time_dataY.splice(1, 59);
	    }
						   
	    length = {
		x: length_dataX,
		y: length_dataY,
		type: 'scatter',
		name: 'Queue Length'
	    };
	    time = {
		x: time_dataX,
		y: time_dataY,
		type: 'scatter',
		name: 'Processing Time'
	    };
	    var data = [length, time];

	    var layout = {
		title:'Queue Length and Processing Duration',
		xaxis: {
		    title: 'time'
		},
		yaxis: {
		    title: "Length and Time (s)"
		}
	    };
	    
	    Plotly.newPlot('lengthChart', data, layout);
	    setTimeout(function() {
		updateLength(queueId);
	    }, 1000);
        }});
    }

    updateLength();
}

