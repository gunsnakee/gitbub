package org.jiawu.function;

/**
 * 创建了这么一个抽像类，把Java中的函数也变成了一个对像，就像Javascript一样
 *
 */
public abstract class Function {

    public abstract Object apply(Object[] args);
    public Object call(Object... args){
        return  apply(args);
    }
}
