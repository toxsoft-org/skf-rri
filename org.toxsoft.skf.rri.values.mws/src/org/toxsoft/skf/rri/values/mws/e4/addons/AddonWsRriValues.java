package org.toxsoft.skf.rri.values.mws.e4.addons;

import static org.toxsoft.skf.rri.values.gui.IRegRefInfoConstants.*;
import static org.toxsoft.skf.rri.values.mws.IWsRriValuesConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.rri.values.gui.*;
import org.toxsoft.skf.rri.values.mws.*;
import org.toxsoft.skf.rri.values.mws.Activator;
import org.toxsoft.skf.rri.values.mws.e4.services.*;
import org.toxsoft.uskat.core.gui.utils.*;
import org.toxsoft.uskat.core.impl.*;

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

    IWsRriSectionsManagementService rrims = new WsRriSectionsManagementService( new TsGuiContext( aWinContext ) );
    aWinContext.set( IWsRriSectionsManagementService.class, rrims );

    // implement access rights
    // new version goga
    GuiE4ElementsToAbilitiesBinder binder = new GuiE4ElementsToAbilitiesBinder( new TsGuiContext( aWinContext ) );
    binder.bindPerspective( ABILITYID_RRI_PERSP_VALUES_EDITOR, E4_VISUAL_ELEM_ID_PERSP_RRI_VALUES );
    binder.bindMenuElement( ABILITYID_RRI_PERSP_VALUES_EDITOR, E4_VISUAL_ELEM_ID_MENU_ITEEM_RRI_VALUES );
    binder.bindToolItem( ABILITYID_RRI_PERSP_VALUES_EDITOR, E4_VISUAL_ELEM_ID_TOOL_ITEEM_RRI_VALUES );
    SkCoreUtils.registerCoreApiHandler( binder );

    // old version dima
    // ISkConnectionSupplier connSupplier = aWinContext.get( ISkConnectionSupplier.class );
    // ISkAbility canAccess = connSupplier.defConn().coreApi().userService().abilityManager()
    // .findAbility( IRegRefInfoConstants.ABILITYID_RRI_PERSP_VALUES_EDITOR );
    // if( !connSupplier.defConn().coreApi().userService().abilityManager().isAbilityAllowed( canAccess.id() ) ) {
    // ITsE4Helper e4Helper = aWinContext.get( ITsE4Helper.class );
    // // hide perspective, menu and toolbar
    // e4Helper.setPrerspectiveVisible( IWsRriValuesConstants.VISEL_ID_PERSP_RRI_VALUES, false );
    // e4Helper.setMenuItemVisible( IWsRriValuesConstants.VISEL_ID_MENU_ITEEM_RRI_VALUES, false );
    // e4Helper.setToolItemVisible( IWsRriValuesConstants.VISEL_ID_TOOL_ITEEM_RRI_VALUES, false );
    // }

  }

}
