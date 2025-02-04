package org.toxsoft.skf.rri.values.gui.panels;

import static org.toxsoft.skf.rri.values.gui.panels.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.skf.rri.values.gui.*;
import org.toxsoft.skf.rri.values.gui.km5.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.km5.sgw.*;

/**
 * Editor panel of rri section values.
 *
 * @author max
 */
public class PanelRriSectionValuesEditor
    extends TsPanel {

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

  private ITsGuiContext context;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param abContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelRriSectionValuesEditor( Composite aParent, ITsGuiContext abContext ) {
    super( aParent, abContext );

    // ITsGuiContext ctx = new TsGuiContext( aContext );
    // ctx.params().addAll( aContext.params() );

    this.setLayout( new BorderLayout() );

    ISkConnectionSupplier connSup = abContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    final IM5Domain m5 = conn.scope().get( IM5Domain.class );

    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );

    context = m5.tsContext();

    // IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    // IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
    // avValobj( EBorderLayoutPlacement.SOUTH ) );

    TsGuiContext clsCtx = new TsGuiContext( context );

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
    classComponentModown.toolbar().setNameLabelText( STR_RRI_CLASSES );
    classesPanel.refresh();

    SashForm rightPane = new SashForm( sfMain, SWT.VERTICAL );

    IM5Model<ISkObject> objModel = m5.getModel( ObjectCheckableM5Model.MODEL_ID, ISkObject.class );

    // Менеджер ЖЦ списка объектов.
    TsGuiContext checkCtx = new TsGuiContext( context );
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

    TsGuiContext attrCtx = new TsGuiContext( context );

    IM5Model<AttrParam> attrModel = m5.getModel( AttrParamM5Model.MODEL_ID, AttrParam.class );

    attrLm = (AttrParamM5LifeCycleManager)attrModel.getLifecycleManager( null );

    // dima 09.01.25 check ability to edit and tune M5 panels accordingly
    // FIXME max по хорошему здесь нужно использовать createCollViewerPanel() вместо createCollEditPanel() если
    // редактирование запрещено, но в коде используется AttrParamM5LifeCycleManager и быстро переделать не получается
    ISkAbility canEdit =
        conn.coreApi().userService().abilityManager().findAbility( ISkRriServiceHardConstants.ABILITY_EDIT_RRI.id() );
    if( !conn.coreApi().userService().abilityManager().isAbilityAllowed( canEdit.id() ) ) {
      // turn off editing
      IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( attrCtx.params(), AvUtils.AV_FALSE );
      IMultiPaneComponentConstants.OPDEF_DBLCLICK_ACTION_ID.setValue( attrCtx.params(), AvUtils.AV_STR_EMPTY );
    }
    else {
      // turn on editing
      IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( attrCtx.params(), AvUtils.AV_TRUE );
    }

    attrsListPanel = attrModel.panelCreator().createCollEditPanel( attrCtx, attrLm.itemsProvider(), attrLm );

    attrItem.setControl( attrsListPanel.createControl( paramsFolder ) );
    attrsListPanel.refresh();

    TabItem linkItem = new TabItem( paramsFolder, SWT.NONE );
    linkItem.setText( LINKS_TAB_NAME );

    TsGuiContext lnkCtx = new TsGuiContext( context );
    LinkParamM5Model linkModel = (LinkParamM5Model)m5.getModel( LinkParamM5Model.MODEL_ID, LinkParam.class );

    linkLm = (LinkParamM5LifeCycleManager)linkModel.getLifecycleManager( null );

    // dima 09.01.25 check ability to edit and tune M5 panels accordingly
    if( !conn.coreApi().userService().abilityManager().isAbilityAllowed( canEdit.id() ) ) {
      // turn off editing
      IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( lnkCtx.params(), AvUtils.AV_FALSE );
      IMultiPaneComponentConstants.OPDEF_DBLCLICK_ACTION_ID.setValue( lnkCtx.params(), AvUtils.AV_STR_EMPTY );
    }
    else {
      // turn on editing
      IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( lnkCtx.params(), AvUtils.AV_TRUE );
    }

    linksListPanel = linkModel.panelCreator().createCollEditPanel( lnkCtx, linkLm.itemsProvider(), linkLm );

    linkItem.setControl( linksListPanel.createControl( paramsFolder ) );
    linksListPanel.refresh();

    sfMain.setWeights( 4000, 6000 );

  }

  /**
   * Sets RRI section for editing values of its objects.
   *
   * @param aRriSection ISkRriSection - RRI section for editing values of its objects.
   */
  public void setRriSection( ISkRriSection aRriSection ) {
    String sectionId = aRriSection != null ? aRriSection.id() : TsLibUtils.EMPTY_STRING;
    context.put( IRegRefInfoConstants.REG_REF_INFO_DEFAULT_SECTION_ID.id(), sectionId );
    if( clm == null ) {
      return;
    }
    clm.setSectionId( sectionId );

    objLm.setClassIds( IStringList.EMPTY );
    objectsListPanel.refresh();

    classesPanel.refresh();
    attrsListPanel.refresh();
    linksListPanel.refresh();
  }

}
