package org.toxsoft.skf.rri.struct.skide.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.struct.skide.ISkidePluginRriStructConstants.*;
import static org.toxsoft.skf.rri.struct.skide.ISkidePluginRriStructSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.ucateg.ISkideUnitCategoryConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skf.rri.struct.skide.tasks.upload.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * SkiDE unit: rri struct editor
 *
 * @author max
 */
public class SkideUnitRriStruct
    extends AbstractSkideUnit {

  /**
   * The plugin ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.rri.struct"; //$NON-NLS-1$

  SkideUnitRriStruct( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_TEMPLATE_UNIT_1, //
        TSID_DESCRIPTION, STR_SKIDE_TEMPLATE_UNIT_1_D, //
        OPDEF_SKIDE_UNIT_CATEGORY, UCATEGID_SYSDESCR, //
        TSID_ICON_ID, ICONID_SKIDE_PLUGIN_RRI //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitRriStructPanel( aContext, this );
  }

  @Override
  protected void doFillTasks( IStringMapEdit<AbstractSkideUnitTask> aTaskRunnersMap ) {
    AbstractSkideUnitTask task = new TaskRriStructUpload( this );
    aTaskRunnersMap.put( task.taskInfo().id(), task );
  }
}
