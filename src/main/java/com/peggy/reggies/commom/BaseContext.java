package com.peggy.reggies.commom;

public class BaseContext {
    public static ThreadLocal<Long> threadLocal=new InheritableThreadLocal<>();

    public Long getContextId(){
        return threadLocal.get();
    }

    public void setContextId(Long id){
        threadLocal.set(id);
    }
}
