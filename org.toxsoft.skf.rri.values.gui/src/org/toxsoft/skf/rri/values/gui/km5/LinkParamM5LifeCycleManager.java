package org.toxsoft.skf.rri.values.gui.km5;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.values.gui.utils.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Менеджер ЖЦ связей НСИ
 *
 * @author max
 */
public class LinkParamM5LifeCycleManager
    extends M5LifecycleManager<LinkParam, ISkRegRefInfoService> {

  private ISkClassInfo classInfo;

  private IList<ISkObject> objects = new ElemArrayList<>();

  private ITsGuiContext context;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст.
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aMaster {@link ISkRegRefInfoService} - мастер-объект
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public LinkParamM5LifeCycleManager( ITsGuiContext aContext, IM5Model<LinkParam> aModel,
      ISkRegRefInfoService aMaster ) {
    super( aModel, false, RriUtils.canEdit( aContext ), false, true, aMaster );
    TsNullArgumentRtException.checkNulls( aContext, aMaster );
    context = aContext;
  }

  /**
   * Возвращает класс, связи которого редактируются.
   *
   * @return ISkClassInfo - класс, связи которого редактируются (может быть null).
   */
  public ISkClassInfo getClassInfo() {
    return classInfo;
  }

  /**
   * Устанавливает класс, связи которого следует редактировать.
   *
   * @param aClassInfo ISkClassInfo - класс, может быть null.
   */
  public void setClassInfo( ISkClassInfo aClassInfo ) {
    if( aClassInfo == null || !aClassInfo.equals( classInfo ) ) {
      objects = new ElemArrayList<>();
    }
    classInfo = aClassInfo;
  }

  /**
   * Возвращает объекты, связи которых редактируются.
   *
   * @return IList - список редактируемых объектов.
   */
  public IList<ISkObject> getObjects() {
    return objects;
  }

  /**
   * Устанавливает объекты, связи которых будут редактироваться.
   *
   * @param aObjects IList - список редактируемых объектов.
   */
  public void setObjects( IList<ISkObject> aObjects ) {
    objects = aObjects;
  }

  @Override
  protected LinkParam doEdit( IM5Bunch<LinkParam> aValues ) {
    String setionId = RriUtils.getRriSectionId( context );
    ISkRriSection rriSection = master().findSection( setionId );
    if( rriSection == null ) {
      return null;
    }

    LinkParam orig = aValues.originalEntity();

    ITsCollection<ISkObject> objs = ((LinkParamM5Model)aValues.model()).getMultiLinkValue( aValues );

    SkidList links = new SkidList();
    for( ISkObject obj : objs ) {
      if( !obj.skid().equals( Skid.NONE ) ) {
        links.add( new Skid( obj.classId(), obj.strid() ) );
      }
    }

    LinkParam newParam = new LinkParam( orig.getName(), links, false );

    IAtomicValue reason = aValues.getAsAv( LinkParamM5Model.FID_REASON );
    System.out.println( reason.asString() );
    try {
      for( ISkObject obj : objects ) {
        rriSection.setLinkParamValue( new Skid( obj.classId(), obj.strid() ), orig.getName().id(), links,
            reason.asString() );
      }
    }
    catch( Exception e ) {
      TsDialogUtils.error( context.get( Shell.class ), e );
    }

    // dima 27.07.22 TODO выставим флаг "данные изменились"
    // MPart part = context.appContext().get( MPart.class );
    // if( part != null ) {
    // part.setDirty( true );
    // }

    return newParam;
  }

  @Override
  protected IList<LinkParam> doListEntities() {
    if( classInfo == null ) {
      return new ElemArrayList<>();
    }
    String setionId = RriUtils.getRriSectionId( context );
    ISkRriSection rriSection = master().findSection( setionId );
    if( rriSection == null ) {
      return new ElemArrayList<>();
    }
    IStridablesList<ISkRriParamInfo> paramInfoes = rriSection.listParamInfoes( classInfo.id() );

    IListEdit<LinkParam> params = new ElemArrayList<>();

    for( ISkRriParamInfo pInfo : paramInfoes ) {
      if( pInfo.isLink() ) {
        params.add( formRriAttrParam( pInfo.linkInfo() ) );
      }
    }

    return params;
  }

  protected ITsCollection<LinkParam> doListEntitiesTest() {
    if( classInfo == null ) {
      return new ElemArrayList<>();
    }

    IListEdit<LinkParam> params = new ElemArrayList<>();

    for( IDtoLinkInfo pInfo : classInfo.links().list() ) {

      params.add( formRriAttrParam( pInfo ) );

    }

    return params;
  }

  private LinkParam formRriAttrParam( IDtoLinkInfo aLinkInfo ) {
    if( objects.size() == 0 ) {
      return new LinkParam( aLinkInfo, new SkidList(), false );
    }
    String setionId = RriUtils.getRriSectionId( context );
    ISkRriSection rriSection = master().findSection( setionId );
    if( rriSection == null ) {
      return new LinkParam( aLinkInfo, new SkidList(), false );
    }
    ISkidList prev = null;
    boolean different = false;
    for( ISkObject obj : objects ) {
      ISkidList objVal = rriSection.getLinkParamValue( new Skid( obj.classId(), obj.strid() ), aLinkInfo.id() );
      if( prev != null ) {
        if( !equalsSkidLists( prev, objVal ) ) {
          different = true;
          prev = new SkidList();
          break;
        }
      }
      prev = objVal;
    }
    return new LinkParam( aLinkInfo, prev, different );
  }

  private static boolean equalsSkidLists( ISkidList aList1, ISkidList aList2 ) {
    if( aList1.size() != aList2.size() ) {
      return false;
    }

    int hashCode1 = 0;
    int hashCode2 = 0;

    for( int i = 0; i < aList1.size(); i++ ) {
      hashCode1 += aList1.get( i ).hashCode();
      hashCode2 += aList2.get( i ).hashCode();
    }

    return hashCode1 == hashCode2;
  }

}
