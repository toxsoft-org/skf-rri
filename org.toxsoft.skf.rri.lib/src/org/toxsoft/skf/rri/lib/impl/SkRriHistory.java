package org.toxsoft.skf.rri.lib.impl;

import static org.toxsoft.skf.rri.lib.impl.ISkRriServiceHardConstants.*;

import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.evserv.*;

/**
 * {@link ISkRriHistory} implementation.
 *
 * @author hazard157
 */
class SkRriHistory
    implements ISkRriHistory {

  private final SkRegRefInfoService rriService;
  private final ISkCoreApi          coreApi;

  SkRriHistory( SkRegRefInfoService aOwner ) {
    rriService = aOwner;
    coreApi = rriService.coreApi();
  }

  @Override
  public ITimedList<SkEvent> querySectionEditingHistory( IQueryInterval aInterval, String aSectionId ) {
    TsNullArgumentRtException.checkNull( aInterval );
    ISkEventService evs = coreApi.eventService();
    Gwid gwid = Gwid.createEvent( CLASSID_RRI_SECTION, aSectionId, EVID_RRI_PARAM_CHANGE );
    return evs.queryObjEvents( aInterval, gwid );
  }

}
