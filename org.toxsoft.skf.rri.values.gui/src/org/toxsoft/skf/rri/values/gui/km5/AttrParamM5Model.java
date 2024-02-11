package org.toxsoft.skf.rri.values.gui.km5;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.skf.rri.values.gui.km5.ITsResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.swt.dnd.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.rri.values.gui.utils.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Модель для редактирования списка атрибутов
 *
 * @author max
 */
public class AttrParamM5Model
    extends M5Model<AttrParam> {

  final static String ACTID_COPY_GWID = SK_ID + "rri.values.gui.copy.gwid"; //$NON-NLS-1$

  final static TsActionDef ACDEF_COPY_GWID =
      TsActionDef.ofPush2( ACTID_COPY_GWID, STR_N_COPY_TO_CLIPBOARD, STR_D_COPY_TO_CLIPBOARD, ICONID_EDIT_COPY );

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
              protected ITsToolbar doCreateToolbar( ITsGuiContext aaContext, String aName, EIconSize aIconSize,
                  IListEdit<ITsActionDef> aActs ) {

                aActs.add( ACDEF_SEPARATOR );
                aActs.add( ACDEF_COPY_GWID );

                ITsToolbar toolbar =

                    super.doCreateToolbar( aaContext, aName, aIconSize, aActs );

                toolbar.addListener( aActionId -> {
                  // nop

                } );

                return toolbar;
              }

              @Override
              protected void doProcessAction( String aActionId ) {

                switch( aActionId ) {
                  case ACTID_COPY_GWID:
                    AttrParam param = selectedItem();
                    ISkObject obj = ((AttrParamM5LifeCycleManager)aLifecycleManager).getObjects().first();

                    String paramId = param.getAttrInfo().id();

                    String s = Gwid.createAttr( obj.classId(), obj.id(), paramId ).asString();

                    Clipboard clipboard = new Clipboard( getDisplay() );
                    clipboard.setContents( new String[] { s }, new Transfer[] { TextTransfer.getInstance() } );
                    clipboard.dispose();
                    break;
                  default:
                    break;
                }
              }

              @Override
              protected AttrParam doEditItem( AttrParam aItem ) {
                // editValue = aItem.getValue();
                // return super.doEditItem( aItem );

                TsDialogInfo cdi = new TsDialogInfo( aContext, null, STR_DLG_TITLE_EDITING_RRI_ATTR,
                    STR_DLG_TITLE_EDITING_RRI_ATTR, 0 );

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

              @Override
              protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, AttrParam aSel ) {
                boolean isEnable = ((AttrParamM5LifeCycleManager)aLifecycleManager).getObjects().size() == 1 && aIsSel;
                toolbar().setActionEnabled( ACTID_COPY_GWID, isEnable );
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
