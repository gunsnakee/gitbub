flume-ng-kafka-sink
================

This project is used for [flume-ng](https://github.com/apache/flume) to communicate with [kafka 0.7,2](http://kafka.apache.org/07/quickstart.html).

Configuration of Kafka Sink
----------
0.7.2
    agent_log.sinks.kafka.type = com.vipshop.flume.sink.kafka.KafkaSink
    agent_log.sinks.kafka.channel = all_channel
    agent_log.sinks.kafka.zk.connect = 127.0.0.1:2181
    agent_log.sinks.kafka.topic = all
    agent_log.sinks.kafka.batchsize = 200
    agent_log.sinks.kafka.producer.type = async
    agent_log.sinks.kafka.serializer.class = kafka.serializer.StringEncoder

0.8.0
    kafka_sink_demo.sinks.kafka_sink.type = org.apache.flume.sink.kafka.KafkaSink
    kafka_sink_demo.sinks.kafka_sink.metadata.broker.list = 10.249.15.144:9092
    kafka_sink_demo.sinks.kafka_sink.topic = page_visits
    kafka_sink_demo.sinks.kafka_sink.serializer.class = kafka.serializer.StringEncoder
    kafka_sink_demo.sinks.kafka_sink.request.required.acks = 1

Speical Thanks
---------

In fact I'm a newbie in Java. I have learnt a lot from [flumg-ng-rabbitmq](https://github.com/jcustenborder/flume-ng-rabbitmq). Thanks to [Jeremy Custenborder](https://github.com/jcustenborder).

