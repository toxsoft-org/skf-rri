package org.toxsoft.skf.rri.values.gui.sol;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель выбора идентификатора sk-сущности в разделе НСИ.
 * <p>
 *
 * @author vs
 */
public class PanelRriSectionIdSelector
    extends AbstractTsDialogPanel<String, ITsGuiContext> {

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelRriSectionIdSelector( Composite aParent, TsDialog<String, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init( aParent );
  }

  StridableTableViewer viewer;

  void init( Composite aParent ) {
    int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL;
    viewer = new StridableTableViewer( aParent, style, 80, 200, 400 );
    ISkCoreApi coreApi = environ().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkRegRefInfoService rriServ = coreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    viewer.viewer().setInput( rriServ.listSections().toArray() );
    viewer.viewer().addSelectionChangedListener( aEvent -> fireContentChangeEvent() );
  }

  @Override
  protected void doSetDataRecord( String aString ) {
    if( aString != null ) {
      ISkCoreApi coreApi = environ().get( ISkConnectionSupplier.class ).defConn().coreApi();
      ISkRegRefInfoService rriServ = coreApi.getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection section = rriServ.listSections().findByKey( aString );
      if( section != null ) {
        viewer.viewer().setSelection( new StructuredSelection( section ) );
      }
    }
  }

  @Override
  protected String doGetDataRecord() {
    IStructuredSelection selection = (IStructuredSelection)viewer.viewer().getSelection();
    if( !selection.isEmpty() ) {
      return ((ISkRriSection)selection.getFirstElement()).id();
    }
    return null;
  }

  @Override
  protected ValidationResult doValidate() {
    // check selected section
    if( viewer.viewer().getSelection().isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать секцию" );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Выводит диалог выбора String.
   * <p>
   *
   * @param aString {@link String} для инициализации
   * @param aContext {@link ITsGuiContext} - контекст
   * @return {@link String} - выбранный параметр <b>null</b> в случает отказа от редактирования
   */
  public static final String selectRriSectionId( String aString, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<String, ITsGuiContext> creator = PanelRriSectionIdSelector::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "DLG_String_SELECTOR", "DLG_String_SELECTOR_D" );
    TsDialog<String, ITsGuiContext> d = new TsDialog<>( dlgInfo, aString, aContext, creator );
    return d.execData();
  }

}
