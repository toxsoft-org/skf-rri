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
    // 2024-11-15 mvk ---+++ правка кода позволяет использовать initialize() при старте сервера до создания
    // локальных(внутри сервера) соединений
    // ISkRegRefInfoService rriService = aCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    // if( rriService != null ) {
    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)aCoreApi.services().findByKey( ISkRegRefInfoService.SERVICE_ID );
    if( rriService == null ) {
      rriService = aCoreApi.addService( SkRegRefInfoService.CREATOR );
      ISkUgwiService uServ = aCoreApi.ugwiService();
      uServ.registerKind( UgwiKindRriAttr.INSTANCE.createUgwiKind( aCoreApi ) );
      uServ.registerKind( UgwiKindRriLink.INSTANCE.createUgwiKind( aCoreApi ) );
      // TODO add other RRI-related UGWI kinds

    }
  };

  /**
   * The plugin initialization must be called before any action to access classes in this plugin.
   */
  public static void initialize() {
    SkUgwiUtils.registerUgwiKind( UgwiKindRriAttr.INSTANCE );
    SkUgwiUtils.registerUgwiKind( UgwiKindRriLink.INSTANCE );
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
