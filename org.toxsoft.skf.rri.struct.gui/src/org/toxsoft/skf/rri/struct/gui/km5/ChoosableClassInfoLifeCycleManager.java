package org.toxsoft.skf.rri.struct.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

public class ChoosableClassInfoLifeCycleManager
    extends M5LifecycleManager<ISkClassInfo, ISkCoreApi> {

  private String sectionId;

  public String getSectionId() {
    return sectionId;
  }

  public void setSectionId( String aSectionId ) {
    sectionId = aSectionId;
  }

  public ChoosableClassInfoLifeCycleManager( IM5Model<ISkClassInfo> aModel, ISkCoreApi aMaster ) {
    super( aModel, false, false, false, true, aMaster );
    // TODO Auto-generated constructor stub
  }

  @Override
  protected IList<ISkClassInfo> doListEntities() {
    if( sectionId == null ) {
      return IList.EMPTY;
    }
    return master().sysdescr().listClasses();
  }

}
