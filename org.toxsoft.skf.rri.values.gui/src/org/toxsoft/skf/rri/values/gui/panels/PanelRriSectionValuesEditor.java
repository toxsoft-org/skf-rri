package org.toxsoft.skf.rri.values.gui.panels;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.rri.values.gui.panels.ITsResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.values.gui.*;
import org.toxsoft.skf.rri.values.gui.km5.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.km5.sgw.*;

/**
 * Панель редактирования структуры (набора) параметров НСИ.
 *
 * @author goga
 */
public class PanelRriSectionValuesEditor
    extends TsPanel {

  final static String ACTID_RRI_SECTION_SELECT = SK_ID + "rri.struct.edit.section.select"; //$NON-NLS-1$

  final static TsActionDef ACDEF_RRI_SECTION_SELECT = TsActionDef.ofPush2( ACTID_RRI_SECTION_SELECT,
      STR_N_SELECT_RRI_SECTION, STR_D_SELECT_RRI_SECTION, ICONID_VIEW_AS_LIST );

  private TextControlContribution textContr1;

  final ISkConnection conn;

  private ISkClassInfo selectedClass = null;

  private RriClassInfoLifeCycleManager clm;

  private IM5CollectionPanel<ISkClassInfo> classesPanel;

  /**
   * Панель списка объектов
   */
  private IM5CollectionPanel<ISkObject> objectsListPanel = null;

  private ObjectsM5LifeCycleManager objLm = null;

  /**
   * Панель редактора атрибутов
   */
  private IM5CollectionPanel<AttrParam> attrsListPanel = null;

  private AttrParamM5LifeCycleManager attrLm;

  /**
   * Динамическая панель редатора связей
   */
  private IM5CollectionPanel<LinkParam> linksListPanel = null;

  private LinkParamM5LifeCycleManager linkLm = null;

  // private IM5CollectionPanel<IDtoAttrInfo> attrPanel;

  // private AttributeLifeCycleManager alm;

  // private IM5CollectionPanel<IDtoLinkInfo> linkPanel;

  // private LinkLifeCycleManager llm;

  private final ITsSelectionChangeListener<ISkClassInfo> classChangeListener = ( aSource, aSelectedItem ) -> {
    this.selectedClass = aSelectedItem;
    if( selectedClass == null ) {
      return;
    }
    if( objectsListPanel != null ) {
      objLm.setClassIds( new SingleStringList( selectedClass.id() ) );
      objectsListPanel.refresh();
    }
    if( attrsListPanel != null ) {
      attrLm.setClassInfo( selectedClass );
      attrsListPanel.refresh();
    }
    if( linksListPanel != null ) {
      linkLm.setClassInfo( selectedClass );
      linksListPanel.refresh();
    }

  };

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelRriSectionValuesEditor( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    // ITsGuiContext ctx = new TsGuiContext( aContext );
    // ctx.params().addAll( aContext.params() );

    this.setLayout( new BorderLayout() );

    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    final IM5Domain m5 = conn.scope().get( IM5Domain.class );

    TsComposite frame = new TsComposite( this );
    frame.setLayout( new BorderLayout() );

    TsToolbar toolBar = new TsToolbar( aContext );
    toolBar.setIconSize( EIconSize.IS_24X24 );

    // toolBar.addActionDef( ACDEF_EDIT );
    toolBar.addActionDef( ACDEF_RRI_SECTION_SELECT );

    toolBar.addSeparator();

    Control toolbarCtrl = toolBar.createControl( frame );
    toolbarCtrl.setLayoutData( BorderLayout.NORTH );

    textContr1 = new TextControlContribution( "Label", 300, "Раздел НСИ:", SWT.NONE ); //$NON-NLS-1$
    toolBar.addContributionItem( textContr1 );

    ITsGuiContext rriSecCtx = new TsGuiContext( aContext );

    toolBar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_RRI_SECTION_SELECT.id() ) ) {
        selectRriSection( rriSecCtx );
        return;
      }
    } );

    SashForm sfMain = new SashForm( frame, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );

    // IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    // IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
    // avValobj( EBorderLayoutPlacement.SOUTH ) );

    TsGuiContext clsCtx = new TsGuiContext( aContext );

    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( clsCtx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( clsCtx.params(), AvUtils.AV_FALSE );

    IM5Model<ISkClassInfo> clsModel = m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );
    clm = new RriClassInfoLifeCycleManager( clsModel, conn.coreApi() );

    // AttributeModel attrModel = (AttributeModel)m5.getModel( AttributeModel.MODEL_ID, IDtoAttrInfo.class );
    // ISkRegRefInfoService rriService =
    // (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );
    // alm = new AttributeLifeCycleManager( ctx, attrModel, rriService );
    // -----------------------------------

    MultiPaneComponentModown<ISkClassInfo> classComponentModown =
        new MultiPaneComponentModown<>( clsCtx, clsModel, clm.itemsProvider(), clm );
    classesPanel = new M5CollectionPanelMpcModownWrapper<>( classComponentModown, true );
    // ----------------------------------
    classesPanel.addTsSelectionListener( classChangeListener );
    classesPanel.createControl( sfMain );
    classComponentModown.toolbar().setNameLabelText( "Классы НСИ: " );
    classesPanel.refresh();

    SashForm rightPane = new SashForm( sfMain, SWT.VERTICAL );

    IM5Model<ISkObject> objModel = m5.getModel( ObjectCheckableM5Model.MODEL_ID, ISkObject.class );

    // Менеджер ЖЦ списка объектов.
    TsGuiContext checkCtx = new TsGuiContext( aContext );
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( checkCtx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_TOOLBAR_NAME.setValue( checkCtx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( checkCtx.params(), AvUtils.AV_FALSE );
    objLm = new ObjectsM5LifeCycleManager( checkCtx, objModel, conn.coreApi() );

    // Создание панели
    objectsListPanel = objModel.panelCreator().createCollChecksPanel( checkCtx, objLm.itemsProvider() );
    objectsListPanel.setLifecycleManager( objLm );
    objectsListPanel.createControl( rightPane );
    objectsListPanel.refresh();
    // appContext().set( "objectsPanel", objectsListPanel );

    // Слушатель изменений выбора объектов
    objectsListPanel.checkSupport().checksChangeEventer().addListener(

        aSource -> {
          // appContext().set( "selObject", objectsListPanel.selectedItem() );
          IList<ISkObject> checkedObjects = objectsListPanel.checkSupport().listCheckedItems( true );

          ((AttrParamM5LifeCycleManager)attrsListPanel.lifecycleManager()).setObjects( checkedObjects );
          attrsListPanel.refresh();

          if( linksListPanel != null ) {
            ((LinkParamM5LifeCycleManager)linksListPanel.lifecycleManager()).setObjects( checkedObjects );
            linksListPanel.refresh();
          }
        } );

    // ITsGuiContext ctx = new TsGuiContext( aContext );

    // alm.setSectionId( rriSection.id() );

    // attrPanel = attrModel.panelCreator().createCollEditPanel( ctx, alm.itemsProvider(), alm );
    // attrPanel.createControl( rightPane );
    // attrPanel.refresh();
    // new Label( rightPane, 0 );
    //
    // ITsGuiContext lCtx = new TsGuiContext( aContext );
    //
    // LinkModel linkModel = (LinkModel)m5.getModel( LinkModel.MODEL_ID, IDtoLinkInfo.class );
    // llm = new LinkLifeCycleManager( lCtx, linkModel, rriService );
    // llm.setSectionId( rriSection.id() );

    // linkPanel = linkModel.panelCreator().createCollEditPanel( lCtx, llm.itemsProvider(), llm );
    // linkPanel.createControl( rightPane );
    // linkPanel.refresh();
    TabFolder paramsFolder = new TabFolder( rightPane, SWT.NONE );

    TabItem attrItem = new TabItem( paramsFolder, SWT.NONE );
    attrItem.setText( ATTRS_TAB_NAME );

    TsGuiContext attrCtx = new TsGuiContext( aContext );
    IM5Model<AttrParam> attrModel = m5.getModel( AttrParamM5Model.MODEL_ID, AttrParam.class );

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );
    attrLm = new AttrParamM5LifeCycleManager( attrCtx, attrModel, rriService );

    attrsListPanel = attrModel.panelCreator().createCollEditPanel( attrCtx, attrLm.itemsProvider(), attrLm );

    attrItem.setControl( attrsListPanel.createControl( paramsFolder ) );
    attrsListPanel.refresh();

    TabItem linkItem = new TabItem( paramsFolder, SWT.NONE );
    linkItem.setText( LINKS_TAB_NAME );

    TsGuiContext lnkCtx = new TsGuiContext( aContext );
    LinkParamM5Model linkModel = (LinkParamM5Model)m5.getModel( LinkParamM5Model.MODEL_ID, LinkParam.class );

    linkLm = new LinkParamM5LifeCycleManager( lnkCtx, linkModel, rriService );

    linksListPanel = linkModel.panelCreator().createCollEditPanel( lnkCtx, linkLm.itemsProvider(), linkLm );

    linkItem.setControl( linksListPanel.createControl( paramsFolder ) );
    linksListPanel.refresh();

    sfMain.setWeights( 4000, 6000 );
    setRriSection( null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericEntityPanel
  //

  private void setRriSection( ISkRriSection aEntity ) {
    String sectionId = aEntity != null ? aEntity.id() : TsLibUtils.EMPTY_STRING;
    clm.setSectionId( sectionId );
    tsContext().put( IRegRefInfoConstants.REG_REF_INFO_DEFAULT_SECTION_ID.id(), sectionId );
    // alm.setSectionId( sectionId );
    // alm.setClassId( null );
    // llm.setSectionId( sectionId );
    // llm.setClassId( null );

    objLm.setClassIds( IStringList.EMPTY );
    objectsListPanel.refresh();

    classesPanel.refresh();
    attrsListPanel.refresh();
    linksListPanel.refresh();

    textContr1.setText( "Раздел НСИ: " + (aEntity != null ? aEntity.id() : "не выбран") );
  }

  private void selectRriSection( ITsGuiContext aContext ) {
    // select the section

    IM5Domain m5 = conn.scope().get( IM5Domain.class );

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );

    IM5Model<ISkRriSection> model = m5.getModel( RriSectionModel.MODEL_ID, ISkRriSection.class );
    IM5LifecycleManager<ISkRriSection> lm = model.getLifecycleManager( rriService );
    TsDialogInfo di = new TsDialogInfo( aContext, "Выбор раздела НСИ", "Выбор раздела НСИ" );
    // установим нормальный размер диалога
    di.setMinSize( new TsPoint( -30, -40 ) );
    ISkRriSection section = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), lm );
    if( section != null ) {
      setRriSection( section );
    }
    else {
      if( clm.getSectionId() != null ) {
        if( !rriService.listSections().hasKey( clm.getSectionId() ) ) {
          setRriSection( null );
        }
      }
    }
  }

  // private void editRriSectionList( ITsGuiContext aContext ) {
  // // edit the sections
  //
  // IM5Domain m5 = conn.scope().get( IM5Domain.class );
  // ISkRegRefInfoService rriService =
  // (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );
  //
  // IM5Model<ISkRriSection> model = m5.getModel( RriSectionModel.MODEL_ID, ISkRriSection.class );
  // ITsDialogInfo di =
  // new TsDialogInfo( aContext, "Редактирование списка разделов НСИ", "Редактирование списка разделов НСИ" );
  // IM5LifecycleManager<ISkRriSection> lm = model.getLifecycleManager( rriService );
  // M5GuiUtils.editModownColl( aContext, model, di, lm );
  // }

  static class TextControlContribution
      extends ControlContribution {

    private final int width;
    private final int swtStyle;
    private String    text;
    CLabel            label;

    /**
     * Конструктор.
     *
     * @param aId String - ИД элемента
     * @param aWidth int - ширина текстового поля
     * @param aText String - текст
     * @param aSwtStyle int - swt стиль
     */
    public TextControlContribution( String aId, int aWidth, String aText, int aSwtStyle ) {
      super( aId );
      width = aWidth;
      swtStyle = aSwtStyle;
      text = aText;
    }

    // ------------------------------------------------------------------------------------
    // ControlContribution
    //

    @Override
    protected Control createControl( Composite aParent ) {
      label = new CLabel( aParent, swtStyle );
      label.setText( text );
      label.setAlignment( SWT.LEFT );
      return label;
    }

    @Override
    protected int computeWidth( Control aControl ) {
      if( width == SWT.DEFAULT ) {
        return super.computeWidth( aControl );
      }
      return width;
    }

    // ------------------------------------------------------------------------------------
    // API
    //

    /**
     * Возвращает текстовое поле.
     *
     * @return CLabel - текстовое поле
     */
    public CLabel label() {
      return label;
    }

    void setText( String aText ) {
      label.setText( aText );
      label.redraw();
    }

  }

}
