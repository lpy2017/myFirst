package com.dc.appengine.node.redis;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.dc.appengine.node.configuration.model.NodeProperties;

public class JedisClientImpl implements JedisClient {
	private static JedisPool jedisPool;
	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(NodeProperties.getRedisMaxTotal());
		config.setMaxIdle(NodeProperties.getRedisMaxIdle());
		config.setMaxWaitMillis(NodeProperties.getMaxWaitMillis());
		config.setTestOnBorrow(NodeProperties.isTestOnBorrow());
		config.setTestOnReturn(NodeProperties.isTestOnReturn());
		jedisPool = new JedisPool(config, NodeProperties.getRedisIp(),
				NodeProperties.getRedisPort(),
				NodeProperties.getRedisTimeOut(), NodeProperties.getRedisAuth());
	}

	@SuppressWarnings("deprecation")
	@Override
	public String get(String key) {
		String value = null;
		Jedis jedis = jedisPool.getResource();
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public byte[] get(byte[] key) {
		byte[] value = null;
		Jedis jedis = jedisPool.getResource();
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		try {
			value = jedis.set(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String set(String key, String value, int expire) {
		Jedis jedis = jedisPool.getResource();
		try {
			value = jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String set(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		String v = null;
		try {
			v = jedis.set(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return v;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String set(byte[] key, byte[] value, int expire) {
		Jedis jedis = jedisPool.getResource();
		String v = null;
		try {
			v = jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return v;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String hget(String hkey, String key) {
		String value = null;
		Jedis jedis = jedisPool.getResource();
		try {
			value = jedis.hget(hkey, key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long hset(String hkey, String key, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.hset(hkey, key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.incr(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long expire(String key, int second) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.expire(key, second);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.ttl(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.del(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long del(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.del(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long hdel(String hkey, String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.hdel(hkey, key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			return null;
		}
		try {
			keys = jedis.keys(pattern.getBytes());
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return keys;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void flushDB() {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.flushDB();
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long dbSize() {
		Long dbSize = 0L;
		Jedis jedis = jedisPool.getResource();
		try {
			dbSize = jedis.dbSize();
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return dbSize;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long push(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = null;
		try {
			result = jedis.rpush(key, value);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void lpop(String key) {
		Jedis jedis = jedisPool.getResource();

		try {
			jedis.lpop(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public List<String> lrange(String key, int start, int end) {
		Jedis jedis = jedisPool.getResource();
		List<String> result = null;
		try {
			result = jedis.lrange(key, start, end);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String index(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = null;
		try {
			result = jedis.lindex(key, -1);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long llength(String key) {
		Jedis jedis = jedisPool.getResource();
		long result = 0;
		try {
			result = jedis.llen(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
}
