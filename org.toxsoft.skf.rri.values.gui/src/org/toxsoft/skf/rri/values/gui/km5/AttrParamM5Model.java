package org.toxsoft.skf.rri.values.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.skf.rri.values.gui.km5.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.rri.values.gui.utils.*;

/**
 * Модель для редактирования списка атрибутов
 *
 * @author max
 */
public class AttrParamM5Model
    extends M5Model<AttrParam> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "org.toxsoft.skf.rri.values.gui.km5.AttrParamM5Model"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #VIS_NAME}.
   */
  public static final String FID_VIS_NAME = "VisName"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #VALUE}.
   */
  public static final String FID_VALUE = "ts.Value"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #REASON}.
   */
  public static final String FID_REASON = "ts.Reason"; //$NON-NLS-1$

  /**
   * Наименование параметра
   */
  public final M5AttributeFieldDef<AttrParam> VIS_NAME = new M5AttributeFieldDef<>( FID_VIS_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( FNAME_VIS_NAME_ATTR, FDESCR_VIS_NAME_ATTR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( AttrParam aEntity ) {
      return AvUtils.avStr( aEntity.getAttrInfo().nmName() );
    }

  };

  /**
   * Значение параметра
   */
  public final M5AttributeFieldDef<AttrParam> VALUE = new M5AttributeFieldDef<>( FID_VALUE, NONE ) {

    @Override
    protected void doInit() {
      setNameAndDescription( FNAME_VALUE_ATTR, FDESCR_VALUE_ATTR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN );
      setGetter( null );
    }

    @Override
    protected IAtomicValue doGetFieldValue( AttrParam aEntity ) {
      return aEntity.getAttrValue();
    }

    @Override
    protected String doGetFieldValueName( AttrParam aEntity ) {
      if( aEntity.isDifferent() ) {
        return DIFFERENT_VALUE_STR;
      }

      // TODO Выяснить у Гоги как корректно заводить форматы
      if( aEntity.getAttrInfo().params().hasValue( "timeFormat" ) ) { //$NON-NLS-1$
        // Dima, 04.06.19
        if( aEntity.getAttrInfo().params().getStr( "timeFormat" ).equals( "HH_mm_ss" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
          IAtomicValue timeVal = aEntity.getAttrValue();
          if( timeVal != IAtomicValue.NULL && timeVal.isAssigned() ) {
            int time = timeVal.asInt();
            return HmsUtils.hhmmss( time );
          }
        }
        if( aEntity.getAttrInfo().params().getStr( "timeFormat" ).equals( "mm_ss" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
          IAtomicValue timeVal = aEntity.getAttrValue();
          if( timeVal != IAtomicValue.NULL && timeVal.isAssigned() ) {
            long time = timeVal.asLong();
            long mm = time / 60;
            long ss = time % 60;
            return String.format( "%d min, %d sec", Long.valueOf( mm ), Long.valueOf( ss ) ); //$NON-NLS-1$
          }
        }
      }

      return super.doGetFieldValueName( aEntity );
    }
  };

  /**
   * Причина изменения параметра
   */
  public final M5AttributeFieldDef<AttrParam> REASON = new M5AttributeFieldDef<>( FID_REASON, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( FNAME_REASON_ATTR, FDESCR_REASON_ATTR );
      setDefaultValue( IAtomicValue.NULL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( AttrParam aEntity ) {
      return AvUtils.avStr( TsLibUtils.EMPTY_STRING );
    }

  };

  /**
   * Конструктор.
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public AttrParamM5Model() {
    super( MODEL_ID, AttrParam.class );

    setNameAndDescription( ATTR_PARAM_MODEL_NAME, ATTR_PARAM_MODEL_NAME );
    IListEdit<IM5FieldDef<?, ?>> fDefs = new ElemArrayList<>();
    fDefs.add( VIS_NAME );
    fDefs.add( VALUE );
    fDefs.add( REASON );

    addFieldDefs( VIS_NAME, VALUE, REASON );

    setPanelCreator( new M5DefaultPanelCreator<AttrParam>() {

      @Override
      protected IM5CollectionPanel<AttrParam> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<AttrParam> aItemsProvider, IM5LifecycleManager<AttrParam> aLifecycleManager ) {
        IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AvUtils.AV_TRUE );
        MultiPaneComponentModown<AttrParam> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager ) {

              @Override
              protected AttrParam doEditItem( AttrParam aItem ) {
                // editValue = aItem.getValue();
                // return super.doEditItem( aItem );

                TsDialogInfo cdi =
                    new TsDialogInfo( aContext, null, "Редактирование атрибута НСИ", "Редактирование атрибута НСИ", 0 );

                IM5EntityPanel<AttrParam> editPanel =
                    new M5DefaultEntityEditorPanel<>( aContext, model(), aLifecycleManager ) {

                      @SuppressWarnings( "hiding" )
                      @Override
                      protected IValedControl doCreateEditor( IValedControlFactory aFactory,
                          IM5FieldDef<AttrParam, ?> aFieldDef, ITsGuiContext aContext ) {
                        IValedControlFactory factory = null;
                        if( aFieldDef.id().equals( FID_VALUE ) ) {
                          factory = RriUtils.searchValedControlFactory( aItem.getAttrInfo() );
                        }
                        if( factory == null ) {
                          factory = aFactory;
                        }
                        return super.doCreateEditor( factory, aFieldDef, aContext );
                      }

                    };

                return M5GuiUtils.askEdit( tsContext(), editPanel, aItem, cdi );
              }

              protected boolean doGetIsEditAllowed( AttrParam aSel ) {
                return ((AttrParamM5LifeCycleManager)aLifecycleManager).getObjects().size() > 0;
              }

            };
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

      // @Override
      // protected IM5EntityEditPanel<AttrParamTest> doCreateEntityEditorPanel( ITsGuiContext aContext,
      // IM5LifecycleManager<AttrParamTest> aLifecycleManager ) {
      //
      // return new M5DefaultEntityEditorPanel<AttrParamTest>( aContext, aLifecycleManager, model(), false ) {
      //
      // @Override
      // protected IValedControl doCreateEditor( IValedControlFactory aFactory,
      // IM5FieldDef<AttrParamTest, ?> aFieldDef, ITsGuiContext aContext ) {
      // IValedControlFactory factory = aFactory;
      // if( aFieldDef.id().equals( FID_VALUE ) ) {
      // factory = ValedControlUtils.getDefaultFactory( editValue.atomicType() );
      // }
      // return super.doCreateEditor( factory, aFieldDef, aContext );
      // }
      // };
      // }
    } );

  }

}
