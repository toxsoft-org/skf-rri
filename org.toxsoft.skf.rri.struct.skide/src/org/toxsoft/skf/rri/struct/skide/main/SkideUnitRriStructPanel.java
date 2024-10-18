package org.toxsoft.skf.rri.struct.skide.main;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.skf.rri.struct.gui.panels.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link AbstractSkideUnitPanel} implementation: rri struct editor panel
 *
 * @author max
 */
class SkideUnitRriStructPanel
    extends AbstractSkideUnitPanel {

  public SkideUnitRriStructPanel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsGuiContext reportContext = new TsGuiContext( tsContext() );

    PanelRriStructEditor panel = new PanelRriStructEditor( aParent, reportContext );
    panel.setLayoutData( BorderLayout.CENTER );
    return panel;
  }

}
