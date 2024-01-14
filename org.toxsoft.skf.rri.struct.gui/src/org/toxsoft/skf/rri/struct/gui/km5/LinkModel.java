package org.toxsoft.skf.rri.struct.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.skf.rri.struct.gui.km5.ISkResources.*;

import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.sgw.*;

/**
 * Модель редактора описания НСИ связи.
 *
 * @author max
 */
public class LinkModel
    extends M5Model<IDtoLinkInfo> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "org.toxsoft.skf.rri.struct.gui.km5.LinkModel"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #LINK_ID}.
   */
  public static final String FID_LINK_ID = "LinkId"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #LINK_NAME}.
   */
  public static final String FID_LINK_NAME = "ts.Name"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #LINK_DESCR}.
   */
  public static final String FID_LINK_DESCR = "ts.Descr"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #MAX_COUNT}.
   */
  public static final String FID_MAX_COUNT = "LinkMaxCount"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #IS_EXACT_COUNT}.
   */
  public static final String FID_IS_EXACT_COUNT = "LinkExactCount"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #RIGHT_CLASS_IDS}.
   */
  public static final String FID_RIGHT_CLASS_IDS = "LinkRightClassIds"; //$NON-NLS-1$

  /**
   * Поле идентификатора связи
   */
  public final M5AttributeFieldDef<IDtoLinkInfo> LINK_ID = new M5AttributeFieldDef<>( FID_LINK_ID, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_LINK_ID, STR_D_LINK_ID );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_INVARIANT );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoLinkInfo aEntity ) {
      return AvUtils.avStr( aEntity.id() );
    }
  };

  /**
   * Поле наименования связи
   */
  public final M5AttributeFieldDef<IDtoLinkInfo> LINK_NAME = new M5AttributeFieldDef<>( FID_LINK_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_LINK_NAME, STR_D_LINK_NAME );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoLinkInfo aEntity ) {
      return AvUtils.avStr( aEntity.nmName() );
    }
  };

  /**
   * Поле описания связи
   */
  public final M5AttributeFieldDef<IDtoLinkInfo> LINK_DESCR = new M5AttributeFieldDef<>( FID_LINK_DESCR, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_LINK_DESCRIPTION, STR_D_LINK_DESCRIPTION );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoLinkInfo aEntity ) {
      return AvUtils.avStr( aEntity.description() );
    }
  };

  /**
   * Поле макс. количества связанных сущностей
   */
  public final M5AttributeFieldDef<IDtoLinkInfo> MAX_COUNT = new M5AttributeFieldDef<>( FID_MAX_COUNT, INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_LINK_MAX_COUNT, STR_D_LINK_MAX_COUNT );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoLinkInfo aEntity ) {
      return AvUtils.avInt( aEntity.linkConstraint().maxCount() );
    }
  };

  /**
   * Поле индекса точного количества связей
   */
  public final M5AttributeFieldDef<IDtoLinkInfo> IS_EXACT_COUNT =
      new M5AttributeFieldDef<>( FID_IS_EXACT_COUNT, BOOLEAN ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_LINK_IS_EXACT_COUNT, STR_D_LINK_IS_EXACT_COUNT );
          setDefaultValue( IAtomicValue.NULL );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IDtoLinkInfo aEntity ) {
          return AvUtils.avBool( aEntity.linkConstraint().isExactCount() );
        }
      };

  /**
   * Поле классов связи.
   */

  public final M5MultiLookupFieldDef<IDtoLinkInfo, ISkClassInfo> RIGHT_CLASS_IDS =
      new M5MultiLookupKeyFieldDef<>( FID_RIGHT_CLASS_IDS, ISgwM5Constants.MID_SGW_CLASS_INFO, FID_ID, String.class ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_LINK_CLASS_IDS, STR_D_LINK_CLASS_IDS );
          setFlags( M5FF_COLUMN );
          // setLookupProvider( new IM5LookupProvider<ISkClassInfo>() {
          //
          // @Override
          // public IList<ISkClassInfo> listItems( IPolyFilterParams aFilterParams ) {
          // ISkConnection skConn = ownerModel().domain().domainContext().get( ISkConnection.class );
          // ISkClassInfoManager cim = skConn.coreApi().sysdescr().classInfoManager();
          // return cim.listClasses();
          // }
          // } );
        }

        @Override
        protected IList<ISkClassInfo> doGetFieldValue( IDtoLinkInfo aEntity ) {
          ISkConnection skConn = domain().tsContext().get( ISkConnection.class );
          IStridablesList<ISkClassInfo> all = new StridablesList<>( skConn.coreApi().sysdescr().listClasses() );
          IListEdit<ISkClassInfo> ll = new ElemArrayList<>( aEntity.rightClassIds().size() );

          for( String cid : aEntity.rightClassIds() ) {
            ISkClassInfo cinf = all.findByKey( cid );
            if( cinf != null ) {
              ll.add( cinf );
            }
            else {
              // LoggerUtils.errorLogger().warning( "Модель %s: связь %s ссылается на несуществующий класс %s",
              // ownerModel().id(), aEntity.id(), cid );
            }
          }
          return ll;
        }

        @Override
        protected String doGetFieldValueName( IDtoLinkInfo aEntity ) {
          IList<ISkClassInfo> linkClasses = doGetFieldValue( aEntity );

          StringBuilder sb = new StringBuilder();

          String add = TsLibUtils.EMPTY_STRING;
          for( ISkClassInfo classInfo : linkClasses ) {
            sb.append( add );
            sb.append( classInfo.id() );
            add = ", "; //$NON-NLS-1$
          }

          return sb.toString();
        }

      };

  /**
   * Конструктор.
   */
  public LinkModel() {
    super( MODEL_ID, IDtoLinkInfo.class );
    setNameAndDescription( STR_N_LINKS_LIST, STR_D_LINKS_LIST );

    addFieldDefs( LINK_ID, LINK_NAME, LINK_DESCR, MAX_COUNT, IS_EXACT_COUNT, RIGHT_CLASS_IDS );
  }

}
