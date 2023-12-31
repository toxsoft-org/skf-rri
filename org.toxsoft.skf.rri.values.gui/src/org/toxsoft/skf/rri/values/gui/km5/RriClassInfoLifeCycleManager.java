package org.toxsoft.skf.rri.values.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

public class RriClassInfoLifeCycleManager
    extends M5LifecycleManager<ISkClassInfo, ISkCoreApi> {

  private String sectionId;

  private ISkRegRefInfoService rriService;

  public String getSectionId() {
    return sectionId;
  }

  public void setSectionId( String aSectionId ) {
    sectionId = aSectionId;
  }

  public RriClassInfoLifeCycleManager( IM5Model<ISkClassInfo> aModel, ISkCoreApi aMaster ) {
    super( aModel, true, false, true, true, aMaster );
    rriService = (ISkRegRefInfoService)aMaster.services().getByKey( ISkRegRefInfoService.SERVICE_ID );
  }

  @Override
  protected void doRemove( ISkClassInfo aEntity ) {
    if( sectionId == null ) {
      return;
    }
    ISkRriSection section = rriService.findSection( sectionId );
    if( section == null ) {
      return;
    }
    section.removeAll( aEntity.id() );
  }

  @Override
  protected IList<ISkClassInfo> doListEntities() {
    if( sectionId == null ) {
      return IList.EMPTY;
    }
    ISkRriSection section = rriService.findSection( sectionId );
    if( section == null ) {
      return IList.EMPTY;
    }
    IListEdit<ISkClassInfo> result = new ElemArrayList<>();
    for( String classId : section.listClassIds() ) {
      result.add( master().sysdescr().getClassInfo( classId ) );
    }

    return result;// master().sysdescr().listClasses();
  }

}
