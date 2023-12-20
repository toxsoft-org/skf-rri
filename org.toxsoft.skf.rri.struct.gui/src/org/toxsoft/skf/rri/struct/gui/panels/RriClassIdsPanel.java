package org.toxsoft.skf.rri.struct.gui.panels;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Панель списка идентификаторов классов, предоставляемый разделм НСИ {@link ISkRriSection#listClassIds()}.
 *
 * @author goga
 */
class RriClassIdsPanel
    extends TsPanel {

  private static final EIconSize TOOLBAR_CION_SIZE = EIconSize.IS_24X24;

  // private static final INameProvider<ISkClassInfo> NAME_PROVIDER = new INameProvider<>() {
  //
  // @Override
  // public String getItemName( ISkClassInfo aItem ) {
  // return StridUtils.printf( StridUtils.FORMAT_ID_NAME, aItem );
  // }
  // };

  ISkConnection skConn;

  // private final HdrToolbar toolbar;
  // private final ITsguiList<ISkClassInfo> listWidget;

  private ISkRriSection rriSection = null;

  public RriClassIdsPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    // toolbar
    // toolbar = new HdrToolbar( this, TBNAME_CLASSES, TOOLBAR_CION_SIZE, SA_ADD, SA_REMOVE );
    // toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    // // listWidget
    // listWidget = new TsguiList<>( panelContext() );
    // listWidget.createControl( this );
    // listWidget.getControl().setLayoutData( BorderLayout.CENTER );
    // listWidget.setNameProvider( NAME_PROVIDER );
    // // setup
    // toolbar.addToolbarListener( this );
    // listWidget.addTsDoubleClickListener( doubleClickEventHelper );
    // listWidget.addTsSelectionListener( new ITsSelectionChangeListener<ISkClassInfo>() {
    //
    // @Override
    // public void onTsSelectionChanged( Object aSource, ISkClassInfo aSelectedItem ) {
    // updateActionsState();
    // }
    // } );
    // listWidget.addTsSelectionListener( selectionChangeEventHelper );
    // updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  void reinitContent() {
    // if( rriSection != null ) {
    // IListEdit<ISkClassInfo> ll = new ElemArrayList<>();
    // for( String classId : rriSection.listClassIds() ) {
    // ISkClassInfo cinf = skCim().getClassInfo( classId );
    // ll.add( cinf );
    // }
    // listWidget.setItems( ll );
    // }
    // else {
    // listWidget.setItems( IList.EMPTY );
    // }
    updateActionsState();
  }

  void updateActionsState() {
    boolean isAlive = rriSection != null;
    // boolean isSel = listWidget.selectedItem() != null;
    // toolbar.setActionEnabled( SAID_ADD, isAlive );
    // toolbar.setActionEnabled( SAID_REMOVE, isAlive && isSel );
    // toolbar.setActionEnabled( SAID_CLEAR, isAlive );
  }

  boolean isClassInSection( String aClassId ) {
    // for( ISkClassInfo cinf : listWidget.items() ) {
    // if( cinf.id().equals( aClassId ) ) {
    // return true;
    // }
    // }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IHdrToolbarListener
  //

  // @Override
  public void onButtonPressed( int aActionId ) {
    // if( !toolbar.isActionEnabled( aActionId ) ) {
    // return;
    // }
    // ISkClassInfo sel = listWidget.selectedItem();
    // switch( aActionId ) {
    // case SAID_ADD: {
    // // подготовим список классов, которые могут быть добавлены в НСИ
    // IListEdit<ISkClassInfo> ll = new ElemArrayList<>();
    // for( ISkClassInfo cinf : skCim().listClasses() ) {
    // // пропустим классы, которые уже в разделе НСИ
    // if( isClassInSection( cinf.id() ) ) {
    // continue;
    // }
    // // пропустим корневой класс
    // if( cinf.id().equals( IGwHardConstants.GW_ROOT_CLASS_ID ) ) {
    // continue;
    // }
    // // пропустим служебные классы
    // String ownerServiceId = skCim().getClassOwnerService( cinf.id() );
    // if( !ownerServiceId.equals( ISkSysdescr.SERVICE_ID ) ) {
    // continue;
    // }
    // ll.add( cinf );
    // }
    // // неяего добавлять
    // if( ll.isEmpty() ) {
    // TsDialogUtils.warn( getShell(), FMT_ASK_REMOVE_RRI_CLASS );
    // break;
    // }
    // // TODO select class from list and add
    // ICommonDialogInfo cdi = CommonDialogInfo.cdiSelectObjDialog( getShell() );
    // ISkClassInfo cinf = DialogItemSelectionListPanel.select( cdi, ll, null, NAME_PROVIDER );
    // if( cinf == null ) {
    // break;
    // }
    //
    // FIXME просто так класс не добавить - тут надо предложить создать атриубт или связь этого класса
    // DEBUG ---
    // TsDialogUtils.underDevelopment( getShell() );
    // --- DEBUG
    //

    // EditListsDialog d = new EditListsDialog( getShell(), panelContext(), getRriSection().id(), cinf.id() );
    // d.open();
    // reinitContent();
    // listWidget.setSelectedItem( cinf );
    // break;
    // }
    // case SAID_REMOVE: {
    // if( TsDialogUtils.askYesNoCancel( getShell(), FMT_ASK_REMOVE_RRI_CLASS,
    // NAME_PROVIDER.getItemName( sel ) ) == ETsDialogCode.YES ) {
    // int index = listWidget.items().indexOf( sel );
    // rriSection.removeAll( sel.id() );
    // reinitContent();
    // if( index >= listWidget.items().size() ) {
    // --index;
    // }
    // if( index >= 0 ) {
    // ISkClassInfo toSel = listWidget.items().get( index );
    // listWidget.setSelectedItem( toSel );
    // }
    // }
    // break;
    // }
    // default:
    // throw new TsNotAllEnumsUsedRtException();
    // }
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  void setRriSection( ISkRriSection aSection ) {
    rriSection = aSection;
    reinitContent();
  }

  public ISkRriSection getRriSection() {
    return rriSection;
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractTsE4StdEventsProducerPanel
  //

  public ISkClassInfo selectedItem() {
    return null;// listWidget.selectedItem();
  }

  public void setSelectedItem( ISkClassInfo aItem ) {
    // listWidget.setSelectedItem( aItem );
  }

}
