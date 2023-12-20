package org.toxsoft.skf.rri.struct.gui.panels;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.km5.sgw.*;

/**
 * Панель редактирования структуры (набора) параметров НСИ.
 *
 * @author goga
 */
public class PanelRriSectionStructEditor
    extends TsPanel {

  RriClassIdsPanel rriClassIdsPanel;

  PanelRriAttrLinkListsEditor listsEditor;

  final ISkConnection                      conn;
  private IM5CollectionPanel<ISkClassInfo> classesPanel;
  private ISkClassInfo                     selectedClass = null;

  private IM5CollectionPanel<IDtoAttrInfo> attrPanel;

  private AttributeLifeCycleManager alm;

  private IM5CollectionPanel<IDtoLinkInfo> linkPanel;

  private LinkLifeCycleManager llm;

  private final ITsSelectionChangeListener<ISkClassInfo> classChangeListener = ( aSource, aSelectedItem ) -> {
    this.selectedClass = aSelectedItem;
    if( selectedClass == null ) {
      return;
    }
    if( linkPanel != null ) {
      llm.setClassId( selectedClass.id() );
      linkPanel.refresh();
    }
    if( attrPanel != null ) {
      alm.setClassId( selectedClass.id() );
      attrPanel.refresh();
    }
    // if( this.skObjectPanel != null ) {
    // this.skObjectPanel.setClass( this.selectedClass );
    // }
    // if( this.skObjectCheckedListPanel != null ) {
    // this.skObjectCheckedListPanel.setClass( this.selectedClass );
    // }
    // if( this.propPanel != null ) {
    // this.propPanel.setClass( this.selectedClass );
    // }
    // if( this.rtDataCheckedListPanel != null ) {
    // this.rtDataCheckedListPanel.setClass( this.selectedClass );
    // }
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
  public PanelRriSectionStructEditor( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );

    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkClassInfo> model = m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.params().addAll( aContext.params() );
    // IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    // IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
    // avValobj( EBorderLayoutPlacement.SOUTH ) );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );

    classesPanel =
        model.panelCreator().createCollViewerPanel( ctx, model.findLifecycleManager( conn ).itemsProvider() );
    // setup
    classesPanel.addTsSelectionListener( classChangeListener );
    classesPanel.createControl( sfMain );

    SashForm rightPane = new SashForm( sfMain, SWT.VERTICAL );

    AttributeModel attrModel = (AttributeModel)m5.getModel( AttributeModel.MODEL_ID, IDtoAttrInfo.class );

    // ITsGuiContext ctx = new TsGuiContext( aContext );

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );

    ISkRriSection rriSection = rriService.findSection( "test.section" );
    if( rriSection == null ) {
      rriSection = rriService.createSection( "test.section", "Test Section", "Test Section", IOptionSet.NULL );
    }

    alm = new AttributeLifeCycleManager( ctx, attrModel, rriService );
    alm.setSectionId( rriSection.id() );

    attrPanel = attrModel.panelCreator().createCollEditPanel( ctx, alm.itemsProvider(), alm );
    attrPanel.createControl( rightPane );
    attrPanel.refresh();

    ITsGuiContext lCtx = new TsGuiContext( aContext );

    LinkModel linkModel = (LinkModel)m5.getModel( LinkModel.MODEL_ID, IDtoLinkInfo.class );
    llm = new LinkLifeCycleManager( lCtx, linkModel, rriService );
    llm.setSectionId( rriSection.id() );

    linkPanel = linkModel.panelCreator().createCollEditPanel( lCtx, llm.itemsProvider(), llm );
    linkPanel.createControl( rightPane );
    linkPanel.refresh();

    sfMain.setWeights( 4000, 6000 );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericEntityPanel
  //

  public void setRriSection( ISkRriSection aEntity ) {
    if( aEntity == null ) {
      rriClassIdsPanel.setRriSection( null );
      // TODO PanelRriSectionValuesEditor.setEntity()
      return;
    }
    ISkRriSection rriSection = aEntity;// ISkRriSection.class.cast( aEntity.entity() );
    rriClassIdsPanel.setRriSection( rriSection );
    listsEditor.setSectionId( rriSection.id() );
  }

  // @Override
  // public void onTsSelectionChanged( Object aSource, ISkClassInfo aSelectedItem ) {
  // listsEditor.setClassId( aSelectedItem.id() );
  //
  // }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducerEx
  //

  // @Override
  // public void addGenericChangeListener( IGenericChangeListener aListener ) {
  // genericChangeEventHelper.addGenericChangeListener( aListener );
  // }
  //
  // @Override
  // public void removeGenericChangeListener( IGenericChangeListener aListener ) {
  // genericChangeEventHelper.removeGenericChangeListener( aListener );
  // }
  //
  // @Override
  // public void pauseFiring() {
  // genericChangeEventHelper.pauseFiring();
  // }
  //
  // @Override
  // public void resumeFiring( boolean aFireDelayed ) {
  // genericChangeEventHelper.resumeFiring( aFireDelayed );
  // }

}
