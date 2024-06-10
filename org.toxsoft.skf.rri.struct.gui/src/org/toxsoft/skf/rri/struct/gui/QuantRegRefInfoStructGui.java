package org.toxsoft.skf.rri.struct.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.skf.rri.struct.gui.ugwi.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.gui.ugwi.gui.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The library quant.
 * <p>
 * Implements {@link ISkCoreExternalHandler#processSkCoreInitialization(IDevCoreApi)} for Sk-connection related stuff
 * registration.
 *
 * @author max
 */
public class QuantRegRefInfoStructGui
    extends AbstractQuant
    implements ISkCoreExternalHandler {

  /**
   * Constructor.
   */
  public QuantRegRefInfoStructGui() {
    super( QuantRegRefInfoStructGui.class.getSimpleName() );
    SkfRriUtils.initialize();
    // GUI registration
    KM5Utils.registerContributorCreator( KM5RriStructContributor.CREATOR );
    SkCoreUtils.registerCoreApiHandler( this );
  }

  // ------------------------------------------------------------------------------------
  // ISkCoreExternalHandler
  //

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public void processSkCoreInitialization( IDevCoreApi aCoreApi ) {
    ISkUgwiService uServ = aCoreApi.ugwiService();
    ISkUgwiKind uk;
    uk = uServ.listKinds().getByKey( UgwiKindRriAttr.KIND_ID );
    uk.registerHelper( IUgwiKindGuiHelper.class, new UgwiGuiHelperRriAttr( (AbstractSkUgwiKind)uk ) );
    uk = uServ.listKinds().getByKey( UgwiKindRriLink.KIND_ID );
    uk.registerHelper( IUgwiKindGuiHelper.class, new UgwiGuiHelperRriLink( (AbstractSkUgwiKind)uk ) );

  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
