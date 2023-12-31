package org.toxsoft.skf.rri.values.gui.panels;

import org.eclipse.osgi.util.*;

public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "org.toxsoft.skf.rri.values.gui.panels.messages"; //$NON-NLS-1$
  public static String        ATTRS_TAB_NAME;
  public static String        LINKS_TAB_NAME;
  public static String        TREE_MODE_NAME;
  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
