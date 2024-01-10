package org.toxsoft.skf.rri.values.mws.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.rri.values.gui.*;
import org.toxsoft.skf.rri.values.mws.*;
import org.toxsoft.skf.rri.values.mws.Activator;
import org.toxsoft.skf.rri.values.mws.e4.services.*;

/**
 * The plugin addon.
 *
 * @author max
 */
public class AddonWsRriValues
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonWsRriValues() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantRegRefInfoValuesGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IWsRriValuesConstants.init( aWinContext );
    //
    IWsRriSectionsManagementService rrims = new WsRriSectionsManagementService( new TsGuiContext( aWinContext ) );
    aWinContext.set( IWsRriSectionsManagementService.class, rrims );
  }

}
