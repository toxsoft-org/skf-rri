package org.toxsoft.skf.rri.values.gui.km5;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * Менеджер ЖЦ объектов классов НСИ.
 *
 * @author max
 */
public class ObjectsM5LifeCycleManager
    extends M5LifecycleManager<ISkObject, ISkCoreApi> {

  /**
   * Идентификаторы классов, объекты которых следует загружать (список меняется за время ЖЦ)
   */
  private IStringList classIds = new StringArrayList();

  /**
   * Объект, к которому по связям с определённым идентфикатором должны иерархически принадлежать выдаваемые объекты.
   * Может быть null - тогда выдаются все загруженные объекты классов. Объект может меняться за время ЖЦ.
   */
  private ISkObject topObject = ISkObject.NONE;

  /**
   * Признак того, что есть специальная связь, создающая иерархию, по которой можно дойти до топового ограничивающего
   * объекта. Если связи нет - выдаются все загруженные объекты классов. (не меняется за время ЖЦ)
   */
  private boolean hasLink = false;

  /**
   * Идентификатор специальной связи. Если связи нет - выдаются все загруженные объекты классов. (не меняется за время
   * ЖЦ)
   */
  private String linkId = TsLibUtils.EMPTY_STRING;

  /**
   * Конструктор.
   *
   * @param aContext {@link IEclipseContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aMaster {@link ISkCoreApi} - мастер-объект
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ObjectsM5LifeCycleManager( ITsGuiContext aContext, IM5Model<ISkObject> aModel, ISkCoreApi aMaster ) {
    super( aModel, false, false, false, true, aMaster );
    TsNullArgumentRtException.checkNulls( aContext, aMaster );

  }

  /**
   * Возвращает корневой объект.
   *
   * @return ISkObject - корневой объект, может быть null
   */
  public ISkObject getTopObject() {
    return topObject;
  }

  /**
   * Устанавливает корневой объект.
   *
   * @param aTopObject ISkObject - корневой объект, может быть null
   */
  public void setTopObject( ISkObject aTopObject ) {
    topObject = aTopObject;
  }

  /**
   * Возвращает список идентификаторов классов.
   *
   * @return IStringList - список идентификаторов классов.
   */
  public IStringList getClassIds() {
    return classIds;
  }

  /**
   * Устанавливает список идентификаторов классов.
   *
   * @param aClassIds IStringList - список идентификаторов классов.
   */
  public void setClassIds( IStringList aClassIds ) {
    classIds = aClassIds;
  }

  private IListEdit<ISkObject> filterObjects( IListEdit<ISkObject> aResult ) {
    System.out.println( linkId );
    return aResult;
  }

  @Override
  public IList<ISkObject> doListEntities() {
    IListEdit<ISkObject> result = new ElemArrayList<>( false );

    if( hasLink && topObject != null && topObject != ISkObject.NONE ) {
      System.out.println( linkId );
      addChildren( topObject, result );
      return result;
    }
    for( String clsId : classIds ) {
      result.addAll( master().objService().listObjs( clsId, true ) );
    }

    return result;
  }

  private void addChildren( ISkObject aLocalTopObj, IListEdit<ISkObject> aAddResult ) {

    if( classIds.hasElem( aLocalTopObj.classId() ) ) {
      aAddResult.add( aLocalTopObj );
    }

    ISkClassInfo classInfo = master().sysdescr().getClassInfo( aLocalTopObj.classId() );
    if( !classInfo.links().list().hasKey( linkId ) ) {
      return;
    }

    ISkLinkService lService = master().linkService();

    IDtoLinkFwd linkObjs = lService.getLinkFwd( new Skid( aLocalTopObj.classId(), aLocalTopObj.strid() ), linkId );

    ISkObjectService objService = master().objService();
    IList<ISkObject> children = objService.getObjs( linkObjs.rightSkids() );
    for( ISkObject child : children ) {
      addChildren( child, aAddResult );
    }
  }
}
