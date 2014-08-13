package com.redis.monitor;

import java.util.HashMap;
import java.util.Map;

public class RedisInfoDetail {
	private static Map<String, String> map = new HashMap<String, String>();
	
	static {
		map.put("redis_version", "Redis 服务器版本");
		map.put("redis_git_sha1", "Git SHA1");
		map.put("redis_git_dirty", "Git dirty flag");
		map.put("os", "Redis 服务器的宿主操作系统");
		map.put("arch_bits", " 架构（32 或 64 位）");
		map.put("multiplexing_api", "Redis 所使用的事件处理机制");
		map.put("gcc_version", "编译 Redis 时所使用的 GCC 版本");
		map.put("process_id", "服务器进程的 PID");
		map.put("run_id", "Redis 服务器的随机标识符（用于 Sentinel 和集群）");
		map.put("tcp_port", "TCP/IP 监听端口");
		map.put("uptime_in_seconds", "自 Redis 服务器启动以来，经过的秒数");
		map.put("uptime_in_days", "自 Redis 服务器启动以来，经过的天数");
		map.put("lru_clock", " 以分钟为单位进行自增的时钟，用于 LRU 管理");
		map.put("connected_clients", "已连接客户端的数量（不包括通过从属服务器连接的客户端）");
		map.put("client_longest_output_list", "当前连接的客户端当中，最长的输出列表");
		map.put("client_longest_input_buf", "当前连接的客户端当中，最大输入缓存");
		map.put("blocked_clients", "正在等待阻塞命令（BLPOP、BRPOP、BRPOPLPUSH）的客户端的数量");
		map.put("used_memory", "由 Redis 分配器分配的内存总量，以字节（byte）为单位");
		map.put("used_memory_human", "以人类可读的格式返回 Redis 分配的内存总量");
		map.put("used_memory_rss", "从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）。这个值和 top 、 ps 等命令的输出一致");
		map.put("used_memory_peak", " Redis 的内存消耗峰值(以字节为单位)");
		map.put("used_memory_peak_human", "以人类可读的格式返回 Redis 的内存消耗峰值");
		map.put("used_memory_lua", "Lua 引擎所使用的内存大小（以字节为单位）");
		map.put("mem_fragmentation_ratio", "sed_memory_rss 和 used_memory 之间的比率");
		map.put("mem_allocator", "在编译时指定的， Redis 所使用的内存分配器。可以是 libc 、 jemalloc 或者 tcmalloc");
		map.put("redis_mode", "Redis 模式");
		map.put("hz", "hz");
		map.put("client_biggest_input_buf", "客户端最大的输入缓存");
		map.put("loading", "加载");
		map.put("rdb_changes_since_last_save", "自从上次dump后,改变总数");
		map.put("rdb_changes_since_last_save","自上次转储变化数");
		map.put("rdb_bgsave_in_progress","标志指示RDB保存为持续");
		map.put("rdb_last_save_time","一次成功的RDB划时代基于时间戳保存");
		map.put("rdb_last_bgsave_status","最后的RDB的状态保存操作");
		map.put("rdb_last_bgsave_time_sec","最后的RDB的时间保存操作以秒为单位");
		map.put("rdb_current_bgsave_time_sec","在正在进行的RDB的时间保存操作（如有）");
		map.put("aof_enabled","该标志指示AOF日志记录被激活");
		map.put("aof_rewrite_in_progress","标志指示AOF重写操作仍在进行");
		map.put("aof_rewrite_scheduled","标志指示的AOF重写操作将安排一次持续RDB保存完毕。");
		map.put("aof_last_rewrite_time_sec","以秒为单位的最后AOF重写操作的持续时间");
		map.put("aof_current_rewrite_time_sec","在正在进行的AOF重写操作的持续时间（如有）");
		map.put("aof_last_bgrewrite_status","最后AOF重写操作的状态");
		map.put("total_connections_received","服务器接受的连接总数");
		map.put("total_commands_processed","处理服务器的命令总数");
		map.put("instantaneous_ops_per_sec","每秒处理的指令数");
		map.put("rejected_connections","拒绝的连接数，因为MaxClients的限制");
		map.put("expired_keys","密钥过期的事件总数");
		map.put("evicted_keys","由于maxmemory限制拆迁户按键数");
		map.put("keyspace_hits","键在主字典中成功查找的次数");
		map.put("keyspace_misses","失败的查找键在主字典中的数");
		map.put("pubsub_channels","的发布/订阅频道客户端订阅全球数");
		map.put("pubsub_patterns","发布/订阅模式与客户端订阅的全球数"); 
		map.put("latest_fork_usec","以微秒为单位的最新叉操作的持续时间"); 
		map.put("role","作用"); 
		map.put("connected_slaves","从服务连接数"); 
		map.put("used_cpu_sys","系统CPU的Redis的服务器消耗");
		map.put("used_cpu_user","由Redis的服务器消耗的用户CPU");
		map.put("used_cpu_sys_children","系统CPU的后台进程消耗");  
		map.put("used_cpu_user_children","用户CPU消耗的后台进程");
		map.put("db0","db0");
		/**
		 * 失败的查找键在主字典中的数  
db0 keys=2010,expires=44 
以人类可读的格式返回 Redis 分配的内存总量 
由 Redis 分配器分配的内存总量，以字节（byte）为单位  
从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）。这个值和 top 、 ps 等命令的输出一致
Redis 的内存消耗峰值(以字节为单位) 
以人类可读的格式返回 Redis 的内存消耗峰值 
拒绝的连接数，因为MaxClients的限制 
已连接客户端的数量（不包括通过从属服务器连接的客户端）
		 */
	}
	
	private String key;
	private String value;
	private String desctiption;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
		this.desctiption = map.get(this.key);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesctiption() {
		return desctiption;
	}
	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}
	
	
}
