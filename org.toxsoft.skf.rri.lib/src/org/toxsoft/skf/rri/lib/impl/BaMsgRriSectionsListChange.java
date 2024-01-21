package org.toxsoft.skf.rri.lib.impl;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.bricks.events.msg.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.backend.api.*;

/**
 * Message: RRI sections list CRUD change event.
 * <p>
 * Note: does <b>not</b> allows to create message with {@link ECrudOp#LIST}.
 *
 * @author hazard157
 */
class BaMsgRriSectionsListChange
    extends AbstractBackendMessageBuilder {

  /**
   * The message ID.
   */
  public static final String MSG_ID = "SectionsListChange"; //$NON-NLS-1$

  /**
   * The builder singleton.
   */
  public static final BaMsgRriSectionsListChange BUILDER = new BaMsgRriSectionsListChange();

  private static final String ARGID_CRUD_OP    = "CrudOp";    //$NON-NLS-1$
  private static final String ARGID_SECTION_ID = "SectionId"; //$NON-NLS-1$

  private BaMsgRriSectionsListChange() {
    super( ISkRegRefInfoService.SERVICE_ID, MSG_ID );
    defineArgNonValobj( ARGID_SECTION_ID, STRING, true );
    defineArgValobj( ARGID_CRUD_OP, ECrudOp.KEEPER_ID, true );
  }

  public GtMessage makeMessage( ECrudOp aOp, String aSectionId ) {
    TsNullArgumentRtException.checkNulls( aOp, aSectionId );
    TsNullArgumentRtException.checkTrue( aOp == ECrudOp.LIST );
    return makeMessageVarargs( ARGID_CRUD_OP, avValobj( aOp ), ARGID_SECTION_ID, aSectionId );
  }

  public ECrudOp getCrudOp( GenericMessage aMsg ) {
    return getArg( aMsg, ARGID_CRUD_OP ).asValobj();
  }

  public String getSectionId( GenericMessage aMsg ) {
    return getArg( aMsg, ARGID_SECTION_ID ).asString();
  }

}
