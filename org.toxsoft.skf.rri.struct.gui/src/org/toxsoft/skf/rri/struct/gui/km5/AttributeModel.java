package org.toxsoft.skf.rri.struct.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.valeds.IM5ValedConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.models.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель редактора описания НСИ атрибута.
 *
 * @author max
 */
public class AttributeModel
    extends M5Model<IDtoAttrInfo> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "ru.skide.sysnode.rri.mod.guilib.structedit.AttributeModel"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #ATTR_ID}.
   */
  public static final String FID_ATTR_ID = "AttrId"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #ATTR_TYPE}.
   */
  public static final String FID_ATTR_TYPE = "AttrType"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #ATTR_NAME}.
   */
  public static final String FID_ATTR_NAME = "ts.Name"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #ATTR_DESCR}.
   */
  public static final String FID_ATTR_DESCR = "ts.Descr"; //$NON-NLS-1$

  /**
   * Поле идентификатора атрибута
   */
  public final M5AttributeFieldDef<IDtoAttrInfo> ATTR_ID = new M5AttributeFieldDef<>( FID_ATTR_ID, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( "Идентификатор", "Идентификатор атрибута" );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_INVARIANT );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoAttrInfo aEntity ) {
      return AvUtils.avStr( aEntity.id() );
    }
  };

  /**
   * Поле типа данных, которое находится в реестре по ключу {@link IDtoAttrInfo#dataType()}.
   */
  public final IM5SingleModownFieldDef<IDtoAttrInfo, IDataType> ATTR_TYPE =
      new M5SingleModownFieldDef<>( FID_ATTR_TYPE, DataTypeM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( "Тип", "Тип атрибута" );
          setFlags( M5FF_COLUMN );
          params().setBool( TSID_IS_NULL_ALLOWED, false );
          params().setStr( M5_VALED_OPDEF_WIDGET_TYPE_ID, M5VWTID_INPLACE );
          // params().setInt( IValedControlConstants.OPDEF_VERTICAL_SPAN, 10 );
        }

        protected IDataType doGetFieldValue( IDtoAttrInfo aEntity ) {
          return aEntity.dataType();
        }

      };

  // ISkDataTypesManager dtm() {
  // ISkConnection skConn;// = domain().m5().get( ISkConnection.class );
  // return skConn.coreApi().sysdescr().
  // }

  /**
   * Поле наименования атрибута
   */
  public final M5AttributeFieldDef<IDtoAttrInfo> ATTR_NAME = new M5AttributeFieldDef<>( FID_ATTR_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( "Наименование", "Наименование атрибута" );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoAttrInfo aEntity ) {
      return AvUtils.avStr( aEntity.nmName() );
    }
  };

  /**
   * Поле описания атрибута
   */
  public final M5AttributeFieldDef<IDtoAttrInfo> ATTR_DESCR = new M5AttributeFieldDef<>( FID_ATTR_DESCR, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( "Описание", "Описание атрибута" );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoAttrInfo aEntity ) {
      return AvUtils.avStr( aEntity.description() );
    }
  };

  /**
   * Конструктор.
   */
  public AttributeModel() {
    super( MODEL_ID, IDtoAttrInfo.class );
    setNameAndDescription( "Список атрибутов", "Список атрибутов" );

    addFieldDefs( ATTR_ID, ATTR_TYPE, ATTR_NAME, ATTR_DESCR );
  }

}
