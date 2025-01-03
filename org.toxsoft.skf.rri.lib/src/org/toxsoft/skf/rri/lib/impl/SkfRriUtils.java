package org.toxsoft.skf.rri.lib.impl;

import org.toxsoft.core.tslib.math.cond.checker.*;
import org.toxsoft.skf.alarms.lib.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.checkers.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.*;
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
    ISkRegRefInfoService rriService = (ISkRegRefInfoService)aCoreApi.findService( ISkRegRefInfoService.SERVICE_ID );
    if( rriService == null ) {
      rriService = aCoreApi.addService( SkRegRefInfoService.CREATOR );
      ISkUgwiService uServ = aCoreApi.ugwiService();
      uServ.registerKind( UgwiKindRriAttr.INSTANCE.createUgwiKind( aCoreApi ) );
      uServ.registerKind( UgwiKindRriLink.INSTANCE.createUgwiKind( aCoreApi ) );
      // TODO add other RRI-related UGWI kinds

    }
    ISkAlarmService alarmService = aCoreApi.findService( ISkAlarmService.SERVICE_ID );
    if( alarmService != null ) {
      ITsCheckerTopicManager<ISkCoreApi> tm = alarmService.getAlarmCheckersTopicManager();

      tm.registerType( new AlertCheckerRtdataVsRriType() );
      tm.registerType( new AlertCheckerRriTypeGtZero() );

      // dima 23.12.24 needles code
      // ISkUgwiKind uk;
      // uk = uServ.listKinds().getByKey( UgwiKindRriAttr.KIND_ID );
      // uk.registerHelper( IUgwiKindGuiHelper.class, new UgwiGuiHelperRriAttr( (AbstractSkUgwiKind)uk ) );
      // uk = uServ.listKinds().getByKey( UgwiKindRriLink.KIND_ID );
      // uk.registerHelper( IUgwiKindGuiHelper.class, new UgwiGuiHelperRriLink( (AbstractSkUgwiKind)uk ) );
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
