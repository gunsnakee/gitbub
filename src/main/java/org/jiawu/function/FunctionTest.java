package org.jiawu.function;

import java.util.*;

/**
 * 在Java中模拟高阶函数
 * http://benjycui.com/java/2014/04/14/higher-order-function-in-java.html
 *
 */
public class FunctionTest {
    public static void main(String[] args){
        Function greet = new Function() {
            @Override
            public Object apply(Object[] args) {
                System.out.println("Hello "+args[0]);
                return null;
            }
        };
        greet.call("jiawu");


        (new Function(){
            @Override
            public Object apply(Object[] args) {
                System.out.println("execute imedeilate ");
                return null;
            }
        }).call();


        // 这段map的代码没看懂，是从网上抄袭的
        Function map = new Function() {
            @Override
            public Object apply(Object[] args) {
                Function fn = (Function)args[0];
                List coll = (List)args[1];
                Iterator itr = coll.iterator();
                List result = new ArrayList();
                while (itr.hasNext()){
                    result.add(fn.call(itr.next()));
                }
                return result;
            }
        };

        //如果我们要把一个集合中所有的小写字母转成大写字母（或者其它更加复杂的映射工作），就要自己去迭代整个集合，
        //并把处理后的结果add到新的集合里。而在支持高阶函数的Java里，你只需要把映射函数及集合传进map，
        // 你要可以得到你想要的集合咯。这也是函数式编程关注What to do而非How to do的一个体现

        // ['a', 'b', 'c'] -> ['A', 'B', 'C']
        Collection lowercase = new ArrayList();
        lowercase.add('a');
        lowercase.add('b');
        lowercase.add('c');
        List uppercase = (List)map.call(new Function() {
            @Override
            public Object apply(Object[] args) {
                return Character.toUpperCase((Character)args[0]);
            }
        },lowercase);

        System.out.println(uppercase);



        //reduce(fn, collection) or reduce(fn, collection, init)
        //将一个collection里的所有值归结到一个单独的数值。init是归结的初始值，如果不传入，就会把collection的第一个值赋给init。每一步都由fn返回。fn会传入2个参数：init，value。

        Function reduce = new Function() {
            @Override
            public Object apply(Object[] args) {
                Function fn = (Function)args[0];
                List coll = (List)args[1];
                Iterator itr = coll.iterator();
                Object init = args.length == 3 ? args[2] : itr.next();
                while (itr.hasNext()){
                    init = fn.call(init,itr.next());
                }
                return init;
            }
        };

        // 现在我们可以用下面的方式去求和咯：
        Function add = new Function() {
            @Override
            public Object apply(Object[] args) {
                return (Integer)args[0]+(Integer)args[1];
            }
        };

        // coll = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        Collection coll = new ArrayList();
        coll.add(1);
        coll.add(2);
        coll.add(3);
        coll.add(4);
        coll.add(5);
        coll.add(6);
        coll.add(7);
        coll.add(8);
        coll.add(9);
        coll.add(10);


        int sum1 = (Integer)reduce.call(add, coll);
        System.out.println(sum1); // 55

        int sum2 = (Integer)reduce.call(add, coll, 11);
        System.out.println(sum2); // 66


        // not(fn)
        // 传入一个谓词函数，返回一个新的谓词函数，并且新的谓词函数的判断总是与原来的相反。

        Function not = new Function() {
            @Override
            public Object apply(Object[] args) {
                final Function fn = (Function)args[0];
                return new Function() {
                    @Override
                    public Object apply(Object[] args) {
                        return !(Boolean)fn.apply(args);
                    }
                };
            }
        };

        // 判断一个数字是否为偶数
        Function isEven = new Function() {
            @Override
            public Object apply(Object[] args) {
                return (Integer)args[0] % 2 == 0 ;
            }
        };

        System.out.println(isEven.call(2)); // true
        System.out.println(isEven.call(3)); // false

        // 一个数字如果不是偶数，那它就一定是奇数
        Function isOdd = (Function)not.call(isEven);

        System.out.println(isOdd.call(2)); // false
        System.out.println(isOdd.call(3)); // true



        //curry化

        //在计算机科学中，柯里化（英语：Currying），又译为卡瑞化或加里化，是把接受多个参数的函数变换成接受一个单一参数（最初函数的第一个参数）的函数，
        // 并且返回接受余下的参数而且返回结果的新函数的技术。这个技术由 Christopher Strachey 以逻辑学家哈斯凯尔·加里命名的，尽管它是 Moses Schönfinkel 和 戈特洛布·弗雷格 发明的。 -- 维基百科


        Function curry = new Function() {
            @Override
            public Object apply(Object[] args) {
                final Function fn = (Function)args[0];
                final Object[] argument = args;
                return new Function() {
                    @Override
                    public Object apply(Object[] args) {
                        Object[] a = new Object[argument.length+args.length-1];
                        System.arraycopy(argument,1,a,0,argument.length-1);
                        System.arraycopy(args,0,a,argument.length-1,args.length);
                        return fn.apply(a);
                    }
                };
            }
        };


        Function inc = (Function)curry.call(add, 1);
        System.out.println(inc.call(5)); // 6



        //memoize(fn)
        // 传入一个函数，然后返回一个功能相同，但是有记忆功能，即不会进行重复计算的函数。

        Function memoize = new Function() {
            @Override
            public Object apply(Object[] args) {
                final Function fn = (Function)args[0];
                final HashMap<String,Object> cache = new HashMap<String, Object>();
                return new Function() {
                    @Override
                    public Object apply(Object[] args) {
                        String key = "";
                        for(Object o: args){
                            key += args.hashCode();
                        }
                        Object result = cache.get(key);
                        if(result==null){
                            result = fn.apply(args);
                            cache.put(key,result);
                        }
                        return result;
                    }
                };
            }
        };

        // 如果一个函数要进行大量的计算，并且只要传入的参数一样，那么返回值就一定会一样。这种情况下我们会怎样优化呢？缓存？
        // 是的，我们可以把计算过的结果缓存起来。然后不好意思，我很懒，我既不想改函数的实现，也不想自己去维护缓存。这时，我的救世主从天而降--memoize。现在，我可以用一行代码完成这个工作咯。

        // 假设有一个非常耗时的计算
        Function verySlowComputation = new Function() {
            public Object apply (Object[] args) {
                System.out.println("Why do you call me?" + (Integer)args[0]);
                return args[0];
            }
        };

        // 每次调用都会重复计算，消耗大量的计算资源
        verySlowComputation.call(1); // Why do you call me?1
        verySlowComputation.call(1); // Why do you call me?1

        // 只要一次函数调用，就可以让一个函数具有缓存功能
        Function memVerySlowComputation = (Function)memoize.call(verySlowComputation);

        memVerySlowComputation.call(1); // Why do you call me?1
        memVerySlowComputation.call(1); // 没有输出，因为之前的结果已经缓存，所以不再重复计算
        memVerySlowComputation.call(2); // Why do you call me?2






    }
}
