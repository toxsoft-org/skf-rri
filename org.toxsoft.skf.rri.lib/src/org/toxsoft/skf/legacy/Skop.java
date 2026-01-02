package org.toxsoft.skf.legacy;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Идентификация свойства конкретного объекта.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 * @deprecated use GWID for GWIDable entities or UGWI or complex data structures for non-GWIDable entities
 */
@Deprecated
public final class Skop
    implements Serializable, Comparable<Skop> {

  private static final long serialVersionUID = 157157L;

  /**
   * Value-object keeper identifier.
   */
  @Deprecated
  public static final String KEEPER_ID = "Skop"; //$NON-NLS-1$

  /**
   * Экземпляр-синголтон хранителя.
   */
  @Deprecated
  public static final IEntityKeeper<Skop> KEEPER =
      new AbstractEntityKeeper<>( Skop.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, Skop aEntity ) {
          Skid.KEEPER.write( aSw, aEntity.skid() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.propId() );
        }

        @Override
        protected Skop doRead( IStrioReader aSr ) {
          Skid skid = Skid.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          String propId = aSr.readIdPath();
          return new Skop( skid, propId );
        }
      };

  private final Skid   skid;
  private final String propId;

  /**
   * Конструктор.
   *
   * @param aSkid {@link Skid} - скид (идентификатор) объекта
   * @param aPropId String - идентификар (ИД-путь) свойства объекта
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException идентификатор свойства не ИД-имя
   */
  @Deprecated
  public Skop( Skid aSkid, String aPropId ) {
    skid = TsNullArgumentRtException.checkNull( aSkid );
    propId = StridUtils.checkValidIdPath( aPropId );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Deprecated
  @Override
  public String toString() {
    return skid.toString() + IdPair.CHAR_SEPARATOR + propId;
  }

  @Deprecated
  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof Skop that ) {
      return this.skid.equals( that.skid ) && this.propId.equals( that.propId );
    }
    return false;
  }

  @Deprecated
  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + skid.hashCode();
    result = TsLibUtils.PRIME * result + propId.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Deprecated
  @Override
  public int compareTo( Skop aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    int c = this.skid.compareTo( aThat.skid );
    if( c == 0 ) {
      c = this.propId.compareTo( propId );
    }
    return c;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает скид.
   *
   * @return {@link Skid} - скид (идентификатор) объекта
   */
  @Deprecated
  public Skid skid() {
    return skid;
  }

  /**
   * Возвращает идентификар свойства объекта.
   *
   * @return String - идентификар (ИД-путь) свойства объекта
   */
  @Deprecated
  public String propId() {
    return propId;
  }

}
