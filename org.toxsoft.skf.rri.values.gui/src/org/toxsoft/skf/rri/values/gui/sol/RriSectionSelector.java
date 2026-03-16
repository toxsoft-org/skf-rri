package org.toxsoft.skf.rri.values.gui.sol;

import static org.toxsoft.skf.rri.values.gui.sol.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель выбора НСИ секции.
 *
 * @author vs
 */
public class RriSectionSelector
    extends TsPanel {

  ISkRegRefInfoService rriServ    = null;
  ISkCoreApi           coreApi    = null;
  ISkRriSection        rriSection = null;

  private Text   fldSectionId;
  private Button btnBrowseSection;

  private final GenericChangeEventer eventer;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aRriSectionId String - ИД НСИ секции
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   */
  public RriSectionSelector( Composite aParent, String aRriSectionId, ITsGuiContext aContext ) {
    super( aParent, aContext );
    coreApi = aContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
    rriServ = coreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    init( this );
    if( aRriSectionId != null && !aRriSectionId.isBlank() ) {
      fldSectionId.setText( aRriSectionId );
      rriSection = rriServ.findSection( aRriSectionId );
    }
    eventer = new GenericChangeEventer( this );
  }

  /**
   * Возвращает НСИ секцию или <code>null</code> если секция не выбрана.
   *
   * @return {@link ISkRriSection} - НСИ секция или <code>null</code> если секция не выбрана
   */
  public ISkRriSection rriSection() {
    return rriSection;
  }

  /**
   * Возвращает ИД НСИ секции или <code>null</code> если секция не выбрана.
   *
   * @return String - ИД НСИ секции или <code>null</code> если секция не выбрана
   */
  public String sectionId() {
    if( rriSection != null ) {
      return rriSection.id();
    }
    return null;
  }

  void init( Composite aParent ) {
    GridLayout gl = new GridLayout( 3, false );
    aParent.setLayout( gl );

    CLabel l = new CLabel( aParent, SWT.CENTER );
    l.setText( STR_L_RRI_SECTION );

    fldSectionId = new Text( aParent, SWT.BORDER );
    fldSectionId.setEditable( false );
    fldSectionId.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    btnBrowseSection = new Button( aParent, SWT.PUSH );
    btnBrowseSection.setText( "..." ); //$NON-NLS-1$
    btnBrowseSection.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        String sectionId = PanelRriSectionIdSelector.selectRriSectionId( fldSectionId.getText(), tsContext() );
        if( sectionId != null ) {
          fldSectionId.setText( sectionId );
          rriSection = rriServ.findSection( sectionId );
          eventer.fireChangeEvent();
        }
      }
    } );
  }

  /**
   * Возвращает помощник для работы с событиями типа {@link IGenericChangeEventer}.
   *
   * @return {@link IGenericChangeEventer} - помощник для работы с событиями типа {@link IGenericChangeEventer}
   */
  public IGenericChangeEventer eventer() {
    return eventer;
  }

}
