package org.toxsoft.skf.rri.struct.skide.tasks.upload;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skf.rri.struct.skide.ISkidePluginRriStructSharedResources.*;
import static org.toxsoft.skide.plugin.exconn.main.UploadToServerTaskProcessor.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.legacy.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.skf.rri.struct.skide.main.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.exconn.main.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * SkIDE task {@link UploadToServerTaskProcessor} runner for {@link SkideUnitRriValues}.
 *
 * @author max
 */
public class TaskRriValuesUpload
    extends AbstractSkideUnitTaskSync {

  private final RriValuesUploadSelectionRules uploadSelectionRules = new RriValuesUploadSelectionRules();

  private ISkCoreApi srcCoreApi  = null;
  private ISkCoreApi destCoreApi = null;

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskRriValuesUpload( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, UploadToServerTaskProcessor.INSTANCE.taskInfo(), //
        new StridablesList<>( /* No cfg ops */ ) );
  }

  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    // Начинаем процесс экспорта
    lop.startWork( MSG_RRI_VALUES_UPLOAD, true );
    uploadSelectionRules.loadFromOptions( getCfgOptionValues() );
    ISkConnection srcConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
    srcCoreApi = srcConn.coreApi();
    ISkConnection destConn = REFDEF_IN_OPEN_SK_CONN.getRef( aInput );
    destCoreApi = destConn.coreApi();
    int uploadedObjectsCount = uploadRriValues();
    lop.finished( ValidationResult.info( FMT_RRI_VALUES_UPLOADED, Integer.valueOf( uploadedObjectsCount ) ) );
  }

  private int uploadRriValues() {
    ISkRegRefInfoService sourceRriService = srcCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    ISkRegRefInfoService targetRriService = destCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );

    int paramsCount = 0;

    // TODO 1. проверка наличия экспортируемых секций
    // TODO 2. проверка наличия целевых классов (проверка иерархии)
    // TODO 3. проверка наличия классов, на которые существуют связи НСИ
    // TODO 4. проверка наличия соответствующих параметров НСИ в целевых классах
    // TODO 5. проверка наличия соответствующих целевых объектов
    // TODO 6. проверка наличия объектов, на которые существуют значения связей НСИ

    // получаем список секций в источнике
    for( ISkRriSection srcSection : sourceRriService.listSections() ) {
      ISkRriSection targetSection = targetRriService.getSection( srcSection.id() );

      IStringList rriSectionClassIds = srcSection.listClassIds();

      for( String classId : rriSectionClassIds ) {
        ISkRriParamValues srcAllObjectsClassRriValues = getParamValuesByClassId( classId, srcSection );
        targetSection.setParamValues( srcAllObjectsClassRriValues, STR_REASON_IMPORT_FROM_SKIDE );
        paramsCount++;
      }
    }

    // TODO Auto-generated method stub
    return paramsCount;

  }

  private ISkRriParamValues getParamValuesByClassId( String aClassId, ISkRriSection aSection ) {
    ISkidList aObjIds = srcCoreApi.objService().listSkids( aClassId, false );

    TsNullArgumentRtException.checkNull( aObjIds );
    IMapEdit<Skop, IAtomicValue> attrsMap = new ElemMap<>();
    IMapEdit<Skop, ISkidList> linksMap = new ElemMap<>();

    IStridablesList<IDtoRriParamInfo> paramInfoes = aSection.listParamInfoes( aClassId );
    // цилк по каждому объекту из запрошенного списка для формирования незультата
    for( Skid origSkid : aObjIds ) {

      for( IDtoRriParamInfo paramInfo : paramInfoes ) {
        Skop skop = new Skop( origSkid, paramInfo.id() );
        if( paramInfo.isLink() ) {
          try {
            ISkidList linkVal = aSection.getLinkParamValue( origSkid, paramInfo.id() );
            if( linkVal != null && linkVal.size() > 0 ) {
              linksMap.put( skop, linkVal );
            }
          }
          catch( Exception e ) {
            System.out.println( e.getMessage() );
          }
        }
        else {
          try {
            IAtomicValue attrVal = aSection.getAttrParamValue( origSkid, paramInfo.id() );
            if( attrVal != null && attrVal.isAssigned() ) {
              attrsMap.put( skop, attrVal );
            }
          }
          catch( Exception e ) {
            System.out.println( e.getMessage() );
          }
        }
      }
    }

    return new SkRriParamValues( attrsMap, linksMap );
  }

}
