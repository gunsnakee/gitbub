package com.meiliwan.emall.commons.jedisTool;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.JedisClientExceptionCode;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ZKClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 缓存基本工具类 User: jiawuwu Date: 13-10-1 Time: 下午1:26 To change this template use
 * File | Settings | File Templates.
 */
public class JedisExecutor {

	private static final  MLWLogger LOGGER = MLWLoggerFactory.getLogger(JedisExecutor.class);
	private static JedisPoolConfig config = new JedisPoolConfig();
	private static JedisExecutor executor;

	static {
		config.setMaxActive(40);
		config.setMaxIdle(10);
		config.setMaxWait(1000L);
	}

	private List<JedisPool> jedisPools = new CopyOnWriteArrayList<JedisPool>();
	private Random random = new Random();

	private JedisExecutor(String configFilePath) throws JedisClientException,
			KeeperException, InterruptedException {
		init(configFilePath);
		watchDir(configFilePath);
	}

    private JedisExecutor(ServerInfo... servInfos) throws JedisConnectionException, JedisClientException {
        for (ServerInfo serverInfo : servInfos) {
            JedisPool pool = JedisExecutor.getJedisPool(serverInfo.getHost(),
                    serverInfo.getPort());
            jedisPools.add(pool);
        }


    }

    public static JedisExecutor initInstance(String configFilePath)
            throws JedisClientException, KeeperException, InterruptedException {
        if (executor == null) {
            synchronized (JedisExecutor.class) {
                if (executor == null) {
                    executor = new JedisExecutor(configFilePath);
                }
            }
        }

        return executor;
    }

    public static JedisExecutor initInstance(ServerInfo... servInfos) throws JedisConnectionException, JedisClientException {

        if (executor == null) {
            synchronized (JedisExecutor.class) {
                if (executor == null) {
                    executor = new JedisExecutor(servInfos);
                }
            }
        }

        return executor;
    }

	private synchronized void init(String configFilePath)
			throws JedisClientException, KeeperException, InterruptedException {
		ConfigUtil.initList(configFilePath);
		List<ServerInfo> rlist = ConfigUtil.getRedisNodes();
		jedisPools = new ArrayList<JedisPool>(10);
		for (ServerInfo serverInfo : rlist) {
			JedisPool pool = JedisExecutor.getJedisPool(serverInfo.getHost(),
					serverInfo.getPort());
			jedisPools.add(pool);
		}
	}

	/**
	 * 服务节点列表监控器
	 */
	private class RedisChildWatcher extends ZKClient.ChildrenWatcher {

		private String path;

		private RedisChildWatcher(String path) {
			this.path = path;
		}

		@Override
		public void nodeRemoved(String node) {
			try {
				init(path);
			} catch (Exception e) {
				LOGGER.error(e,"init(path): {path:"+path+"}",null);
			}
		}

		@Override
		public void nodeAdded(String node) {
			watchNode(path, node);
		}
	}

	/**
	 * 服务节点状态监控器
	 */
	private class RedisStringValueWatcher implements
			ZKClient.StringValueWatcher {

		private String path;

		private RedisStringValueWatcher(String path) {
			this.path = path;
		}

		@Override
		public void valueChaned(String l) {
			try {
				init(path);
			} catch (Exception e) {
                LOGGER.error(e,"init(path): {path:"+path+"}",null);
			}
		}
	}

	private void watchDir(String path) {
		RedisChildWatcher cw = new RedisChildWatcher(path);
		ZKClient.get().watchChildren(path, cw);
	}

	private void watchNode(String path, String nodeName) {
		if (path == null || nodeName == null) {
			return;
		}
		ServerInfo s = ConfigUtil.transTo(nodeName);
		if (s == null) {
			return;
		}
		RedisStringValueWatcher sw = new RedisStringValueWatcher(path);
		ZKClient.get().watchStrValueNode(path + "/" + nodeName, sw);
	}

