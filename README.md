# babar
babar is a light weight RPC framework built on Netty 4
## Some key features:
* Build on Netty 4 using NIO for high performance.
* Fully utilized Spring/Spring boot for bean management.
* Unblock NIO thread in server side by creating a tread pool to handle actual business transaction.
* Create connection pool in client side to re-use connection between server and client.
* Using Zookeeper for service discovery and registry.
