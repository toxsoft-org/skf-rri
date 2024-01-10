package org.toxsoft.skf.rri.values.gui.panels;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.struct.gui.panels.*;

/**
 * Панель редактирования значений параметров НСИ.
 *
 * @author max
 */
public class PanelRriValuesEditor
    extends TsPanel {

  private PanelRriSectionValuesEditor sectionEditor;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelRriValuesEditor( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    // ITsGuiContext ctx = new TsGuiContext( aContext );
    // ctx.params().addAll( aContext.params() );

    this.setLayout( new BorderLayout() );

    SelectRriSectionToolbarComposite toolBarComposite = new SelectRriSectionToolbarComposite( this, aContext, false ) {

      @Override
      protected void doSetRriSection( ISkRriSection aRriSection ) {
        if( sectionEditor != null ) {
          sectionEditor.setRriSection( aRriSection );
        }
      }

    };

    toolBarComposite.setLayoutData( BorderLayout.NORTH );

    sectionEditor = new PanelRriSectionValuesEditor( this, aContext );

    sectionEditor.setLayoutData( BorderLayout.CENTER );

  }

}
