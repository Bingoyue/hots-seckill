###  项目简介

​        基于springcloud alibaba全家桶+Mybatis+Mysql+RabbitMq+Redis等构建的微服务高并发秒杀系统，包括秒杀服务、商品服务、订单服务、用户服务。解决秒杀过程中涉及到的高并发、超卖、接口直调、数据库性能瓶颈等问题。具体解决方案如下：

####         一、高并发解决方案

​        1、限流和熔断

​             自定义注解避免单用户重复提交 ，并引入sentinel对秒杀接口限流和熔断

​         2、预扣减库存

​              库存信息存入redis,秒杀请求先请求redis判断是否有库存，如果有，扣减库存，此过程保证原子性。

​         3、异步下单

​              用户下单不直接调用下单服务，而是将消息写入RabbitMQ，订单服务消费MQ中的订单消息，进行异步下单和扣减真实库存

​         4、微服务架构

​               采用微服务架构，分布式部署，便于扩容，解决单台服务器性能问题

​         5、分布式事务

​              下订单与扣减真实库存环节，涉及到分布式事务，本方案摒弃刚性事务解决方案，基于base理论，采用MQ+定时任务的柔性事务解决方案

​        

####          二、超卖

​               从redis中进行预扣减时，判断与扣减保证原子性；真实扣减采用乐观锁的方式扣减。



####         三、接口直调

​               动态化秒杀URL,采用用户ID+商品ID+随机数等信息组成用户指纹，对其进行编码后，作为该用户的秒杀地址

​         



​        