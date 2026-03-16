package org.toxsoft.skf.rri.values.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.ved.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.skf.rri.struct.gui.ugwi.*;
import org.toxsoft.skf.rri.values.gui.km5.*;
import org.toxsoft.skf.rri.values.gui.sol.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.gui.ugwi.gui.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The library quant.
 *
 * @author max
 */
public class QuantRegRefInfoValuesGui
    extends AbstractQuant
    implements ISkCoreExternalHandler {

  /**
   * Constructor.
   */
  public QuantRegRefInfoValuesGui() {
    super( QuantRegRefInfoValuesGui.class.getSimpleName() );
    SkfRriUtils.initialize();
    // GUI registration
    KM5Utils.registerContributorCreator( KM5RriValuesContributor.CREATOR );
    KM5Utils.registerContributorCreator( KM5RriStructContributor.CREATOR );
    SkCoreUtils.registerCoreApiHandler( this );
    registerQuant( new QuantTsGuiVed() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    IVedActorFactoriesRegistry actFact = aAppContext.get( IVedActorFactoriesRegistry.class );
    actFact.register( SkActorRtdataText.FACTORY );
    actFact.register( SkActorRriInputField.FACTORY );
    actFact.register( SkActorRriCheckbox.FACTORY );

  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IRegRefInfoConstants.init( aWinContext );

    IValedControlFactoriesRegistry vcfRegistry = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfRegistry.registerFactory( ValedRriIdEditor.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRriIdEditor.FACTORY );

    IRtControlFactoriesRegistry rtcReg = aWinContext.get( IRtControlFactoriesRegistry.class );
    rtcReg.register( RtcRriAttrValueView.FACTORY );
    rtcReg.register( RtcRriInputField.FACTORY );

  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public void processSkCoreInitialization( IDevCoreApi aCoreApi ) {
    ISkUgwiService uServ = aCoreApi.ugwiService();
    ISkUgwiKind uk;
    uk = uServ.listKinds().getByKey( UgwiKindRriAttr.KIND_ID );
    uk.registerHelper( IUgwiKindGuiHelper.class, new UgwiGuiHelperRriAttr( (AbstractSkUgwiKind)uk ) );
    uk = uServ.listKinds().getByKey( UgwiKindRriLink.KIND_ID );
    uk.registerHelper( IUgwiKindGuiHelper.class, new UgwiGuiHelperRriLink( (AbstractSkUgwiKind)uk ) );
    // register abilities
    aCoreApi.userService().abilityManager().defineAbility( IRegRefInfoConstants.ABILITY_ACCESS_RRI_VALUES_EDITOR );
  }

}
