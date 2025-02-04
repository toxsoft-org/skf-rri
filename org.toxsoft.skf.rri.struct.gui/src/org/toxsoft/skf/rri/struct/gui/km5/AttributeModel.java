package org.toxsoft.skf.rri.struct.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.valeds.IM5ValedConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.struct.gui.km5.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.models.av.*;
import org.toxsoft.core.tsgui.m5.valeds.singlemodown.*;
import org.toxsoft.core.tsgui.valed.api.*;
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
  public static final String MODEL_ID = "org.toxsoft.skf.rri.struct.gui.km5.AttributeModel"; //$NON-NLS-1$

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
      setNameAndDescription( STR_N_ATTR_ID, STR_D_ATTR_ID );
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
          setNameAndDescription( STR_N_ATTR_TYPE, STR_D_ATTR_TYPE );
          setFlags( M5FF_COLUMN );
          params().setBool( TSID_IS_NULL_ALLOWED, false );
          params().setStr( M5_VALED_OPDEF_WIDGET_TYPE_ID, M5VWTID_INPLACE );
          params().setInt( IValedControlConstants.OPDEF_VERTICAL_SPAN, 7 );
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
      setNameAndDescription( STR_N_ATTR_NAME, STR_D_ATTR_NAME );
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
      setNameAndDescription( STR_N_ATTR_DESCRIPTION, STR_D_ATTR_DESCRIPTION );
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
    setNameAndDescription( STR_N_ATTRIBUTES_LIST, STR_D_ATTRIBUTES_LIST );

    addFieldDefs( ATTR_ID, ATTR_TYPE, ATTR_NAME, ATTR_DESCR );
    // panels creator
    setPanelCreator( new M5DefaultPanelCreator<>() {

      protected IM5EntityPanel<IDtoAttrInfo> doCreateEntityEditorPanel( ITsGuiContext aContext,
          IM5LifecycleManager<IDtoAttrInfo> aLifecycleManager ) {
        return new M5DefaultEntityControlledPanel<>( aContext, model(), aLifecycleManager, new Controller() );
      }
    } );

  }

  private class Controller
      extends M5EntityPanelWithValedsController<IDtoAttrInfo> {

    public Controller() {
    }

    @Override
    public boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<IDtoAttrInfo, ?> aFieldDef,
        boolean aEditFinished ) {
      switch( aFieldDef.id() ) {
        case FID_ATTR_TYPE:
          // when changing type check and add
          IDataType dt = (IDataType)editors().getByKey( FID_ATTR_TYPE ).getValue();
          if( !dt.params().hasKey( TSID_DEFAULT_VALUE ) ) {
            IAtomicValue defaultVal = getDefaultValue( dt.atomicType() );
            DataType updateDt = DataType.create( dt, TSID_DEFAULT_VALUE, defaultVal );
            ValedSingleModownInplaceEditor<DataType> editor =
                (ValedSingleModownInplaceEditor)editors().getByKey( FID_ATTR_TYPE );
            editor.setValue( updateDt );
          }
          break;
        default:
          break;
      }
      return true;
    }

    private IAtomicValue getDefaultValue( EAtomicType aAtomicType ) {
      IAtomicValue retVal = IAtomicValue.NULL;
      switch( aAtomicType ) {
        case BOOLEAN:
          retVal = AvUtils.AV_TRUE;
          break;
        case FLOATING:
          retVal = AvUtils.avFloat( 0.0 );
          break;
        case INTEGER:
          retVal = AvUtils.avInt( 0 );
          break;
        case TIMESTAMP:
          retVal = AvUtils.avTimestamp( 0 );
          break;
        case STRING:
          retVal = AvUtils.avStr( "<enter value>" ); //$NON-NLS-1$
          break;
        case NONE:
        case VALOBJ:
          break;
        default:
          break;
      }
      return retVal;
    }

  }

}
