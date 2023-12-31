package org.toxsoft.skf.rri.values.gui.utils;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

public class ValedDurationTextFactory
    extends AbstractValedControlFactory {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ValedDurationTextEditor"; //$NON-NLS-1$

  protected ValedDurationTextFactory() {
    super( FACTORY_NAME );
    // TODO Auto-generated constructor stub
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
    AbstractValedControl<IAtomicValue, Control> e = new ValedDurationText( aContext );
    e.setParamIfNull( OPID_IS_WIDTH_FIXED, AV_FALSE );
    return e;
  }
}
