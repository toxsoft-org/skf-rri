package org.toxsoft.skf.rri.struct.skide.tasks.upload;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skf.rri.struct.skide.ISkidePluginRriStructSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.plugin.exconn.main.UploadToServerTaskProcessor.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
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

  static final String OPID_RRI_VALUES_DOWNLOAD = SKIDE_FULL_ID + ".RriValuesDownload"; //$NON-NLS-1$

  static final IDataDef OPDEF_RRI_VALUES_DOWNLOAD = DataDef.create( OPID_RRI_VALUES_DOWNLOAD, BOOLEAN, //
      TSID_NAME, STR_RRI_VALUES_DOWNLOAD, //
      TSID_DESCRIPTION, STR_RRI_VALUES_DOWNLOAD_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  private ISkCoreApi srcCoreApi  = null;
  private ISkCoreApi destCoreApi = null;

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskRriValuesUpload( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, UploadToServerTaskProcessor.INSTANCE.taskInfo(), // configuration options
        new StridablesList<>( //
            OPDEF_RRI_VALUES_DOWNLOAD //
        ) );
  }

  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    String lopMessage = MSG_RRI_VALUES_UPLOAD;
    uploadSelectionRules.loadFromOptions( getCfgOptionValues() );
    ISkConnection srcConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
    srcCoreApi = srcConn.coreApi();
    ISkConnection destConn = REFDEF_IN_OPEN_SK_CONN.getRef( aInput );
    destCoreApi = destConn.coreApi();
    boolean isDownload = OPDEF_RRI_VALUES_DOWNLOAD.getValue( getCfgOptionValues() ).asBool();
    if( isDownload ) {
      lopMessage = MSG_RRI_VALUES_DOWNLOAD;
      // reverse direction
      ISkCoreApi tmp = destCoreApi;
      destCoreApi = srcCoreApi;
      srcCoreApi = tmp;
    }
    // Начинаем процесс переноса
    lop.startWork( lopMessage, true );
    int transferedClassessCount = transferRriValues();
    lop.finished( ValidationResult.info( isDownload ? FMT_RRI_VALUES_DOWNLOADED : FMT_RRI_VALUES_UPLOADED,
        Integer.valueOf( transferedClassessCount ) ) );
  }

  private int transferRriValues() {
    ISkRegRefInfoService sourceRriService = srcCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    ISkRegRefInfoService targetRriService = destCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );

    int classesCount = 0;

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
        classesCount++;
      }
    }

    return classesCount;

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
