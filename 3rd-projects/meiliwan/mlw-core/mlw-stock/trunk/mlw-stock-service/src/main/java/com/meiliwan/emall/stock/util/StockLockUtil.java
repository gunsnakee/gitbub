package com.meiliwan.emall.stock.util;



import com.meiliwan.emall.commons.util.concurrent.ConcurrentLRUHashMap;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 用于缓存商品库存增减操作时的锁缓存动作
 */
public class StockLockUtil {
    //定义库存缓存对象
    private static volatile ConcurrentLRUHashMap<Integer, ReentrantLock> stockLock = new ConcurrentLRUHashMap<Integer, ReentrantLock>(1000);

    //clean data 中的数据
    public static void cleanData(Integer key) {
        ReentrantLock lock = stockLock.get(key);
        if (lock != null) {
            stockLock.putIfAbsent(key, null);
        }
    }


    public static ReentrantLock get(Integer key) {
        ReentrantLock lock = stockLock.get(key);
        if (lock != null) {
            return lock;
        } else {
            lock =  new ReentrantLock();
            ReentrantLock pl = stockLock.putIfAbsent(key, lock);
            if (pl == null) {
                return lock;
            }
            return pl;
        }
    }

    public static void cleanAll() {
        stockLock.clear();
    }

    public static void unlock(int proId) {
        ReentrantLock aa = stockLock.get(proId);
        aa.unlock();
    }

}
