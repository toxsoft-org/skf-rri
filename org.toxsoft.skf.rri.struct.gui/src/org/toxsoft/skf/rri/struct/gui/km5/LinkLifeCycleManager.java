package org.toxsoft.skf.rri.struct.gui.km5;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.struct.gui.km5.LinkModel.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Менеджер ЖЦ описания связей НСИ
 *
 * @author max
 */
public class LinkLifeCycleManager
    extends M5LifecycleManager<IDtoLinkInfo, ISkRegRefInfoService> {

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
  public LinkLifeCycleManager( ITsGuiContext aContext, IM5Model<IDtoLinkInfo> aModel, ISkRegRefInfoService aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNulls( aContext, aModel, aMaster );
  }

  @Override
  protected IDtoLinkInfo doCreate( IM5Bunch<IDtoLinkInfo> aValues ) {
    IDtoLinkInfo dpuSdLinkInfo = makeLinkInfoDpu( aValues );
    IDtoRriParamInfo dtoRriParamInfo = new DtoRriParamInfo( dpuSdLinkInfo );
    master().getSection( sectionId ).defineParam( classId, dtoRriParamInfo );
    return dpuSdLinkInfo;
  }

  @Override
  protected IDtoLinkInfo doEdit( IM5Bunch<IDtoLinkInfo> aValues ) {
    IDtoLinkInfo dpuSdLinkInfo = makeLinkInfoDpu( aValues );
    IDtoRriParamInfo dtoRriParamInfo = new DtoRriParamInfo( dpuSdLinkInfo );
    master().getSection( sectionId ).defineParam( classId, dtoRriParamInfo );
    return dpuSdLinkInfo;
  }

  @SuppressWarnings( "unchecked" )
  private static IDtoLinkInfo makeLinkInfoDpu( IM5Bunch<IDtoLinkInfo> aValues ) {
    String id = aValues.getAsAv( FID_LINK_ID ).asString();
    String name = aValues.getAsAv( FID_LINK_NAME ).asString();
    String description = aValues.getAsAv( FID_LINK_DESCR ).asString();
    int maxCount = aValues.getAsAv( FID_MAX_COUNT ).asInt();
    boolean isExactCount = aValues.getAsAv( FID_IS_EXACT_COUNT ).asBool();
    IList<ISkClassInfo> rightClasses = (IList<ISkClassInfo>)aValues.get( FID_RIGHT_CLASS_IDS );
    IStringListEdit rightClassIds = new StringArrayList();
    if( !rightClasses.isEmpty() ) {
      for( ISkClassInfo cinf : rightClasses ) {
        rightClassIds.add( cinf.id() );
      }
    }
    IDtoLinkInfo linf = DtoLinkInfo.create2( id, //
        rightClassIds, //
        new CollConstraint( maxCount, isExactCount, false, false ), //
        TSID_NAME, name, //
        TSID_DESCRIPTION, description //
    );

    return linf;
  }

  @Override
  protected void doRemove( IDtoLinkInfo aEntity ) {
    master().getSection( sectionId ).removeParam( classId, aEntity.id() );
  }

  @Override
  protected IList<IDtoLinkInfo> doListEntities() {
    if( classId == null || classId.equals( TsLibUtils.EMPTY_STRING ) ) {
      return IList.EMPTY;
    }
    IStridablesList<IDtoRriParamInfo> allParamInfoes = master().getSection( sectionId ).listParamInfoes( classId );

    ElemArrayList<IDtoLinkInfo> result = new ElemArrayList<>();

    for( IDtoRriParamInfo param : allParamInfoes ) {
      if( param.isLink() ) {
        result.add( param.linkInfo() );
      }
    }

    return result;
  }

}
