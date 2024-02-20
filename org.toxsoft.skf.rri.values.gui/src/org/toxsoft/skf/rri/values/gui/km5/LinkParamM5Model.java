package org.toxsoft.skf.rri.values.gui.km5;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.rri.values.gui.km5.ITsResources.*;

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
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Модель для теста редактирования списка параметров-связей НСИ
 *
 * @author max
 */
public class LinkParamM5Model
    extends M5Model<LinkParam> {

  /**
   * Формат идентификатора
   */
  public static final String MODEL_ID_FORMAT = "org.toxsoft.skf.rri.values.gui.km5.%s"; //$NON-NLS-1$

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = String.format( MODEL_ID_FORMAT, "LinkParamM5Model" ); //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #VIS_NAME}.
   */
  public static final String FID_VIS_NAME = "VisName"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #DESCRIPTION}.
   */
  public static final String FID_DESCRIPTION = "ts.Description"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #GWID}.
   */
  public static final String FID_GWID = "ts.Gwid"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #VIS_VALUE}.
   */
  public static final String FID_VIS_VALUE = "VisValue"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #MULTI_VALUE}.
   */
  public static final String FID_MULTI_VALUE = "ts.MultiValue"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #SINGLE_VALUE}.
   */
  public static final String FID_SINGLE_VALUE = "ts.SingleValue"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #REASON}.
   */
  public static final String FID_REASON = "ts.Reason"; //$NON-NLS-1$

  /**
   * Поле наименования связи
   */
  public final M5AttributeFieldDef<LinkParam> VIS_NAME = new M5AttributeFieldDef<>( FID_VIS_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( FNAME_VIS_NAME_LINK, FDESCR_VIS_NAME_LINK );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( LinkParam aEntity ) {
      return avStr( aEntity.getLinkInfo().nmName() );
    }
  };

  /**
   * Видимое в таблице поле значения, но не видимое в диалоге
   */
  public final M5AttributeFieldDef<LinkParam> VIS_VALUE = new M5AttributeFieldDef<>( FID_VIS_VALUE, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( FNAME_VIS_VALUE_LINK, FDESCR_VIS_VALUE_LINK );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
    }

    @Override
    protected String doGetFieldValueName( LinkParam aEntity ) {
      if( aEntity.isDifferent() ) {
        return DIFFERENT_VALUE_STR;
      }
      boolean singleEdit = aEntity.getLinkInfo().linkConstraint().maxCount() == 1;
      if( singleEdit ) {
        return SINGLE_VALUE.getter().getName( aEntity );
      }
      return MULTI_VALUE.getter().getName( aEntity );

    }

    @Override
    protected IAtomicValue doGetFieldValue( LinkParam aEntity ) {
      return avStr( doGetFieldValueName( aEntity ) );
    }

  };

  /**
   * Описание параметра
   */
  public final M5AttributeFieldDef<LinkParam> DESCRIPTION = new M5AttributeFieldDef<>( FID_DESCRIPTION, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( FNAME_DESCRIPTION_LINK, FDESCR_DESCRIPTION_LINK );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_DETAIL | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( LinkParam aEntity ) {
      return AvUtils.avStr( aEntity.getLinkInfo().description() );
    }

  };

  /**
   * Gwid параметра
   */
  public final M5AttributeFieldDef<LinkParam> GWID = new M5AttributeFieldDef<>( FID_GWID, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( FNAME_GWID_LINK, FDESCR_GWID_LINK );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( LinkParam aEntity ) {
      IList<ISkObject> objs = ((LinkParamM5LifeCycleManager)getLifecycleManager( null )).getObjects();
      if( objs.size() == 0 ) {
        return AvUtils.AV_STR_EMPTY;
      }
      ISkObject obj = objs.first();

      String paramId = aEntity.getLinkInfo().id();

      String s = Gwid.createLink( obj.classId(), obj.id(), paramId ).asString();
      return AvUtils.avStr( s );
    }

  };

  /**
   * Поле редактируемой множественной связи, не видимое в таблице, но видимое в диалоге.
   */
  public final M5MultiLookupFieldDef<LinkParam, ISkObject> MULTI_VALUE =
      new M5MultiLookupFieldDef<>( FID_MULTI_VALUE, ObjectCheckableM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( FNAME_MUILTI_VALUE_LINK, FDESCR_MUILTI_VALUE_LINK );
          // setFlags( M5FF_COLUMN | M5FF_DETAIL );
        }

        @Override
        protected IList<ISkObject> doGetFieldValue( LinkParam aEntity ) {
          if( aEntity.getLinkInfo().linkConstraint().maxCount() == 1 ) {
            return IList.EMPTY;
          }

          if( extraMultiValuesFields.hasKey( aEntity.getLinkInfo().id() ) ) {
            return extraMultiValuesFields.getByKey( aEntity.getLinkInfo().id() ).getFieldValue( aEntity );
          }

          ITsGuiContext usingContext = modelContext != null ? modelContext : domain().tsContext();
          ISkConnection skConn = usingContext.get( ISkConnection.class );

          IListEdit<ISkObject> result = new ElemArrayList<>();
          for( Skid skid : aEntity.getValue() ) {
            ISkObject obj = skConn.coreApi().objService().find( skid );

            result.add( obj );
          }
          return result;
        }

        @Override
        protected String doGetFieldValueName( LinkParam aEntity ) {
          StringBuilder result = new StringBuilder();
          String add = TsLibUtils.EMPTY_STRING;
          ITsCollection<ISkObject> objs = doGetFieldValue( aEntity );
          for( ISkObject u : objs ) {
            result.append( add ).append( u.strid() );
            add = ",\n"; //$NON-NLS-1$
          }
          return result.toString();
        }

      };

  /**
   * Поле редактируемой одиночной связи, не видимое в таблице, но видимое в диалоге.
   */
  public final M5SingleLookupFieldDef<LinkParam, ISkObject> SINGLE_VALUE =
      new M5SingleLookupFieldDef<>( FID_SINGLE_VALUE, ObjectCheckableM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( FNAME_SINGLE_VALUE_LINK, FDESCR_SINGLE_VALUE_LINK );
          // setFlags( M5FF_COLUMN | M5FF_DETAIL );
        }

        @Override
        protected ISkObject doGetFieldValue( LinkParam aEntity ) {
          if( aEntity.getLinkInfo().linkConstraint().maxCount() != 1 ) {
            return null;// ISkObject.NULL;
          }

          ITsGuiContext usingContext = modelContext != null ? modelContext : domain().tsContext();
          ISkConnection skConn = usingContext.get( ISkConnection.class );
          ISkObjectService objServ = skConn.coreApi().objService();

          ISkidList entites = aEntity.getValue();
          if( entites.size() == 0 ) {
            return null;// ISkObject.NULL;
          }
          Skid skid = entites.first();
          ISkObject obj = objServ.find( skid );
          return obj;// != null ? obj : ISkObject.NULL;
        }

      };

  /**
   * Причина изменения параметра
   */
  public final M5AttributeFieldDef<LinkParam> REASON = new M5AttributeFieldDef<>( FID_REASON, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( FNAME_REASON_LINK, FDESCR_REASON_LINK );
      setDefaultValue( IAtomicValue.NULL );

      // setFlags( M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( LinkParam aEntity ) {
      return avStr( TsLibUtils.EMPTY_STRING );
    }
  };

  /**
   * Дополнительные поля множественных связей.
   */
  private IMapEdit<String, M5MultiLookupFieldDef<LinkParam, ISkObject>> extraMultiValuesFields = new ElemMap<>();

  private ITsGuiContext modelContext;

  /**
   * Возвращает контекст модели (может быть null)
   *
   * @return ITsGuiContext - контекст модели (может быть null)
   */
  public ITsGuiContext getModelContext() {
    return modelContext;
  }

  /**
   * Устанавливает контекст модели (может быть null - тогда используется доменный контекст)
   *
   * @param aModelContext ITsGuiContext - контекст модели (может быть null - тогда используется доменный контекст)
   */
  public void setModelContext( ITsGuiContext aModelContext ) {
    modelContext = aModelContext;
  }

  /**
   * Конструктор с идентификатором модели по умолчанию.
   */
  public LinkParamM5Model() {
    this( MODEL_ID );

  }

  /**
   * Конструктор по идентификатору модели.
   *
   * @param aModelId String - идентификатор модели.
   */
  public LinkParamM5Model( String aModelId ) {
    super( aModelId, LinkParam.class );

    setNameAndDescription( LINK_PARAM_MODEL_NAME, LINK_PARAM_MODEL_NAME );

    addFieldDefs( VIS_NAME, GWID, VIS_VALUE, MULTI_VALUE, SINGLE_VALUE, REASON, DESCRIPTION );

    setPanelCreator( new M5DefaultPanelCreator<LinkParam>() {

      @Override
      protected IM5CollectionPanel<LinkParam> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<LinkParam> aItemsProvider, IM5LifecycleManager<LinkParam> aLifecycleManager ) {
        IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AvUtils.AV_TRUE );
        MultiPaneComponentModown<LinkParam> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager ) {

              @Override
              protected ITsToolbar doCreateToolbar( ITsGuiContext aaContext, String aName, EIconSize aIconSize,
                  IListEdit<ITsActionDef> aActs ) {

                aActs.add( ACDEF_SEPARATOR );
                aActs.add( AttrParamM5Model.ACDEF_COPY_GWID );

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
                  case AttrParamM5Model.ACTID_COPY_GWID:
                    LinkParam param = selectedItem();
                    ISkObject obj = ((LinkParamM5LifeCycleManager)aLifecycleManager).getObjects().first();

                    String paramId = param.getLinkInfo().id();

                    String s = Gwid.createLink( obj.classId(), obj.id(), paramId ).asString();

                    Clipboard clipboard = new Clipboard( getDisplay() );
                    clipboard.setContents( new String[] { s }, new Transfer[] { TextTransfer.getInstance() } );
                    clipboard.dispose();
                    break;
                  default:
                    break;
                }
              }

              @Override
              protected LinkParam doEditItem( LinkParam aItem ) {

                // установка списка возможных значений
                boolean singleEdit = aItem.getLinkInfo().linkConstraint().maxCount() == 1;
                if( !singleEdit ) {
                  if( extraMultiValuesFields.hasKey( aItem.getLinkInfo().id() ) ) {
                    extraMultiValuesFields.getByKey( aItem.getLinkInfo().id() )
                        .setLookupProvider( getLookupProviderBeforeEdit( aContext, aItem ) );
                  }
                  else {
                    MULTI_VALUE.setLookupProvider(
                        new ObjectsLookupProvider( aContext, aItem.getLinkInfo().rightClassIds() ) );
                  }
                }

                if( singleEdit ) {
                  SINGLE_VALUE.setLookupProvider(
                      new ObjectsLookupProvider( aContext, aItem.getLinkInfo().rightClassIds(), true ) );
                }

                TsDialogInfo cdi = new TsDialogInfo( aContext, null, STR_DLG_TITLE_EDITING_RRI_LINK,
                    STR_DLG_TITLE_EDITING_RRI_LINK, 0 );

                IM5EntityPanel<LinkParam> editPanel =
                    new M5DefaultEntityEditorPanel<>( aContext, model(), aLifecycleManager ) {

                      @Override
                      protected void doInitEditors() {

                        // создает редакторы всех полей без признака M5FF_HIDDEN
                        // при этом добавляется конкретное после редактирования связи - либо одиночной, либо
                        // множественной,
                        // либо множественной из добавочных в классе - наследнике
                        for( IM5FieldDef<LinkParam, ?> fDef : model().fieldDefs() ) {
                          if( singleEdit && fDef.id().equals( FID_MULTI_VALUE ) ) {
                            continue;
                          }
                          if( !singleEdit && fDef.id().equals( FID_SINGLE_VALUE ) ) {
                            continue;
                          }
                          if( !singleEdit && extraMultiValuesFields.hasKey( aItem.getLinkInfo().id() )
                              && extraMultiValuesFields.getByKey( aItem.getLinkInfo().id() ) != fDef ) {
                            continue;
                          }
                          if( (fDef.flags() & M5FF_HIDDEN) == 0 ) {
                            addField( fDef.id() );
                          }
                        }
                      }

                      @Override
                      protected IValedControl doCreateEditor( IValedControlFactory aFactory,
                          IM5FieldDef<LinkParam, ?> aFieldDef, ITsGuiContext aaContext ) {
                        IValedControlFactory factory = aFactory;
                        IM5ValedConstants.M5_VALED_OPDEF_WIDGET_TYPE_ID.setValue( aaContext.params(),
                            avStr( IM5ValedConstants.M5VWTID_TEXT ) );

                        // ValedSingleLookupEditor.IS_LOOKUP_EDITABLE.setValue( aaContext.params(), AV_FALSE );
                        return super.doCreateEditor( factory, aFieldDef, aaContext );
                      }
                    };

                return M5GuiUtils.askEdit( tsContext(), editPanel, aItem, cdi );

                // return super.doEditItem( aItem );
              }

              protected boolean doGetIsEditAllowed( LinkParam aSel ) {
                return ((LinkParamM5LifeCycleManager)aLifecycleManager).getObjects().size() > 0;
              }

              @Override
              protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, LinkParam aSel ) {
                boolean isEnable = ((LinkParamM5LifeCycleManager)aLifecycleManager).getObjects().size() == 1 && aIsSel;
                toolbar().setActionEnabled( AttrParamM5Model.ACTID_COPY_GWID, isEnable );
              }
            };
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );

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
    // } );

  }

  @Override
  protected IM5LifecycleManager<LinkParam> doCreateLifecycleManager( Object aMaster ) {
    ITsGuiContext context = tsContext();

    return new LinkParamM5LifeCycleManager( context, this, (ISkRegRefInfoService)aMaster );
  }

  @Override
  protected IM5LifecycleManager<LinkParam> doCreateDefaultLifecycleManager() {
    ITsGuiContext context = tsContext();

    ISkConnectionSupplier connSup = context.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();

    ISkRegRefInfoService serv = conn.coreApi().getService( ISkRegRefInfoService.SERVICE_ID );

    return new LinkParamM5LifeCycleManager( context, this, serv );
  }

  /**
   * Добавление дополнительного поля редактирования множественной связи
   *
   * @param aLinkId - идентификатор связи
   * @param aField M5MultiLookupFieldDef - поле
   */
  public void addExtraMultiValueField( String aLinkId, M5MultiLookupFieldDef<LinkParam, ISkObject> aField ) {
    extraMultiValuesFields.put( aLinkId, aField );
    addFieldDefs( aField );
  }

  /**
   * Метод для переопределения - поставщик списка возможных значений для редактора.
   *
   * @param aContext - контекст
   * @param aItem - значение конкретного параметра
   * @return IM5LookupProvider - поставщик возможных значений
   */
  protected IM5LookupProvider<ISkObject> getLookupProviderBeforeEdit( ITsGuiContext aContext, LinkParam aItem ) {
    return new ObjectsLookupProvider( aContext, aItem.getLinkInfo().rightClassIds() );
  }

  /**
   * Возвращает отредактированное значение множественной связи.
   *
   * @param aValues IM5Bunch - field values, never is null
   * @return ITsReferenceCollection - значение связи.
   */
  @SuppressWarnings( "unchecked" )
  public ITsCollection<ISkObject> getMultiLinkValue( IM5Bunch<LinkParam> aValues ) {
    LinkParam orig = aValues.originalEntity();
    boolean singleEdit = orig.getLinkInfo().linkConstraint().maxCount() == 1;
    if( singleEdit ) {
      return new ElemArrayList<>( (ISkObject)aValues.get( LinkParamM5Model.FID_SINGLE_VALUE ) );
    }

    String fieldId = LinkParamM5Model.FID_MULTI_VALUE;

    if( extraMultiValuesFields.hasKey( orig.getLinkInfo().id() ) ) {
      fieldId = extraMultiValuesFields.getByKey( orig.getLinkInfo().id() ).id();
    }

    ITsCollection<ISkObject> objs = (ITsCollection<ISkObject>)aValues.get( fieldId );
    return objs;
  }
}
