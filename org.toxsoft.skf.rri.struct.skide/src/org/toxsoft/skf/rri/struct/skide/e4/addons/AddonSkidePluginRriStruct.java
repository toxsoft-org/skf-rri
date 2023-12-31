package org.toxsoft.skf.rri.struct.skide.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.rri.struct.skide.*;
import org.toxsoft.skf.rri.struct.skide.main.*;
import org.toxsoft.skide.core.api.*;

/**
 * Plugin addon.
 *
 * @author max
 */
public class AddonSkidePluginRriStruct
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkidePluginRriStruct() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skEnv = aAppContext.get( ISkideEnvironment.class );
    skEnv.pluginsRegistrator().registerPlugin( SkidePluginRriStruct.INSTANCE );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkidePluginRriStructConstants.init( aWinContext );
    //
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new org.toxsoft.skf.rri.struct.gui.QuantRegRefInfoStructGui() );
    aQuantRegistrator.registerQuant( new org.toxsoft.skf.rri.values.gui.QuantRegRefInfoValuesGui() );
  }
}
