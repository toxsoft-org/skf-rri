package org.toxsoft.skf.rri.struct.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * The library quant.
 *
 * @author max
 */
public class QuantRegRefInfoStructGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantRegRefInfoStructGui() {
    super( QuantRegRefInfoStructGui.class.getSimpleName() );
    // KM5Utils.registerContributorCreator( KM5VetrolSystoolsContributor.CREATOR );

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
