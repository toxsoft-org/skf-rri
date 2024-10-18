package org.toxsoft.skf.rri.struct.gui;

import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The plugin activator.
 *
 * @author max
 */
public class Activator
    extends MwsActivator {

  /**
   * The plugin ID (for Java static imports).
   */
  public static final String PLUGIN_ID = "org.toxsoft.skf.rri.struct.gui"; //$NON-NLS-1$

  private static Activator instance = null;

  /**
   * Constructor.
   */
  public Activator() {
    super( PLUGIN_ID );
    checkInstance( instance );
    instance = this;
    SkCoreUtils.registerSkServiceCreator( SkRegRefInfoService.CREATOR );
  }

  /**
   * Returns the reference to the activator singleton.
   *
   * @return {@link Activator} - the activator singleton
   */
  public static Activator getInstance() {
    return instance;
  }

}
