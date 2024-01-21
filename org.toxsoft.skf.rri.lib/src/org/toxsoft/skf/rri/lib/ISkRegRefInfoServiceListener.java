package org.toxsoft.skf.rri.lib;

import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.api.evserv.*;

/**
 * {@link ISkRegRefInfoService} changes listener.
 * <p>
 * Note: this interface does <b>not</b> allows to listen to the changes of the RRI properties values. Common
 * {@link ISkEventService} must be used to listen to the events
 * {@link ISkRriServiceHardConstants#EVDTO_RRI_PARAM_CHANGE}.
 *
 * @author hazard157
 */
public interface ISkRegRefInfoServiceListener {

  /**
   * Informs about changes in sections.
   * <p>
   * When section properties or section RRI parameters definitions changes, this method is called with
   * {@link ECrudOp#EDIT} argument.
   * <p>
   * Even with batch edits, changes are remembered and events are generated for each section separately.
   *
   * @param aOp {@link ECrudOp} - the kind of changes
   * @param aSectionId String - identifier of the changed section, never is <code>null</code>
   */
  void onSectionChanged( ECrudOp aOp, String aSectionId );

}
