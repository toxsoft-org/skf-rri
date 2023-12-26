package org.toxsoft.skf.rri.struct.skide.main;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link AbstractSkideUnitPanel} implementation.
 *
 * @author hazard157
 */
class SkideUnit2Panel
    extends AbstractSkideUnitPanel {

  public SkideUnit2Panel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    CLabel label = new CLabel( aParent, SWT.CENTER );
    label.setText( "Unit template 2: right panel" ); //$NON-NLS-1$
    return label;
  }

}
