// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.4.2
//
// <auto-generated>
//
// Generated from file `HelloWorldServiceIcePrxHelper.java'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.meiliwan.interf;

public final class HelloWorldServiceIcePrxHelper extends Ice.ObjectPrxHelperBase implements HelloWorldServiceIcePrx
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7447009071591651927L;

	public String
    sayHello(String str)
    {
        return sayHello(str, null, false);
    }

    public String
    sayHello(String str, java.util.Map<String, String> __ctx)
    {
        return sayHello(str, __ctx, true);
    }

    private String
    sayHello(String str, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("sayHello");
                __delBase = __getDelegate(false);
                _HelloWorldServiceIceDel __del = (_HelloWorldServiceIceDel)__delBase;
                return __del.sayHello(str, __ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    private static final String __sayHello_name = "sayHello";

    public Ice.AsyncResult begin_sayHello(String str)
    {
        return begin_sayHello(str, null, false, null);
    }

    public Ice.AsyncResult begin_sayHello(String str, java.util.Map<String, String> __ctx)
    {
        return begin_sayHello(str, __ctx, true, null);
    }

    public Ice.AsyncResult begin_sayHello(String str, Ice.Callback __cb)
    {
        return begin_sayHello(str, null, false, __cb);
    }

    public Ice.AsyncResult begin_sayHello(String str, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_sayHello(str, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_sayHello(String str, Callback_HelloWorldServiceIce_sayHello __cb)
    {
        return begin_sayHello(str, null, false, __cb);
    }

    public Ice.AsyncResult begin_sayHello(String str, java.util.Map<String, String> __ctx, Callback_HelloWorldServiceIce_sayHello __cb)
    {
        return begin_sayHello(str, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_sayHello(String str, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__sayHello_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __sayHello_name, __cb);
        try
        {
            __result.__prepare(__sayHello_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__os();
            __os.writeString(str);
            __os.endWriteEncaps();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public String end_sayHello(Ice.AsyncResult __result)
    {
        Ice.AsyncResult.__check(__result, this, __sayHello_name);
        if(!__result.__wait())
        {
            try
            {
                __result.__throwUserException();
            }
            catch(Ice.UserException __ex)
            {
                throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
            }
        }
        String __ret;
        IceInternal.BasicStream __is = __result.__is();
        __is.startReadEncaps();
        __ret = __is.readString();
        __is.endReadEncaps();
        return __ret;
    }

    public static HelloWorldServiceIcePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        HelloWorldServiceIcePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (HelloWorldServiceIcePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    HelloWorldServiceIcePrxHelper __h = new HelloWorldServiceIcePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static HelloWorldServiceIcePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        HelloWorldServiceIcePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (HelloWorldServiceIcePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    HelloWorldServiceIcePrxHelper __h = new HelloWorldServiceIcePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static HelloWorldServiceIcePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        HelloWorldServiceIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    HelloWorldServiceIcePrxHelper __h = new HelloWorldServiceIcePrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static HelloWorldServiceIcePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        HelloWorldServiceIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    HelloWorldServiceIcePrxHelper __h = new HelloWorldServiceIcePrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static HelloWorldServiceIcePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        HelloWorldServiceIcePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (HelloWorldServiceIcePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                HelloWorldServiceIcePrxHelper __h = new HelloWorldServiceIcePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static HelloWorldServiceIcePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        HelloWorldServiceIcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            HelloWorldServiceIcePrxHelper __h = new HelloWorldServiceIcePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::com::meliwan::interf::HelloWorldServiceIce"
    };

    public static String
    ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _HelloWorldServiceIceDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _HelloWorldServiceIceDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, HelloWorldServiceIcePrx v)
    {
        __os.writeProxy(v);
    }

    public static HelloWorldServiceIcePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            HelloWorldServiceIcePrxHelper result = new HelloWorldServiceIcePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
