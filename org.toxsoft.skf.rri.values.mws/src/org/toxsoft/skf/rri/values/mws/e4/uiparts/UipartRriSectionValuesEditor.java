package org.toxsoft.skf.rri.values.mws.e4.uiparts;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.values.gui.panels.*;
import org.toxsoft.skf.rri.values.mws.e4.services.*;
import org.toxsoft.uskat.core.gui.e4.uiparts.*;

/**
 * UIpart to display editable tables of the specified rri section values.
 *
 * @author max
 */
public class UipartRriSectionValuesEditor
    extends SkMwsAbstractPart {

  private Composite bkPanel;

  // ------------------------------------------------------------------------------------
  // SkMwsAbstractPart
  //
  @Override
  protected void doCreateContent( TsComposite aParent ) {
    bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayout( new BorderLayout() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the rri section to edit items.
   * <p>
   * Method is called from {@link WsRriSectionsManagementService#showRriSectionUipart(ISkRriSection)}.
   *
   * @param aRriSection {@link ISkRriSection} - the rri section
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */

  public void setRriSection( ISkRriSection aRriSection ) {
    TsNullArgumentRtException.checkNull( aRriSection );
    // создаем панель редактирования содержимого раздела
    PanelRriSectionValuesEditor sectionPanel = new PanelRriSectionValuesEditor( bkPanel, tsContext() );
    sectionPanel.setRriSection( aRriSection );
    sectionPanel.setLayoutData( BorderLayout.CENTER );
  }

}
