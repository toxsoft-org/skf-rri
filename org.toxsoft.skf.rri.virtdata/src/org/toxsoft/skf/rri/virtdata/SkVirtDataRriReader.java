package org.toxsoft.skf.rri.virtdata;

import static org.toxsoft.skf.rri.lib.impl.ISkRriServiceHardConstants.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.evserv.*;

/**
 * RegRefInfo (RRi) reader for a virtual data.
 *
 * @author mvk
 */
public class SkVirtDataRriReader
    implements ICloseable {

  private final ISkCoreApi             coreApi;
  private final Skid                   objId;
  private final ISkRriSection          rriSection;
  private final IGwidList              rriIds;
  private final IGenericChangeListener changeListener;
  private final InternalHandler        handler;

  private static final String defaultRriSectionId = "sk.service.sysext.regref.comp.rri.section.id"; //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param aCoreApi {@link ISkCoreApi} connection API.
   * @param aObjId {@link Skid} ID of the object of the written data.
   * @param aRriIds {@link IStringList} list of read RRI IDs.
   * @param aChangeListener {@link IGenericChangeListener} input data change listener
   * @throws TsNullArgumentRtException any argument = null.
   * @throws TsIllegalArgumentRtException object of another class.
   */
  public SkVirtDataRriReader( ISkCoreApi aCoreApi, Skid aObjId, IStringList aRriIds,
      IGenericChangeListener aChangeListener ) {
    this( aCoreApi, aObjId, defaultRriSectionId, aRriIds, aChangeListener );
  }

  /**
   * Constructor.
   *
   * @param aCoreApi {@link ISkCoreApi} connection API.
   * @param aObjId {@link Skid} ID of the object of the written data.
   * @param aRriSectionId String RRI section ID
   * @param aRriIds {@link IStringList} list of read RRI IDs.
   * @param aChangeListener {@link IGenericChangeListener} input data change listener
   * @throws TsNullArgumentRtException any argument = <b>null</b>.
   * @throws TsIllegalArgumentRtException object of another class.
   */
  public SkVirtDataRriReader( ISkCoreApi aCoreApi, Skid aObjId, String aRriSectionId, IStringList aRriIds,
      IGenericChangeListener aChangeListener ) {
    TsNullArgumentRtException.checkNulls( aCoreApi, aObjId, aChangeListener );
    coreApi = aCoreApi;
    objId = aObjId;
    changeListener = aChangeListener;
    // rri
    if( !coreApi.services().hasKey( ISkRegRefInfoService.SERVICE_ID ) ) {
      coreApi.addService( SkRegRefInfoService.CREATOR );
    }
    ISkRegRefInfoService rriService = coreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    rriSection = rriService.getSection( aRriSectionId );
    GwidList tmpRriIds = new GwidList();
    for( String paramId : aRriIds ) {
      tmpRriIds.add( Gwid.createAttr( aObjId.classId(), aObjId.strid(), paramId ) );
    }
    //
    rriIds = tmpRriIds;
    // register rri change-value listener
    handler = new InternalHandler();
    Gwid eventId = Gwid.createEvent( CLASSID_RRI_SECTION, Gwid.STR_MULTI_ID, EVID_RRI_PARAM_CHANGE );
    coreApi.eventService().registerHandler( new GwidList( eventId ), handler );
  }

  // ------------------------------------------------------------------------------------
  // Public API
  //
  /**
   * Add the specified RRI IDs to the list of readable rri data.
   *
   * @param aRriIds {@link IGwidList} list of read RRI IDs.
   * @throws TsNullArgumentRtException argument = <b>null</b>.
   */
  public final void addRriIds( IGwidList aRriIds ) {
    TsNullArgumentRtException.checkNull( aRriIds );
    ((GwidList)rriIds).addAll( aRriIds );
  }

  /**
   * Returns a list of parameters that have no value.
   *
   * @return {@link IGwidList} list identifiers of unassigned parameters.
   */
  public final IGwidList listUnassigned() {
    GwidList retValue = new GwidList();
    for( Gwid rriId : rriIds ) {
      if( !rriSection.getAttrParamValue( rriId.skid(), rriId.propId() ).isAssigned() ) {
        retValue.add( rriId );
      }
    }
    return retValue;
  }

  /**
   * Read RRI param boolean value.
   *
   * @param aRriParamId String ID of read RRI param.
   * @return boolean value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final boolean getBool( String aRriParamId ) {
    TsNullArgumentRtException.checkNull( aRriParamId );
    return rriSection.getAttrParamValue( new Skid( objId.classId(), objId.strid() ), aRriParamId ).asBool();
  }

  /**
   * Read RRI param boolean value.
   *
   * @param aGwid String ID of read data.
   * @return boolean value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final boolean getBool( Gwid aGwid ) {
    TsNullArgumentRtException.checkNull( aGwid );
    return rriSection.getAttrParamValue( aGwid.skid(), aGwid.propId() ).asBool();
  }

  /**
   * Read RRI param integer value.
   *
   * @param aRriParamId String ID of read RRI param.
   * @return int value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final int getInt( String aRriParamId ) {
    TsNullArgumentRtException.checkNull( aRriParamId );
    return rriSection.getAttrParamValue( new Skid( objId.classId(), objId.strid() ), aRriParamId ).asInt();
  }

  /**
   * Read RRI param integer value.
   *
   * @param aGwid String ID of read data.
   * @return int value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final int getInt( Gwid aGwid ) {
    TsNullArgumentRtException.checkNull( aGwid );
    return rriSection.getAttrParamValue( aGwid.skid(), aGwid.propId() ).asInt();
  }

  /**
   * Read RRI param float value.
   *
   * @param aRriParamId String ID of read RRI param.
   * @return float value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final float getFloat( String aRriParamId ) {
    TsNullArgumentRtException.checkNull( aRriParamId );
    return rriSection.getAttrParamValue( new Skid( objId.classId(), objId.strid() ), aRriParamId ).asFloat();
  }

  /**
   * Read RRI param float value.
   *
   * @param aGwid String ID of read data.
   * @return float value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final float getFloat( Gwid aGwid ) {
    TsNullArgumentRtException.checkNull( aGwid );
    return rriSection.getAttrParamValue( aGwid.skid(), aGwid.propId() ).asFloat();
  }

  /**
   * Read RRI param string value.
   *
   * @param aRriParamId String ID of read RRI param.
   * @return String value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final String getStr( String aRriParamId ) {
    TsNullArgumentRtException.checkNull( aRriParamId );
    return rriSection.getAttrParamValue( new Skid( objId.classId(), objId.strid() ), aRriParamId ).asString();
  }

  /**
   * Read RRI param string value.
   *
   * @param aGwid String ID of read data.
   * @return float value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final String getStr( Gwid aGwid ) {
    TsNullArgumentRtException.checkNull( aGwid );
    return rriSection.getAttrParamValue( aGwid.skid(), aGwid.propId() ).asString();
  }

  /**
   * Read RRI param valobj value.
   *
   * @param aRriParamId String ID of read RRI param.
   * @return String value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final <T> T getValobj( String aRriParamId ) {
    TsNullArgumentRtException.checkNull( aRriParamId );
    return rriSection.getAttrParamValue( new Skid( objId.classId(), objId.strid() ), aRriParamId ).asValobj();
  }

  /**
   * Read RRI param valobj value.
   *
   * @param aGwid String ID of read data.
   * @return float value.
   * @throws TsNullArgumentRtException argument = null.
   * @throws TsItemNotFoundRtException read channel does not exist.
   */
  public final <T> T getValobj( Gwid aGwid ) {
    TsNullArgumentRtException.checkNull( aGwid );
    return rriSection.getAttrParamValue( aGwid.skid(), aGwid.propId() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //
  @Override
  public void close() {
    coreApi.eventService().registerHandler( IGwidList.EMPTY, handler );
  }

  private class InternalHandler
      implements ISkEventHandler {

    @Override
    public void onEvents( ISkEventList aEvents ) {
      for( SkEvent event : aEvents ) {
        Gwid paramId = event.paramValues().getValobj( EVPRMID_PARAM_GWID );
        if( rriIds.hasElem( paramId ) ) {
          changeListener.onGenericChangeEvent( this );
          return;
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //
  /**
   * @return {@link ISkCoreApi}
   */
  protected final ISkCoreApi coreApi() {
    return coreApi;
  }

  /**
   * @return {@link Skid} objId
   */
  protected final Skid objId() {
    return objId;
  }

}
