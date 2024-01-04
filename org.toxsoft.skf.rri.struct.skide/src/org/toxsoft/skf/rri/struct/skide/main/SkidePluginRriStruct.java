package org.toxsoft.skf.rri.struct.skide.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.struct.skide.ISkidePluginRriStructConstants.*;
import static org.toxsoft.skf.rri.struct.skide.ISkidePluginRriStructSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.api.*;

/**
 * SkIDE plugin: Reg ref info editor.
 *
 * @author max
 */
public class SkidePluginRriStruct
    extends AbstractSkidePlugin {

  /**
   * The plugin ID.
   */
  public static final String SKIDE_PLUGIN_ID = SKIDE_FULL_ID + ".plugin.rri.struct"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final AbstractSkidePlugin INSTANCE = new SkidePluginRriStruct();

  SkidePluginRriStruct() {
    super( SKIDE_PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_PLUGIN_TEMPLATE, //
        TSID_DESCRIPTION, STR_SKIDE_PLUGIN_TEMPLATE_D, //
        TSID_ICON_ID, ICONID_SKIDE_PLUGIN_RRI //
    ) );
  }

  @Override
  protected void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList ) {
    aUnitsList.add( new SkideUnitRriStruct( aContext, this ) );
    aUnitsList.add( new SkideUnitRriValues( aContext, this ) );
  }

}
