package org.toxsoft.skf.rri.values.gui.sol;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * Идентификатор sk сущности в разделе НСИ.
 * <p>
 *
 * @author vs
 */
public class RriId
    implements Serializable {

  private static final long serialVersionUID = 4151059724175075256L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "RriId"; //$NON-NLS-1$

  static final RriId NONE = new RriId( IStridable.NONE_ID, Gwid.createAttr( IStridable.NONE_ID, IStridable.NONE_ID ) );

  /**
   * The keeper singleton.
   * <p>
   */
  public static final IEntityKeeper<RriId> KEEPER =
      new AbstractEntityKeeper<>( RriId.class, EEncloseMode.ENCLOSES_BASE_CLASS, RriId.NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, RriId aEntity ) {
          aSw.writeAsIs( aEntity.sectionId );
          aSw.writeSeparatorChar();
          Gwid.KEEPER.writeEnclosed( aSw, aEntity.gwid );
        }

        @Override
        protected RriId doRead( IStrioReader aSr ) {
          String sId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          Gwid gw = Gwid.KEEPER.readEnclosed( aSr );
          return new RriId( sId, gw );
        }
      };

  private final String sectionId;

  private final Gwid gwid;

  /**
   * Контруктор.
   *
   * @param aSectionId String - ИД секции НСИ
   * @param aGwid Gwid - ИД sk сущности
   */
  public RriId( String aSectionId, Gwid aGwid ) {
    sectionId = aSectionId;
    gwid = aGwid;
  }

  /**
   * Возвращает идентификатор секции НСИ.
   *
   * @return String - идентификатор секции НСИ
   */
  public String sectionId() {
    return sectionId;
  }

  /**
   * Возвращает ИД sk сущности в разделе НСИ.
   *
   * @return Gwid - ИД sk сущности в разделе НСИ
   */
  public Gwid gwid() {
    return gwid;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return "(" + sectionId + "::" + gwid.toString() + ')'; //$NON-NLS-1$ //$NON-NLS-2$
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( object instanceof RriId id ) {
      return (id.sectionId.equals( this.sectionId ) && id.gwid.equals( this.gwid ));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + (sectionId.hashCode() ^ (sectionId.hashCode() >>> 32));
    result = PRIME * result + (gwid.hashCode() ^ (gwid.hashCode() >>> 32));
    return result;
  }

}
