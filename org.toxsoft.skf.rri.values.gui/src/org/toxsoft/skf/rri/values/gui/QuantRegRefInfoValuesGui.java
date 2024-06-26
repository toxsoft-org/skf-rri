package org.toxsoft.skf.rri.values.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.skf.rri.values.gui.km5.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * The library quant.
 *
 * @author max
 */
public class QuantRegRefInfoValuesGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantRegRefInfoValuesGui() {
    super( QuantRegRefInfoValuesGui.class.getSimpleName() );
    SkfRriUtils.initialize();
    // GUI registration
    KM5Utils.registerContributorCreator( KM5RriValuesContributor.CREATOR );
    KM5Utils.registerContributorCreator( KM5RriStructContributor.CREATOR );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // IVetrolSystoolsGuiConstants.init( aWinContext );
  }
}