	/**
	 * 获取Jedis连接池
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	private static JedisPool getJedisPool(String ip, int port)
			throws JedisConnectionException, JedisClientException {
		if (StringUtils.isBlank(ip) || port <= 0) {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("error", "ip is not allow null and port must >0");
			para.put("callFunc", "JedisExecutor.getJedisPool");
			para.put("ip", ip);
			para.put("port", port);
			LOGGER.warn("param invalid", para, "");
			throw new JedisClientException(
					JedisClientExceptionCode.ERROR_PARAM,
					"ip is not allow null and port must >0, param:" + para);
		}
		return new JedisPool(config, ip, port);
	}

    /**
     * 查询是否存在
     * @param
     * @param key
     * @param id
     * @return
     * @throws JedisClientException
     */
    public RedisReturn<Boolean> exists(JedisKey key, Serializable id)
            throws JedisConnectionException, JedisClientException {
        return  extExec(key,id,new ExtOper<Boolean>(ExtOperType.QUERY) {
            @Override
            Boolean exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return  jedis.exists(keystr);
            }
        },null,null);
    }

    public RedisReturn<Boolean>  hexists(JedisKey key, Serializable id, String field)throws JedisConnectionException,JedisClientException {
        return extExec(key,id,new ExtOper<Boolean>(ExtOperType.QUERY) {
            @Override
            Boolean exec(Jedis jedis, String keystr, Object field, String... values) {
                return jedis.hexists(keystr,(String)field);
            }
        },field);
    }

    /**
     * 往指定Redis节点（ip port） 执行 expire
     *
     * @param key
     * @param id
     * @param seconds
     * @return
     * @throws JedisClientException
     */
    public RedisReturn<Long> expire(JedisKey key, Serializable id,
                                    int seconds) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.EXPIRE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object seconds, String... values) {
                return jedis.expire(keystr, (Integer) seconds);
            }
        }, seconds, null);
    }

    /**
     *
     * <Description>this is a method</Description>
     *
     *
     * @param key
     *            键
     * @param value
     *            值。注意：该方法将调用value的toString()方法作为存储的实际值
     * @return 返回该键所对应值的长度
     * @throws JedisClientException
     */
    public RedisReturn<Long> append(JedisKey key, Serializable id,
                                    Object value) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.append(keystr, values[0]);
            }
        }, null, value.toString());
    }

    /**
     * 往指定Redis节点（ip port） 执行 set
     *
     * @param key
     * @param id
     * @param value
     *
     * @return
     * @throws JedisClientException
     */
    public RedisReturn<String> set(JedisKey key, Serializable id,
                                   Object value) throws JedisConnectionException, JedisClientException {

        return extExec(key, id, new ExtOper<String>(ExtOperType.CHANGE) {
            @Override
            public String exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.set(keystr, values[0]);
            }
        }, null, value.toString());

    }

    /**
     * 从指定Redis节点（ip port） 执行 get
     *
     *
     * @param key
     * @param id
     * @return
     * @throws JedisClientException
     */
    public RedisReturn<String> get( JedisKey key, Serializable id)
            throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<String>(ExtOperType.QUERY) {
            @Override
            String exec(Jedis jedis,  String keystr, Object middleKey, String... values) {
                return jedis.get(keystr);
            }
        }, null, null);
    }

    public RedisReturn<String>  getSet(JedisKey key, Serializable id,Object newValue) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<String>(ExtOperType.CHANGE) {
            @Override
            String exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.getSet(keystr,(String)middleKey);
            }
        }, newValue, null);
    }

    /**
     * 从指定Redis节点执行 del
     *
     * @param key
     * @param id
     * @return
     * @throws JedisClientException
     */
    public RedisReturn<Long> del( JedisKey key, Serializable id)
            throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.del(keystr);
            }
        }, null, null);
    }

    /**
     * 随机从任一可用Redis节点执行 hset
     *
     * @param key
     * @param id
     * @param field
     * @return
     * @throws JedisClientException
     */
    public RedisReturn<Long> hset( JedisKey key, Serializable id,
                                   String field, Object value) throws JedisConnectionException, JedisClientException {

        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.hset(keystr, (String) middleKey, values[0]);
            }
        }, field, value.toString());

    }

    public RedisReturn<String> hget( JedisKey key, Serializable id,
                                     String field) throws JedisClientException {
        return extExec(key, id, new ExtOper<String>(ExtOperType.QUERY) {
            @Override
            String exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.hget(keystr, (String) middleKey);
            }
        }, field, null);
    }

    public RedisReturn<Map<String, String>>  hgetAll(JedisKey key, Serializable id)throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Map<String, String>>(ExtOperType.QUERY) {
            @Override
            Map<String, String> exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.hgetAll(keystr);
            }
        },null);
    }

    public RedisReturn<Long> hdel(JedisKey key, Serializable id, String... fields) throws JedisConnectionException, JedisClientException{
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object middleKey, String... fields) {
                return jedis.hdel(keystr,fields);
            }
        },null,fields);
    }

    public RedisReturn<String> hmset( JedisKey key, Serializable id,
                                      Map<String, String> map) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<String>(ExtOperType.CHANGE) {
            @Override
            String exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.hmset(keystr, (HashMap<String, String>) middleKey);
            }
        }, map, null);
    }

    public RedisReturn<List<String>> hmget(JedisKey key,
                                           Serializable id, String... fields) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<List<String>>(ExtOperType.QUERY) {
            @Override
            List<String> exec(Jedis jedis, String keystr, Object middleKey, String... fields) {
                return jedis.hmget(keystr, fields);
            }
        }, null, fields);
    }

    public RedisReturn<Long>  sadd( JedisKey key, Serializable id,
                                    String... members) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object middleKey, String... members) {
                return jedis.sadd(keystr, members);
            }
        }, null, members);
    }

    public RedisReturn<Set<String>> smembers( JedisKey key,
                                              Serializable id) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Set<String>>(ExtOperType.QUERY) {
            @Override
            Set<String> exec(Jedis jedis, String keystr, Object middleKey, String... values) {
                return jedis.smembers(keystr);
            }
        }, null);
    }

    public RedisReturn<Long> srem( JedisKey key, Serializable id,
                                   String... members) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object score, String... values) {
                return jedis.srem(keystr, values);
            }
        }, null, members);
    }

    public RedisReturn<List<String>> sort( JedisKey key, Serializable id)
            throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<List<String>>(ExtOperType.QUERY) {
            @Override
            List<String> exec(Jedis jedis, String keystr, Object score, String... values) {
                return jedis.sort(keystr);
            }
        }, null);
    }

    public RedisReturn<List<String>> sort( JedisKey key, Serializable id,
                                           SortingParams params) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<List<String>>(ExtOperType.QUERY) {
            @Override
            List<String> exec(Jedis jedis, String keystr, Object params, String... values) {
                return jedis.sort(keystr, (SortingParams) params);
            }
        }, params);
    }

    public RedisReturn<Long> zadd( JedisKey key, Serializable id,
                                   double score, Object value) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object score, String... values) {
                return jedis.zadd(keystr, (Double) score, values[0]);
            }
        }, score, value.toString());
    }

    public RedisReturn<Long> zadd( JedisKey key, Serializable id,
                                   Map<Double, String> scoreMembers) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object scoreMembers, String... values) {
                return jedis.zadd(keystr, (Map<Double, String>) scoreMembers);
            }
        }, scoreMembers);
    }

    public RedisReturn<Long> zrem( JedisKey key, Serializable id,
                                   String... members) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object middleKey, String... members) {
                return jedis.zrem(keystr, members);
            }
        }, null, members);
    }

    public RedisReturn<Long> decr( JedisKey key, Serializable id)
            throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object middleKey, String... members) {
                return jedis.decr(keystr);
            }
        }, null);
    }

    public RedisReturn<Long> decrBy( JedisKey key, Serializable id,
                                     int byvalue) throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object byvalue, String... members) {
                return jedis.decrBy(keystr, (Long)byvalue);
            }
        },Long.parseLong(byvalue+""));
    }

    public RedisReturn<Long> incr( JedisKey key, Serializable id)
            throws JedisConnectionException, JedisClientException {
        return extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object middleKey, String... members) {
                return jedis.incr(keystr);
            }
        }, null);
    }

    public RedisReturn<Long> incrBy( JedisKey key, Serializable id,
                                     int byvalue) throws JedisConnectionException, JedisClientException {
        return  extExec(key, id, new ExtOper<Long>(ExtOperType.CHANGE) {
            @Override
            Long exec(Jedis jedis, String keystr, Object byvalue, String... members) {
                return jedis.incrBy(keystr, (Long) byvalue);
            }
        }, Long.parseLong(byvalue+""));
    }


	/**
	 * <Description>this is a method</Description>
	 * 
	 * @param key
	 *            JedisKey对象
	 * @param id
	 *            键的唯一标识
	 * @return 返回由key.buildKey()+JedisKey.KEY_SPLITER+id.toString()形式的字符串
	 */
	private static String buildKey(JedisKey key, Serializable id) {
		return key.buildKey() + JedisKey.KEY_SPLITER + id.toString();
	}

    /**
     *
     * <Description>对于那些在本类中暂未实现的方法，可以由此方法进行扩展</Description>
     *
     * @param <T>
     *            范型，定义返回类型
     * @param key
     *            键类型
     * @param id
     *            键的唯一标识值
     * @param oper
     *            ExtOper<T>的子类对象
     * @param middleKey
     *            只有hset、zadd这种类型，需要有一个中间域标识的数据类型才需要传值，其它可以传null
     * @return 返回指定类型的对象
     */
    private <T> T extExec(Jedis jedis, JedisKey key, Serializable id,
                          ExtOper<T> oper, Object middleKey, String... values)
            throws JedisConnectionException, JedisDataException,
            JedisClientException {

        long accessTime = System.nanoTime();

        if (jedis == null || key == null || id == null) {
            throw new JedisClientException(
                    JedisClientExceptionCode.ERROR_PARAM,
                    "jedis instanse,key,id must not null, param(jedis:" + jedis
                            + ",jedisKey:" + key + ",id:" + id + ",middleKey:"
                            + middleKey + (ExtOperType.QUERY.equals(oper.getExOperType()) ? "" : ",values:"
                            + (values == null ? null : Arrays.asList(values)))
                            + ")");
        }

        String keystr = buildKey(key, id);

        LOGGER.debug("keystr:" + keystr + ",operType:" + oper.getExOperType()
                + ",operCmd:" + key.getKeyType() + ",middleKey:" + middleKey
                + (ExtOperType.QUERY.equals(oper.getExOperType()) ? "" : ",values:" + (values == null ? null : Arrays.asList(values))));

        T rs = oper.exec(jedis, keystr, middleKey, values);
        if (key.getExpTime() > 0 && oper.getExOperType() == ExtOperType.CHANGE) {
            jedis.expire(keystr, key.getExpTime());
        }

        TimeLog.log(
                key,
                id,
                accessTime,
                "finish execute extExec(Jedis jedis ,JedisKey key, Serializable id, ExtOper<T> oper,Object middleKey, String... values).");

        return rs;

    }

    /**
     * <Description>对于那些在本类中暂未实现的方法，可以由此方法进行扩展</Description>
     *
     * @param key
     *            键类型
     * @param id
     *            键的唯一标识值
     * @param oper
     *            ExtOper<T>的子类对象
     * @param middleKey
     *            只有hset、zadd这种类型，需要有一个中间域标识的数据类型才需要传值，其它可以传null
     * @param values
     * @param <T>
     *            范型，定义返回类型
     * @return 返回指定类型的对象
     * @throws JedisClientException
     */
    private  <T> RedisReturn<T> extExec(JedisKey key, Serializable id,
                                      ExtOper<T> oper, Object middleKey, String... values)
            throws JedisConnectionException,JedisClientException {

        precheck(key, id);

        boolean hasPool = (jedisPools.size() == 0);
        RedisReturn<T> redisReturn = new RedisReturn<T>(hasPool);
        if (hasPool) {
            LOGGER.warn("all redis has bean down", "JedisKey:" + key + ",id:"
                    + id + ",middleKey:" + middleKey + (ExtOperType.QUERY.equals(oper.getExOperType()) ? "" : ",values:"
                    + (values == null ? null : Arrays.asList(values))), "");
            return redisReturn;
        }

        T result = null;
        if (ExtOperType.CHANGE.equals(oper.getExOperType())
                || ExtOperType.EXPIRE.equals(oper.getExOperType())) {
            Iterator<JedisPool> itr = jedisPools.iterator();
            while (itr.hasNext()) {
                result = execByPool(itr.next(), key, id, oper, middleKey,
                        values);
                if (result != null) {
                    redisReturn.setOperRs(result);
                }
            }
        } else {
            int hit = random.nextInt(jedisPools.size());

            result = execByPool(jedisPools.get(hit), key, id, oper, middleKey,
                    values);
            redisReturn.setOperRs(result);
        }

        return redisReturn;
    }

    private <T> T execByPool(JedisPool pool, JedisKey key, Serializable id,
                             ExtOper<T> oper, Object middleKey, String... values)
            throws JedisConnectionException,JedisClientException {
        long accessTime = System.nanoTime();
        Jedis jedis = null;
        int index = 0;
        for (; index < JedisCommon.RETRY_TIMES; index++) {
            try {
                LOGGER.debug("extExec param(host:" + pool.getHost() + ",port:"
                        + pool.getPort() + ",jedisKey:" + key + ",id:" + id
                        + ",middleKey:" + middleKey + (ExtOperType.QUERY.equals(oper.getExOperType()) ? "" : ",values:"
                        + (values == null ? null : Arrays.asList(values))) + ")");
                jedis = pool.getResource();
                T result = extExec(jedis, key, id, oper, middleKey, values);

                return result;
            } catch (JedisConnectionException jce) {

                // 这个地方需要重点测试一下
                if (jedis == null || !jedis.isConnected()) {
                    synchronized (this) {
                        if (jedis == null || !jedis.isConnected()) {
                            JedisPool deadPool = pool;
                            pool = retryConnect(pool, jedis, index);
                            try {
                                jedisPools.remove(deadPool);
                                jedisPools.add(pool);
                            } catch (Exception ex) {
                                LOGGER.error(ex, pool, "");
                            }
                        }
                    }
                }
            } catch (JedisDataException jde) {
                throw new JedisClientException(
                        JedisClientExceptionCode.ERROR_PARAM, "param(host:"
                        + pool.getHost()
                        + ",port:"
                        + pool.getPort()
                        + ",jedisKey:"
                        + key
                        + ",id:"
                        + id
                        + ",middleKey:"
                        + middleKey
                        + ",values:"
                        + (values == null ? null
                        : Arrays.asList(values)) + ")");
            } finally {
                pool.returnResource(jedis);
                TimeLog.log(
                        key,
                        id,
                        accessTime,
                        "finish extExec(JedisPool pool ,JedisKey key, Serializable id, ExtOper<T> oper,Object middleKey, String... values). -> "
                                + pool.getHost() + " " + pool.getPort());
            }
        }

        if (index >= JedisCommon.RETRY_TIMES) {

            try {
                ZKClient.get().setData(
                        JedisCommon.GROUP_NODE + "/r" + pool.getHost() + ":"
                                + pool.getPort(), "false".getBytes());
                LOGGER.info("suceess set false to Zookeeper. " + pool.getHost()
                        + " " + pool.getPort(), null, null);
            } catch (Exception e1) {
                Map<String, Object> para = new HashMap<String, Object>();
                para.put("nodePath",
                        JedisCommon.GROUP_NODE + "/r" + pool.getHost() + ":"
                                + pool.getPort());
                para.put("content", "false".getBytes());
                LOGGER.error(e1, para, "");
            }
        }

        return null;
    }

    private static JedisPool retryConnect(JedisPool pool, Jedis currJedis,
                                          int index) throws JedisClientException {
        try {
            Thread.sleep(JedisCommon.SLEEP_TIME);
            pool.returnResource(currJedis);
            pool.destroy();
        } catch (Exception e1) {
            LOGGER.error(e1, "sleep in getJedis method error", "");
        }

        LOGGER.debug("retry create jedis instanse. -> " + index);

        try {
            pool = getJedisPool(pool.getHost(), pool.getPort());
        } catch (JedisConnectionException ex) {
            LOGGER.error(ex, pool, "");
        }

        return pool;
    }

    private static void precheck(JedisKey key, Serializable id)
            throws JedisClientException {
        if (key == null || id == null) {
            throw new JedisClientException("400", "key or id is not allow null");
        }
    }

}

