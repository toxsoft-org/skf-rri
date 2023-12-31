package org.toxsoft.skf.rri.struct.skide.main;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.skf.rri.values.gui.panels.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link AbstractSkideUnitPanel} implementation: rri values editor panel
 *
 * @author max
 */
class SkideUnitRriValuesPanel
    extends AbstractSkideUnitPanel {

  public SkideUnitRriValuesPanel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsGuiContext reportContext = new TsGuiContext( tsContext() );

    PanelRriSectionValuesEditor panel = new PanelRriSectionValuesEditor( aParent, reportContext );
    panel.setLayoutData( BorderLayout.CENTER );
    return panel;
  }

}
