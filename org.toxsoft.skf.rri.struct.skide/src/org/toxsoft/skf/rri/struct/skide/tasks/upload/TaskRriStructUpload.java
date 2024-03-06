package org.toxsoft.skf.rri.struct.skide.tasks.upload;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skf.rri.struct.skide.ISkidePluginRriStructSharedResources.*;
import static org.toxsoft.skide.plugin.exconn.main.UploadToServerTaskProcessor.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.struct.skide.main.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.exconn.main.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * SkIDE task {@link UploadToServerTaskProcessor} runner for {@link SkideUnitRriStruct}.
 *
 * @author max
 */
public class TaskRriStructUpload
    extends AbstractSkideUnitTaskSync {

  private final RriStructUploadSelectionRules uploadSelectionRules = new RriStructUploadSelectionRules();

  private ISkCoreApi srcCoreApi  = null;
  private ISkCoreApi destCoreApi = null;

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskRriStructUpload( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, UploadToServerTaskProcessor.INSTANCE.taskInfo(), //
        new StridablesList<>( /* No cfg ops */ ) );
  }

  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    // Начинаем процесс экспорта
    lop.startWork( MSG_RRI_STRUCT_UPLOAD, true );
    uploadSelectionRules.loadFromOptions( getCfgOptionValues() );
    ISkConnection srcConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
    srcCoreApi = srcConn.coreApi();
    ISkConnection destConn = REFDEF_IN_OPEN_SK_CONN.getRef( aInput );
    destCoreApi = destConn.coreApi();
    int uploadedObjectsCount = uploadRriStructure();
    lop.finished( ValidationResult.info( FMT_RRI_STRUCT_UPLOADED, Integer.valueOf( uploadedObjectsCount ) ) );
  }

  private int uploadRriStructure() {
    ISkRegRefInfoService sourceRriService = srcCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    ISkRegRefInfoService targetRriService = destCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );

    int paramsCount = 0;

    // TODO 1. проверка наличия целевых классов (проверка иерархии)
    // TODO 2. проверка наличия классов, на которые существуют связи НСИ

    // получаем список секций в источнике
    for( ISkRriSection srcSection : sourceRriService.listSections() ) {
      ISkRriSection targetSection;
      if( targetRriService.findSection( srcSection.id() ) == null ) {
        // создаем секцию в целевом соединении
        targetSection = targetRriService.createSection( srcSection.id(), srcSection.nmName(), srcSection.description(),
            srcSection.params() );
      }
      else {
        targetSection = targetRriService.getSection( srcSection.id() );
        targetSection.setSectionProps( srcSection.nmName(), srcSection.description(), srcSection.params() );
      }

      // копируем в нее содержание исходной
      for( String srcClassId : srcSection.listClassIds() ) {
        // создаем класс в целевой секции
        for( IDtoRriParamInfo paramInfo : srcSection.listParamInfoes( srcClassId ) ) {
          targetSection.defineParam( srcClassId, paramInfo );
          paramsCount++;
        }
      }
    }

    // TODO Auto-generated method stub
    return paramsCount;

  }

}
