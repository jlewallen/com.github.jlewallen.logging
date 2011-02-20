package com.github.jlewallen.logging;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * See http://blog.cherouvim.com/a-better-smtpappender/
 */
public class ThrottledEventEvaluator implements TriggeringEventEvaluator {

   private int maximumPerInterval;

   private int intervalLength;

   public int getMaximumPerInterval() {
      return maximumPerInterval;
   }

   public void setMaximumPerInterval(int maximumPerInterval) {
      this.maximumPerInterval = maximumPerInterval;
   }

   public int getIntervalLength() {
      return intervalLength;
   }

   public void setIntervalLength(int intervalLength) {
      this.intervalLength = intervalLength;
   }

   private long lastCycle;

   private long numberOfTriggers;

   public boolean isTriggeringEvent(LoggingEvent event) {
      if(intervalLength == 0) return false;
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
