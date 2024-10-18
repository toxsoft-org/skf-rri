package org.toxsoft.skf.rri.struct.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * LifeCycleManager for ClassInfoes without RRI params (list for choose)
 *
 * @author max
 */
public class ChoosableClassInfoLifeCycleManager
    extends M5LifecycleManager<ISkClassInfo, ISkCoreApi> {

  private String sectionId;

  private ISkRegRefInfoService rriService;

  /**
   * Returns RRI sectionId (sectionId can be null).
   *
   * @return String - RRI sectionId (sectionId can be null).
   */
  public String getSectionId() {
    return sectionId;
  }

  /**
   * Sets RRI sectionId (sectionId can be null).
   *
   * @param aSectionId String - RRI sectionId (sectionId can be null).
   */
  public void setSectionId( String aSectionId ) {
    sectionId = aSectionId;
  }

  /**
   * Constructor by model and S5 coreApi (master object)
   *
   * @param aModel IM5Model - ClassInfo M5 model
   * @param aMaster ISkCoreApi - S5 coreApi
   */
  public ChoosableClassInfoLifeCycleManager( IM5Model<ISkClassInfo> aModel, ISkCoreApi aMaster ) {
    super( aModel, false, false, false, true, aMaster );
    rriService = (ISkRegRefInfoService)aMaster.services().getByKey( ISkRegRefInfoService.SERVICE_ID );
  }

  @Override
  protected IList<ISkClassInfo> doListEntities() {
    if( sectionId == null ) {
      return IList.EMPTY;
    }
    ISkRriSection section = rriService.findSection( sectionId );

    // // пропустим корневой класс
    // if( cinf.id().equals( IGwHardConstants.GW_ROOT_CLASS_ID ) ) {
    // continue;
    // }
    // // пропустим служебные классы
    // String ownerServiceId = skCim().getClassOwnerService( cinf.id() );
    // if( !ownerServiceId.equals( ISkSysdescr.SERVICE_ID ) ) {
    // continue;
    // }

    IStringList alreadyRri = section.listClassIds();

    IStridablesList<ISkClassInfo> allClasses = master().sysdescr().listClasses();

    IStridablesListEdit<ISkClassInfo> result = new StridablesList<>();

    for( ISkClassInfo cl : allClasses ) {
      // ignore allready RRI
      if( alreadyRri.hasElem( cl.id() ) ) {
        continue;
      }

      // ignore service classes
      if( master().sysdescr().determineClassClaimingServiceId( cl.id() ).equals( ISkRegRefInfoService.SERVICE_ID ) ) {
        continue;
      }

      // ignocer root class
      if( IGwHardConstants.GW_ROOT_CLASS_ID.equals( cl.id() ) ) {
        continue;
      }
      result.add( cl );
    }

    return result;
  }

}
