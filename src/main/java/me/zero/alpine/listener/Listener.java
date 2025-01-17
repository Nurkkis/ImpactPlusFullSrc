package me.zero.alpine.listener;

import java.util.function.Predicate;
import net.jodah.typetools.TypeResolver;

public final class Listener<T> implements EventHook<T> {
  private final Class<T> target;
  
  private final EventHook<T> hook;
  
  private final Predicate<T>[] filters;
  
  private final byte priority;
  
  @SafeVarargs
  public Listener(EventHook<T> hook, Predicate<T>... filters) {
    this(hook, (byte)3, filters);
  }
  
  @SafeVarargs
  public Listener(EventHook<T> hook, byte priority, Predicate<T>... filters) {
    this.hook = hook;
    this.priority = priority;
    this.target = (Class<T>) TypeResolver.resolveRawArgument(EventHook.class, hook.getClass());
    this.filters = filters;
  }
  
  public final Class<T> getTarget() {
    return this.target;
  }
  
  public final byte getPriority() {
    return this.priority;
  }
  
  public final void invoke(T event) {
    if (this.filters.length > 0)
      for (Predicate<T> filter : this.filters) {
        if (!filter.test(event))
          return; 
      }  
    this.hook.invoke(event);
  }
}
