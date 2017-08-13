// A simple rethinkdb access app.  It allows for:
// - The creation of tables
// - The insertion of data
// - Showing the content of a table
// - Deletion of a table
var cli = require('cli'),
    rdb = require('rethinkdb'),
    fs = require('fs');

options = cli.parse({
    host:     [ 'h', 'RethinkDB hostname', 'string', 'localhost' ],
    port:     [ 'p', 'RethinkDB port', 'int', 28015 ],
    action:   [ 'a', 'Action to perform (create, insert, get, delete)', 'string', 'get' ],
    table:    [ 't', 'Table name', 'string', null ],
    datafile: [ 'f', 'RethinkDB datafile', 'string', null ]
});
const util = require('util')

var invalidcli = false;
if(options.table != null) {
    switch(options.action) {
        case 'create':
            break;
        case 'insert':
            if((options.table == null) || (options.datafile == null)) {
                invalidcli = true;
            }
        case 'get':
            break;
        case 'delete':
            break;
        default:
            invalidcli = true;
    }
} else {
    invalidcli = true;
}
if(invalidcli) {
    console.log('Invalid CLI');
    process.exit(1);
}

// perform operation
rdb.connect({ host: options.host, port: options.port }, function(err, conn) {
    if(err) throw err;
    if(options.action == 'create') {
        rdb.db('test').tableCreate(options.table).run(conn, function(err, res) {
            if (err) throw err;
            console.log(JSON.stringify(res, null, 2));
            conn.close();
        });
    } else if (options.action == 'insert') {
        fs.readFile(options.datafile, 'utf8', function(err, json) {
            if(err) throw err;
            var data = JSON.parse(json);
            console.log(data);
            rdb.table(options.table).insert(data).run(conn, function(err, res) {
                if(err) throw err;
                console.log(JSON.stringify(res, null, 2));
                conn.close();
            });
        });
    } else if (options.action == 'get') {
        rdb.table(options.table).run(conn, function(err, cursor) {
            if (err) throw err;
            cursor.toArray(function(err, res) {
                if (err) throw err;
                console.log(JSON.stringify(res, null, 2));
                conn.close();
            });
        });
    } else if (options.action == 'delete') {
        rdb.db('test').tableDrop(options.table).run(conn, function(err, res) {
            if (err) throw err;
            console.log(JSON.stringify(res, null, 2));
            conn.close();
        });
    }
});