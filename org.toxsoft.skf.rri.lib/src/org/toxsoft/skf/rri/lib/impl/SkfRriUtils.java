package org.toxsoft.skf.rri.lib.impl;

import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Initialization and utility methods.
 *
 * @author hazard157
 */
public class SkfRriUtils {

  /**
   * Core handler to register all registered Sk-connection bound {@link ISkUgwiKind} when connection opens.
   */
  private static final ISkCoreExternalHandler coreRegistrationHandler = aCoreApi -> {
    ISkRegRefInfoService rriService = aCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    if( rriService != null ) {
      ISkUgwiService uServ = aCoreApi.ugwiService();
      uServ.registerKind( UgwiKindRriAttr.INSTANCE.createUgwiKind( aCoreApi ) );
      // TODO add other RRI-related UGWI kinds
    }
  };

  /**
   * The plugin initialization must be called before any action to access classes in this plugin.
   */
  public static void initialize() {
    SkUgwiUtils.registerUgwiKind( UgwiKindRriAttr.INSTANCE );
    // TODO add other RRI-related UGWI kinds
    SkCoreUtils.registerCoreApiHandler( coreRegistrationHandler );
  }

  /**
   * No subclasses.
   */
  private SkfRriUtils() {
    // nop
  }

}
