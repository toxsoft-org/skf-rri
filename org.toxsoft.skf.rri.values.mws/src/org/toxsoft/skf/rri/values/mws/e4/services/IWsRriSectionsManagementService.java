package org.toxsoft.skf.rri.values.mws.e4.services;

import org.toxsoft.skf.rri.lib.*;

/**
 * Manages the rri sections perspecive logic, this is something like "perspective controller".
 *
 * @author max
 */
public interface IWsRriSectionsManagementService {

  /**
   * Opens new or activates already open UIpart with the specified rri section.
   * <p>
   * <code>null</code> argument is ignored.
   *
   * @param aRriSection {@link ISkRriSection} - the rri section to open, may be <code>null</code>
   */
  void showRriSectionUipart( ISkRriSection aRriSection );
}
