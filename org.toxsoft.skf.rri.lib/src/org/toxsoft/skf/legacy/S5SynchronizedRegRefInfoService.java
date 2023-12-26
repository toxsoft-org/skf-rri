package org.toxsoft.skf.legacy;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.events.ITsEventer;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.validator.ITsValidationSupport;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.concurrent.*;

/**
 * Синхронизация доступа к {@link ISkRegRefInfoService} (декоратор)
 *
 * @author mvk
 */
public final class S5SynchronizedRegRefInfoService
    extends S5SynchronizedService<ISkRegRefInfoService>
    implements ISkRegRefInfoService {

  private final S5SynchronizedEventer<ITsEventer<ISkRegRefInfoServiceListener>, ISkRegRefInfoServiceListener> eventer;
  private final S5SynchronizedValidationSupport<ISkRegRefInfoServiceValidator>                                svs;

  /**
   * Конструктор
   *
   * @param aConnection {@link S5SynchronizedConnection} защищенное соединение
   * @throws TsNullArgumentRtException аругмент = null
   * @throws TsItemNotFoundRtException в соединении не найдена служба которую необходимо защитить
   */
  public S5SynchronizedRegRefInfoService( S5SynchronizedConnection aConnection ) {
    this( (ISkRegRefInfoService)aConnection.getUnsynchronizedService( ISkRegRefInfoService.SERVICE_ID ),
        aConnection.nativeLock() );
    aConnection.addService( this );
  }

  /**
   * Конструктор
   *
   * @param aTarget {@link ISkRegRefInfoService} защищаемый ресурс
   * @param aLock {@link ReentrantReadWriteLock} блокировка доступа к ресурсу
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public S5SynchronizedRegRefInfoService( ISkRegRefInfoService aTarget, ReentrantReadWriteLock aLock ) {
    super( aTarget, aLock );
    eventer = new S5SynchronizedEventer<>( aTarget.eventer(), aLock );
    svs = new S5SynchronizedValidationSupport<>( aTarget.svs(), aLock );
  }

  // ------------------------------------------------------------------------------------
  // S5SynchronizedResource
  //
  @Override
  protected void doChangeTarget( ISkRegRefInfoService aPrevTarget, ISkRegRefInfoService aNewTarget,
      ReentrantReadWriteLock aNewLock ) {
    eventer.changeTarget( aNewTarget.eventer(), aNewLock );
    svs.changeTarget( aNewTarget.svs(), aNewLock );
  }

  // ------------------------------------------------------------------------------------
  // ISkRegRefInfoService
  //
  @Override
  public IStridablesList<ISkRriSection> listSections() {
    lockWrite( this );
    try {
      return target().listSections();
    }
    finally {
      unlockWrite( this );
    }
  }

  @Override
  public ISkRriSection findSection( String aSectionId ) {
    TsNullArgumentRtException.checkNull( aSectionId );
    lockWrite( this );
    try {
      return target().findSection( aSectionId );
    }
    finally {
      unlockWrite( this );
    }
  }

  @Override
  public ISkRriSection getSection( String aSectionId ) {
    TsNullArgumentRtException.checkNull( aSectionId );
    lockWrite( this );
    try {
      return target().getSection( aSectionId );
    }
    finally {
      unlockWrite( this );
    }
  }

  @Override
  public ISkRriSection createSection( String aId, String aName, String aDescription, IOptionSet aParams ) {
    TsNullArgumentRtException.checkNulls( aId, aName, aDescription, aParams );
    lockWrite( this );
    try {
      return target().createSection( aId, aName, aDescription, aParams );
    }
    finally {
      unlockWrite( this );
    }
  }

  @Override
  public void removeSection( String aSectionId ) {
    TsNullArgumentRtException.checkNull( aSectionId );
    lockWrite( this );
    try {
      target().removeSection( aSectionId );
    }
    finally {
      unlockWrite( this );
    }
  }

  @Override
  public ISkRriHistory history() {
    lockWrite( this );
    try {
      return target().history();
    }
    finally {
      unlockWrite( this );
    }
  }

  @Override
  public ITsEventer<ISkRegRefInfoServiceListener> eventer() {
    return eventer;
  }

  @Override
  public ITsValidationSupport<ISkRegRefInfoServiceValidator> svs() {
    return svs;
  }
}
