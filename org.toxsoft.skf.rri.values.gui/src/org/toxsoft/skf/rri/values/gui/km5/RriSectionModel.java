package org.toxsoft.skf.rri.values.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;

/**
 * M5 model realization for {@link ISkRriSection} entities.
 *
 * @author max
 */
public class RriSectionModel
    extends M5Model<ISkRriSection> {

  /**
   * Model ID.
   */
  public static final String MODEL_ID = "org.toxsoft.skf.rri.values.gui.km5"; //$NON-NLS-1$

  /**
   * Attribute {@link ISkRriSection#id() } string id
   */
  static M5StdFieldDefId<ISkRriSection> STRID = new M5StdFieldDefId<>();

  /**
   * Attribute {@link ISkRriSection#nmName() } name
   */
  static M5StdFieldDefName<ISkRriSection> NAME = new M5StdFieldDefName<>();

  /**
   * Attribute {@link ISkRriSection#description() } description
   */
  static M5StdFieldDefDescription<ISkRriSection> DESCR = new M5StdFieldDefDescription<>();

  /**
   * Constructor.
   */
  public RriSectionModel() {
    super( MODEL_ID, ISkRriSection.class );

    addFieldDefs( STRID, NAME, DESCR );
  }

  @Override
  protected IM5LifecycleManager<ISkRriSection> doCreateDefaultLifecycleManager() {
    ISkRegRefInfoService ccs = tsContext().get( ISkRegRefInfoService.class );
    TsInternalErrorRtException.checkNull( ccs );
    return new RriSectionLifeCycleManager( this, ccs );
  }

  @Override
  protected IM5LifecycleManager<ISkRriSection> doCreateLifecycleManager( Object aMaster ) {
    return new RriSectionLifeCycleManager( this, ISkRegRefInfoService.class.cast( aMaster ) );
  }

  private static class RriSectionLifeCycleManager
      extends M5LifecycleManager<ISkRriSection, ISkRegRefInfoService> {

    public RriSectionLifeCycleManager( IM5Model<ISkRriSection> aModel, ISkRegRefInfoService aMaster ) {
      super( aModel, false, false, false, true, aMaster );
    }

    @Override
    protected IList<ISkRriSection> doListEntities() {
      return master().listSections();
    }

  }
}
