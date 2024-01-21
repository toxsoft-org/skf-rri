package org.toxsoft.skf.rri.lib;

import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.api.evserv.*;

/**
 * The history of the RRI parameter value editing.
 *
 * @author hazard157
 */
public interface ISkRriHistory {

  /**
   * Returns the history of the parameters change.
   * <p>
   * All returned events has the definition as {@link ISkRriServiceHardConstants#EVDTO_RRI_PARAM_CHANGE}.
   *
   * @param aInterval {@link IQueryInterval} - the query time interval
   * @param aSectionId String - identifier of the requested section
   * @return {@link ITimedList}&lt;{@link SkEvent}&gt; - list ef the edit events
   */
  ITimedList<SkEvent> querySectionEditingHistory( IQueryInterval aInterval, String aSectionId );

}
