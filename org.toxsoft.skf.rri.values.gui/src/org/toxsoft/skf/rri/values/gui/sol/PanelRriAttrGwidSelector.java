package org.toxsoft.skf.rri.values.gui.sol;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель выбора идентификатора sk-сущности в разделе НСИ.
 * <p>
 *
 * @author vs
 */
public class PanelRriAttrGwidSelector
    extends AbstractTsDialogPanel<RriId, ITsGuiContext> {

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelRriAttrGwidSelector( Composite aParent, TsDialog<RriId, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init( aParent );
  }

  TableViewer viewer;

  void init( Composite aParent ) {
    viewer = new TableViewer( aParent, SWT.BORDER | SWT.FULL_SELECTION );
    viewer.getTable().setHeaderVisible( true );
    viewer.getTable().setLinesVisible( true );

    TableViewerColumn columnId = new TableViewerColumn( viewer, SWT.NONE );
    columnId.getColumn().setWidth( 120 );
    columnId.getColumn().setText( "ИД" );

    columnId.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ISkRriSection section = (ISkRriSection)aCell.getElement();
        aCell.setText( section.id() );
      }
    } );

    TableViewerColumn columnName = new TableViewerColumn( viewer, SWT.NONE );
    columnName.getColumn().setWidth( 200 );
    columnName.getColumn().setText( "Имя" );

    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ISkRriSection section = (ISkRriSection)aCell.getElement();
        String name = IAvMetaConstants.DDEF_NAME.getValue( section.params() ).asString();
        aCell.setText( name );
      }
    } );

    viewer.setContentProvider( new ArrayContentProvider() );
    ISkCoreApi coreApi = environ().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkRegRefInfoService rriServ = coreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    viewer.setInput( rriServ.listSections().toArray() );
  }

  @Override
  protected void doSetDataRecord( RriId aRriId ) {
    if( aRriId != null ) {
      ISkCoreApi coreApi = environ().get( ISkConnectionSupplier.class ).defConn().coreApi();
      ISkRegRefInfoService rriServ = coreApi.getService( ISkRegRefInfoService.SERVICE_ID );
      // ISkRriSection section = rriServ.listSections().findByKey( aString );
      // if( section != null ) {
      // attrsViewer.setSelection( new StructuredSelection( section ) );
      // }
    }
  }

  @Override
  protected RriId doGetDataRecord() {
    IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
    if( !selection.isEmpty() ) {
      // return ((ISkRriSection)selection.getFirstElement()).id();
    }
    return null;
  }

  /**
   * Выводит диалог выбора String.
   * <p>
   *
   * @param aRriId {@link String} для инициализации
   * @param aContext {@link ITsGuiContext} - контекст
   * @return {@link String} - выбранный параметр <b>null</b> в случает отказа от редактирования
   */
  public static final RriId selectRriSectionId( RriId aRriId, ITsGuiContext aContext ) {
    IDialogPanelCreator<RriId, ITsGuiContext> creator = PanelRriAttrGwidSelector::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "DLG_String_SELECTOR", "DLG_String_SELECTOR_D" );
    TsDialog<RriId, ITsGuiContext> d = new TsDialog<>( dlgInfo, aRriId, aContext, creator );
    return d.execData();
  }

  // @Override
  // protected ValidationResult doValidate() {
  // // check selected object
  // if( skObjectsPanel.getSelectedObj() == null ) {
  // return ValidationResult.error( MSG_ERR_NO_OBJ_SELECTED );
  // }
  // // check selected prop
  // if( propPanel.getSelectedProp() == null ) {
  // return ValidationResult.error( MSG_ERR_NO_PROP_SELECTED );
  // }
  // return ValidationResult.SUCCESS;
  // }

}
