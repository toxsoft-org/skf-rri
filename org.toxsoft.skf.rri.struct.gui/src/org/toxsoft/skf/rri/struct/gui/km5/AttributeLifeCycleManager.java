package org.toxsoft.skf.rri.struct.gui.km5;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Менеджер ЖЦ описания атрибутов НСИ
 *
 * @author max
 */
public class AttributeLifeCycleManager
    extends M5LifecycleManager<IDtoAttrInfo, ISkRegRefInfoService> {

  private String classId;

  private String sectionId;

  /**
   * Returns RRI sectionId (sectionId can be null).
   *
   * @return String - RRI sectionId (sectionId can be null).
   */
  public String getSectionId() {
    return sectionId;
  }

  /**
   * Sets RRI sectionId (sectionId can be null).
   *
   * @param aSectionId String - RRI sectionId (sectionId can be null).
   */
  public void setSectionId( String aSectionId ) {
    sectionId = aSectionId;
  }

  /**
   * Returns classId (classId can be null).
   *
   * @return String - classId (classId can be null).
   */
  public String getClassId() {
    return classId;
  }

  /**
   * Sets classId (classId can be null).
   *
   * @param aClassId String - classId (classId can be null).
   */
  public void setClassId( String aClassId ) {
    classId = aClassId;
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст.
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aMaster {@link ISkRegRefInfoService} - мастер-объект
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public AttributeLifeCycleManager( ITsGuiContext aContext, IM5Model<IDtoAttrInfo> aModel,
      ISkRegRefInfoService aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNulls( aContext, aModel, aMaster );
  }

  @Override
  protected IDtoAttrInfo doCreate( IM5Bunch<IDtoAttrInfo> aValues ) {
    IAtomicValue id = aValues.getAsAv( AttributeModel.FID_ATTR_ID );
    IDataType type = aValues.get( AttributeModel.FID_ATTR_TYPE );
    IAtomicValue name = aValues.getAsAv( AttributeModel.FID_ATTR_NAME );
    IAtomicValue descr = aValues.getAsAv( AttributeModel.FID_ATTR_DESCR );

    IDtoAttrInfo dpuSdAttrInfo =
        DtoAttrInfo.create2( id.asString(), type, TSID_NAME, name.asString(), TSID_DESCRIPTION, descr.asString() );

    IDtoRriParamInfo dtoRriParamInfo = new DtoRriParamInfo( dpuSdAttrInfo );
    master().getSection( sectionId ).defineParam( classId, dtoRriParamInfo );
    return dpuSdAttrInfo;
  }

  @Override
  protected IDtoAttrInfo doEdit( IM5Bunch<IDtoAttrInfo> aValues ) {
    IAtomicValue id = aValues.getAsAv( AttributeModel.FID_ATTR_ID );
    IDataType type = aValues.get( AttributeModel.FID_ATTR_TYPE );
    IAtomicValue name = aValues.getAsAv( AttributeModel.FID_ATTR_NAME );
    IAtomicValue descr = aValues.getAsAv( AttributeModel.FID_ATTR_DESCR );

    IDtoAttrInfo dpuSdAttrInfo =
        DtoAttrInfo.create2( id.asString(), type, TSID_NAME, name.asString(), TSID_DESCRIPTION, descr.asString() );

    IDtoRriParamInfo dtoRriParamInfo = new DtoRriParamInfo( dpuSdAttrInfo );
    master().getSection( sectionId ).defineParam( classId, dtoRriParamInfo );
    return dpuSdAttrInfo;
  }

  @Override
  protected void doRemove( IDtoAttrInfo aEntity ) {
    master().getSection( sectionId ).removeParam( classId, aEntity.id() );
  }

  @Override
  protected IList<IDtoAttrInfo> doListEntities() {
    if( classId == null || classId.equals( TsLibUtils.EMPTY_STRING ) ) {
      return IList.EMPTY;
    }
    IStridablesList<IDtoRriParamInfo> allParamInfoes = master().getSection( sectionId ).listParamInfoes( classId );

    ElemArrayList<IDtoAttrInfo> result = new ElemArrayList<>();

    for( IDtoRriParamInfo param : allParamInfoes ) {
      if( !param.isLink() ) {
        result.add( param.attrInfo() );
      }
    }

    return result;
  }

}
