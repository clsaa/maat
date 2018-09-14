# 1.maat
maat是一个分布式事务中间件,实现了基于可靠消息的最终一致性事务控制.其可靠消息服务通过独立消息服务方案实现,与业务系统耦合低,目前支持的MQ有RocketMQ

## 1.1.中间件架构

![image](http://clsaa-distributed-transaction-img-bed-1252032169.cossh.myqcloud.com/2018-09-14-maat%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 1.2.在一个宠物商店demo中使用

正常流程:下单->减库存->创建订单

异常流程(假如减少库存未创建订单时订单服务宕机):下单->减库存->创建订单(失败)->恢复库存

![images](http://clsaa-distributed-transaction-img-bed-1252032169.cossh.myqcloud.com/2018-09-14-petstore%E6%95%B4%E4%BD%93%E6%9E%B6%E6%9E%84%E5%9B%BE.png)
