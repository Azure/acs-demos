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
            $("#queueLength").text(result.length);
        }});
	
        length = Math.random()
        
        setTimeout(function() {
	    updateLength(queueId);
	}, 1000);
    }
    
    updateLength();
}

