flume-ng-kafka-source
================

This project is used for [flume-ng](https://github.com/apache/flume) to communicate with [kafka 0.7,2](http://kafka.apache.org/07/quickstart.html).

Configuration of Kafka Source
----------
0.7.2
    agent_log.sources.kafka0.type = com.vipshop.flume.source.kafka.KafkaSource
    agent_log.sources.kafka0.zk.connect = 127.0.0.1:2181
    agent_log.sources.kafka0.topic = all
    agent_log.sources.kafka0.groupid = es
    agent_log.sources.kafka0.channels = channel0

0.8.0
    agent_log.sources.kafka0.type = org.apache.flume.source.kafka.KafkaSource
    agent_log.sources.kafka0.zookeeper.connect = 10.249.15.194:2181
	agent_log.sources.kafka0.group.id = group1
	agent_log.sources.kafka0.zookeeper.session.timeout.ms = 400
	agent_log.sources.kafka0.zookeeper.sync.time.ms = 200
	agent_log.sources.kafka0.auto.commit.interval.ms = 1000

Speical Thanks
---------

In fact I'm a newbie in Java. I have learnt a lot from [flumg-ng-rabbitmq](https://github.com/jcustenborder/flume-ng-rabbitmq). Thanks to [Jeremy Custenborder](https://github.com/jcustenborder).

