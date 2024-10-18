package org.toxsoft.skf.rri.struct.gui.panels;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;

/**
 * Panel to edit structure of RRI
 *
 * @author max
 */
public class PanelRriStructEditor
    extends TsPanel {

  private PanelRriSectionStructEditor sectionEditor;

  /**
   * Constructor
   * 
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelRriStructEditor( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    this.setLayout( new BorderLayout() );

    SelectRriSectionToolbarComposite toolBarComposite = new SelectRriSectionToolbarComposite( this, aContext, true ) {

      @Override
      protected void doSetRriSection( ISkRriSection aRriSection ) {
        if( sectionEditor != null ) {
          sectionEditor.setRriSection( aRriSection );
        }
      }

    };

    toolBarComposite.setLayoutData( BorderLayout.NORTH );

    sectionEditor = new PanelRriSectionStructEditor( this, aContext );

    sectionEditor.setLayoutData( BorderLayout.CENTER );

  }

}
