package org.toxsoft.skf.rri.values.gui.km5;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.legacy.*;
//import org.toxsoft.skf.legacy.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.skf.rri.values.gui.utils.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Менеджер ЖЦ атрибутов НСИ
 *
 * @author max
 */
public class AttrParamM5LifeCycleManager
    extends M5LifecycleManager<AttrParam, ISkRegRefInfoService> {

  private ISkClassInfo classInfo;

  private IList<ISkObject> objects = new ElemArrayList<>();

  private ITsGuiContext context;

  /**
   * Конструктор.
   *
   * @param aContext {@link IEclipseContext} - контекст.
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aMaster {@link ISkRegRefInfoService} - мастер-объект
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public AttrParamM5LifeCycleManager( ITsGuiContext aContext, IM5Model<AttrParam> aModel,
      ISkRegRefInfoService aMaster ) {
    super( aModel, false, RriUtils.canEdit( aContext ), false, true, aMaster );
    TsNullArgumentRtException.checkNulls( aContext, aMaster );
    context = aContext;
  }

  /**
   * Возвращает класс, атрибуты которого редактируются.
   *
   * @return ISkClassInfo - класс, атрибуты которого редактируются (может быть null).
   */
  public ISkClassInfo getClassInfo() {
    return classInfo;
  }

  /**
   * Устанавливает класс, атрибуты которого следует редактировать.
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
   * Возвращает объекты, атрибуты которых редактируются.
   *
   * @return IList - список редактируемых объектов.
   */
  public IList<ISkObject> getObjects() {
    return objects;
  }

  /**
   * Устанавливает объекты, атрибуты которых будут редактироваться.
   *
   * @param aObjects IList - список редактируемых объектов.
   */
  public void setObjects( IList<ISkObject> aObjects ) {
    objects = aObjects;
  }

  @Override
  protected AttrParam doEdit( IM5Bunch<AttrParam> aValues ) {
    String setionId = RriUtils.getRriSectionId( context );
    ISkRriSection rriSection = master().findSection( setionId );
    if( rriSection == null ) {
      return null;
    }

    IAtomicValue value = aValues.getAsAv( AttrParamM5Model.FID_VALUE );
    IAtomicValue reason = aValues.getAsAv( AttrParamM5Model.FID_REASON );

    AttrParam orig = aValues.originalEntity();

    AttrParam newParam = new AttrParam( orig.getAttrInfo(), value, false );

    IMapEdit<Skop, IAtomicValue> attrs = new ElemMap<>();

    for( ISkObject obj : objects ) {
      attrs.put( new Skop( new Skid( obj.classId(), obj.strid() ), orig.getAttrInfo().id() ), value );
    }

    SkRriParamValues values = new SkRriParamValues( attrs, new ElemMap<>() );
    rriSection.setParamValues( values, reason.asString() );

    // dima 27.07.22 TODO выставим флаг "данные изменились"
    // MPart part = context.appContext().get( MPart.class );
    // if( part != null ) {
    // part.setDirty( true );
    // }

    return newParam;
  }

  @Override
  protected IList<AttrParam> doListEntities() {
    if( classInfo == null ) {
      return new ElemArrayList<>();
    }

    String setionId = RriUtils.getRriSectionId( context );
    ISkRriSection rriSection = master().findSection( setionId );
    if( rriSection == null ) {
      return new ElemArrayList<>();
    }

    IStridablesList<ISkRriParamInfo> paramInfoes = rriSection.listParamInfoes( classInfo.id() );

    IListEdit<AttrParam> params = new ElemArrayList<>();

    for( ISkRriParamInfo pInfo : paramInfoes ) {
      if( !pInfo.isLink() ) {
        params.add( formRriAttrParam( pInfo.attrInfo() ) );
      }
    }

    return params;
  }

  protected ITsCollection<AttrParam> doListEntitiesTest() {
    if( classInfo == null ) {
      return new ElemArrayList<>();
    }

    IListEdit<AttrParam> params = new ElemArrayList<>();

    for( IDtoAttrInfo pInfo : classInfo.attrs().list() ) {

      params.add( formRriAttrParam( pInfo ) );

    }

    return params;
  }

  private AttrParam formRriAttrParam( IDtoAttrInfo aInfo ) {
    String setionId = RriUtils.getRriSectionId( context );
    ISkRriSection rriSection = master().findSection( setionId );
    if( rriSection == null ) {
      return new AttrParam( aInfo, IAtomicValue.NULL, false );
    }
    if( objects.size() == 0 ) {
      return new AttrParam( aInfo, IAtomicValue.NULL, false );
    }
    IAtomicValue prev = null;
    boolean different = false;
    for( ISkObject obj : objects ) {
      IAtomicValue objVal = master().getSection( RriUtils.getRriSectionId( context ) )
          .getAttrParamValue( new Skid( obj.classId(), obj.strid() ), aInfo.id() );
      if( prev != null ) {
        if( !prev.equals( objVal ) ) {
          different = true;
          prev = IAtomicValue.NULL;
          break;
        }
      }
      prev = objVal;
    }
    return new AttrParam( aInfo, prev, different );
  }

}
