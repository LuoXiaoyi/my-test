package com.jmh.generated;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
public class First_jmhType_B2 extends First_jmhType_B1 {
    public volatile int setupTrialMutex;
    public volatile int tearTrialMutex;
    public final static AtomicIntegerFieldUpdater<First_jmhType_B2> setupTrialMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(First_jmhType_B2.class, "setupTrialMutex");
    public final static AtomicIntegerFieldUpdater<First_jmhType_B2> tearTrialMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(First_jmhType_B2.class, "tearTrialMutex");

    public volatile int setupIterationMutex;
    public volatile int tearIterationMutex;
    public final static AtomicIntegerFieldUpdater<First_jmhType_B2> setupIterationMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(First_jmhType_B2.class, "setupIterationMutex");
    public final static AtomicIntegerFieldUpdater<First_jmhType_B2> tearIterationMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(First_jmhType_B2.class, "tearIterationMutex");

    public volatile int setupInvocationMutex;
    public volatile int tearInvocationMutex;
    public final static AtomicIntegerFieldUpdater<First_jmhType_B2> setupInvocationMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(First_jmhType_B2.class, "setupInvocationMutex");
    public final static AtomicIntegerFieldUpdater<First_jmhType_B2> tearInvocationMutexUpdater = AtomicIntegerFieldUpdater.newUpdater(First_jmhType_B2.class, "tearInvocationMutex");

}
