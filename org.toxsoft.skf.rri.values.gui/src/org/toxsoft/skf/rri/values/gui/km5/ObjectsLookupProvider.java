package org.toxsoft.skf.rri.values.gui.km5;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.rri.values.gui.km5.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Поставщик объектов некоторых классов
 *
 * @author max
 */
public class ObjectsLookupProvider
    implements IM5LookupProvider<ISkObject> {

  private static final EmptySkObject EMPTY_LINK_OBJECT = new EmptySkObject();

  private ITsGuiContext context;
  private IStringList   classes;

  private boolean hasNullEkement;

  /**
   * Конструктор поставщика объектов указанных классов.
   *
   * @param aContext ITsGuiContext - контекст.
   * @param aClasses IStringList - списко классов, объекты которых будут предоставлены.
   */
  public ObjectsLookupProvider( ITsGuiContext aContext, IStringList aClasses ) {
    this( aContext, aClasses, false );
  }

  /**
   * Конструктор поставщика объектов указанных классов с возможностью добавить пустой объект (не SkObject.NONE, но
   * имеющего skid = Skid.NONE)
   *
   * @param aContext ITsGuiContext - контекст.
   * @param aClasses IStringList - списко классов, объекты которых будут предоставлены.
   * @param aHasNullEkement true - в начало списка объектов будет добавлен пустой объект, false - не будет.
   */
  public ObjectsLookupProvider( ITsGuiContext aContext, IStringList aClasses, boolean aHasNullEkement ) {
    context = aContext;
    classes = aClasses;

    hasNullEkement = aHasNullEkement;
  }

  @Override
  public IList<ISkObject> listItems() {
    IListEdit<ISkObject> result = new ElemArrayList<>();
    if( hasNullEkement ) {
      result.add( EMPTY_LINK_OBJECT );// by Max - для обнуления ссылки
    }

    ISkConnectionSupplier connSup = context.get( ISkConnectionSupplier.class );
    ISkObjectService objSrv = connSup.defConn().coreApi().objService();

    for( String clsId : classes ) {
      IList<ISkObject> objs = objSrv.listObjs( clsId, true );

      for( int i = 0; // i < 5 &&
          i < objs.size(); i++ ) {
        result.add( objs.get( i ) );
      }

    }

    return result;
  }

  static class EmptySkObject
      implements ISkObject {

    private OptionSet attrs = new OptionSet();

    EmptySkObject() {
      attrs.setStr( ISkHardConstants.AID_NAME, EMPTY_LINK_OBJECT_VIS_NAME );
      attrs.setStr( ISkHardConstants.AID_DESCRIPTION, EMPTY_LINK_OBJECT_VIS_NAME );
      attrs.setValue( ISkHardConstants.AID_STRID, avStr( "-" ) ); //$NON-NLS-1$
    }

    @Override
    public Skid skid() {
      return Skid.NONE;
    }

    @Override
    public String nmName() {
      return EMPTY_LINK_OBJECT_VIS_NAME;
    }

    @Override
    public String id() {
      return skid().strid();
    }

    @Override
    public String description() {
      return EMPTY_LINK_OBJECT_VIS_NAME;
    }

    @Override
    public IOptionSet attrs() {
      return attrs;
    }

    @Override
    public ISkCoreApi coreApi() {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public Skid getSingleLinkSkid( String aLinkId ) {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public ISkidList getLinkSkids( String aLinkId ) {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public <T extends ISkObject> IList<T> getLinkObjs( String aLinkId ) {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public ISkidList getLinkRevSkids( String aClassId, String aLinkId ) {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public IMappedSkids rivets() {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public String classId() {
      return Skid.NONE.classId();
    }

    @Override
    public String strid() {
      return Skid.NONE.strid();
    }

    @Override
    public String readableName() {
      return Skid.NONE.classId() + "," + Skid.NONE.strid(); //$NON-NLS-1$
    }

    @Override
    public ISkidList getRivetRevSkids( String aClassId, String aRivetId ) {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public <T extends ISkObject> IList<T> getRivetRevObjs( String aClassId, String aRivetId ) {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public String getClob( String aClobId, String aDefaultValue ) {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public <T extends ISkObject> T getSingleLinkObj( String aLinkId ) {
      throw new TsNullObjectErrorRtException();
    }

    @Override
    public <T extends ISkObject> IList<T> getLinkRevObjs( String aClassId, String aLinkId ) {
      throw new TsNullObjectErrorRtException();
    }
  }

}
