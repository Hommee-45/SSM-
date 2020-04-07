package com.rupeng.web.redis;

public interface JedisClient {

	  String get(String key);
	    String set(String key,String value);
	    String setex(String key,int time,String value);
	    String hget(String hkey,String field);//hashmap 取值设值
	    Long hset(String hkey,String field,String value);
	    long incr(String key);//自增长
	    Long expire(String key,int second);//设置key有效期
	    long ttl(String key);//查看key的有效期
	    
}
