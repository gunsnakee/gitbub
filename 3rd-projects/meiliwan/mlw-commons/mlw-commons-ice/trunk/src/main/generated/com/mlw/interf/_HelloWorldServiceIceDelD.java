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
// Generated from file `_HelloWorldServiceIceDelD.java'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.mlw.interf;

public final class _HelloWorldServiceIceDelD extends Ice._ObjectDelD implements _HelloWorldServiceIceDel
{
    public String
    sayHello(final String str, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "sayHello", Ice.OperationMode.Normal, __ctx);
        final Ice.StringHolder __result = new Ice.StringHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    HelloWorldServiceIce __servant = null;
                    try
                    {
                        __servant = (HelloWorldServiceIce)__obj;
                    }
                    catch(ClassCastException __ex)
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    __result.value = __servant.sayHello(str, __current);
                    return Ice.DispatchStatus.DispatchOK;
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.servant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
                return __result.value;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
        return __result.value;
    }
}
