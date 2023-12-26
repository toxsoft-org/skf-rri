package org.toxsoft.skf.rri.struct.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.skf.rri.lib.*;

/**
 * @author max
 */
public class RriSectionLifeCycleManager
    extends M5LifecycleManager<ISkRriSection, ISkRegRefInfoService> {

  public RriSectionLifeCycleManager( IM5Model<ISkRriSection> aModel, ISkRegRefInfoService aMaster ) {
    super( aModel, true, false, true, true, aMaster );
    // TODO Auto-generated constructor stub
  }

  @Override
  protected ISkRriSection doCreate( IM5Bunch<ISkRriSection> aValues ) {
    String id = RriSectionModel.STRID.getFieldValue( aValues ).asString();
    String name = RriSectionModel.NAME.getFieldValue( aValues ).asString();
    String descr = RriSectionModel.DESCR.getFieldValue( aValues ).asString();
    return master().createSection( id, name, descr, IOptionSet.NULL );
  }

  @Override
  protected void doRemove( ISkRriSection aEntity ) {
    master().removeSection( aEntity.id() );
  }

  @Override
  protected IList<ISkRriSection> doListEntities() {
    return master().listSections();
  }

}
