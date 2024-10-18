package org.toxsoft.skf.rri.struct.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;

/**
 * LifeCycleManager for RRI sections
 *
 * @author max
 */
public class RriSectionLifeCycleManager
    extends M5LifecycleManager<ISkRriSection, ISkRegRefInfoService> {

  /**
   * Constructor by model and service (master object)
   *
   * @param aModel IM5Model - RRI sections M5 model
   * @param aMaster ISkRegRefInfoService - RRI service
   */
  public RriSectionLifeCycleManager( IM5Model<ISkRriSection> aModel, ISkRegRefInfoService aMaster ) {
    super( aModel, true, false, true, true, aMaster );
    TsNullArgumentRtException.checkNulls( aModel, aMaster );
  }

  private RriSectionModel m() {
    return (RriSectionModel)model();
  }

  @Override
  protected ISkRriSection doCreate( IM5Bunch<ISkRriSection> aValues ) {
    String id = m().STRID.getFieldValue( aValues ).asString();
    String name = m().NAME.getFieldValue( aValues ).asString();
    String descr = m().DESCR.getFieldValue( aValues ).asString();

    IOptionSetEdit optSet = new OptionSet();
    IAvMetaConstants.DDEF_NAME.setValue( optSet, AvUtils.avStr( name ) );

    return master().createSection( id, name, descr, optSet );
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
