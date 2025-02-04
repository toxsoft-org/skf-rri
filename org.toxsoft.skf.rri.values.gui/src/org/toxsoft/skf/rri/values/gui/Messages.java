package org.toxsoft.skf.rri.values.gui;

import org.eclipse.osgi.util.*;

public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
  public static String        STR_ABILITY_ACCESS_RRI_VALUES_EDITOR;
  public static String        STR_ABILITY_ACCESS_RRI_VALUES_EDITOR_D;
  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
