jGroups Testing
===============

This is for testing out jgroups setup. Things aren't parameterised as they
should be as it's a quick hack to test this stuff. This setup uses the
database for node discovery as we don't have multicast working across the 2 
datacentres and all the nodes need a working database to function so it's
not really a limit to availability.

Building
--------

Before running you need build the Java project. This will create the jGroups
test app that is bundled into the docker image.

    mvn install

Running
-------

It's most useful to startup the database on it's own and leave it running
in the background. It will attempt to listen locally on port 3306 so you can
connect to it (user/pass jgroups/jgroups) with a normal MySQL client and see
the registered nodes.

    docker-compose up -d db
    docker-compose up app
    
You can then bring up and down more nodes and check they join/leave the cluster
by using the scale option for the ``up`` command:

    docker-compose up --scale app=4 app

