package com.jl.com.jl.logging.appenders;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * See http://blog.cherouvim.com/a-better-smtpappender/
 */
public class ThrottledEventEvaluator implements TriggeringEventEvaluator {

   private int maximumPerInterval;

   private int intervalLength;

   public void setMaximumPerInterval(int maximumPerInterval) {
      this.maximumPerInterval = maximumPerInterval;
   }

   public void setIntervalLength(int intervalLength) {
      this.intervalLength = intervalLength;
   }

   private long lastCycle;

   private long numberOfTriggers;

   public boolean isTriggeringEvent(LoggingEvent event) {
      if(!isErrorOrHigher(event)) {
         return false;
      }
      final long now = System.currentTimeMillis();
      final long thisCycle = now - (now % (1000L * intervalLength));
      if(lastCycle != thisCycle) {
         lastCycle = thisCycle;
         numberOfTriggers = 0;
      }
      numberOfTriggers++;
      return numberOfTriggers <= maximumPerInterval;
   }

   private static boolean isErrorOrHigher(LoggingEvent event) {
      return event.getLevel().isGreaterOrEqual(Level.ERROR);
   }

}
