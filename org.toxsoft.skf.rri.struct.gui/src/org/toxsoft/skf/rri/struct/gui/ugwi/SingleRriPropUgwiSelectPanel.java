package org.toxsoft.skf.rri.struct.gui.ugwi;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.struct.gui.ugwi.ISkResources.*;
import static org.toxsoft.uskat.core.gui.ISkCoreGuiConstants.*;
import static org.toxsoft.uskat.core.gui.km5.sgw.ISgwM5Constants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.bricks.cond.IPanelSingleCondInfo;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.bricks.stdevents.ITsSelectionChangeListener;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants;
import org.toxsoft.core.tsgui.m5.gui.panels.IM5CollectionPanel;
import org.toxsoft.core.tsgui.m5.model.IM5ItemsProvider;
import org.toxsoft.core.tsgui.m5.model.impl.M5DefaultItemsProvider;
import org.toxsoft.core.tsgui.panels.generic.AbstractGenericEntityEditPanel;
import org.toxsoft.core.tsgui.panels.generic.IGenericSelectorPanel;
import org.toxsoft.core.tsgui.utils.ITsVisualsProvider;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.core.tsgui.valed.controls.basic.ValedComboSelector;
import org.toxsoft.core.tsgui.widgets.TsComposite;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.ugwi.Ugwi;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.ugwi.UgwiKindRriAttr;
import org.toxsoft.skf.rri.struct.gui.km5.RriClassInfoLifeCycleManager;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.objserv.ISkObject;
import org.toxsoft.uskat.core.api.sysdescr.ESkClassPropKind;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoClassPropInfoBase;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.ugwi.kinds.SingleSkPropUgwiSelectPanel;
import org.toxsoft.uskat.core.impl.SkCoreApi;

/**
 * {@link IGenericSelectorPanel} implementation for selection RRI prop
 * <p>
 *
 * @author dima
 */
public class SingleRriPropUgwiSelectPanel
    extends AbstractGenericEntityEditPanel<Ugwi>
    implements IGenericSelectorPanel<Ugwi> {

  /**
   * ID of option {@link #OPDEF_RRI_UGWI_KIND_ID}.
   */
  public static final String OPID_RRI_UGWI_KIND_ID = TS_ID + ".gui.ugwi.edit.UgwiRriKindId"; //$NON-NLS-1$

  /**
   * option: ID of the RRI Ugwi kind .
   */
  public static final IDataDef OPDEF_RRI_UGWI_KIND_ID = DataDef.create( OPID_RRI_UGWI_KIND_ID, STRING, //
      TSID_DEFAULT_VALUE, UgwiKindRriAttr.KIND_ID );

  static String MPC_OP_ID = TS_ID + ".rri.values.gui.ugwi"; //$NON-NLS-1$

  private final ISkCoreApi coreApi; // never is null

  private final ESkClassPropKind skClassPropKind;

  private final IM5CollectionPanel<ISkClassInfo>          panelClasses;
  private final IM5CollectionPanel<ISkObject>             panelObjects;
  private final IM5CollectionPanel<IDtoClassPropInfoBase> panelProps;

  private final RriClassInfoLifeCycleManager lmClasses;
  private ValedComboSelector<ISkRriSection>  rriSectionCombo;
  private ISkRriSection                      currRriSection;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - viewer flag, sets {@link #isViewer()} value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SingleRriPropUgwiSelectPanel( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
    coreApi = skCoreApi( tsContext() );
    TsInternalErrorRtException.checkNull( coreApi );
    skClassPropKind = SingleSkPropUgwiSelectPanel.OPDEF_CLASS_PROP_KIND.getValue( tsContext().params() ).asValobj();
    // IM5Domain m5 = aContext.get( IM5Domain.class );
    ISkConnection conn = ((SkCoreApi)coreApi).skConn();
    IM5Domain m5 = conn.scope().get( IM5Domain.class );

    // panelClasses
    IM5Model<ISkClassInfo> modelClasses = m5.getModel( MID_SGW_CLASS_INFO, ISkClassInfo.class );
    // берем готовую модель от Max'a
    lmClasses = new RriClassInfoLifeCycleManager( modelClasses, conn.coreApi() );

    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.params().addAll( aContext.params() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
        avValobj( EBorderLayoutPlacement.SOUTH ) );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );
    panelClasses = modelClasses.panelCreator().createCollViewerPanel( ctx, lmClasses.itemsProvider() );

    // panelObjects
    IM5Model<ISkObject> modelObjects = m5.getModel( MID_SGW_SK_OBJECT, ISkObject.class );
    panelObjects = modelObjects.panelCreator().createCollViewerPanel( ctx, IM5ItemsProvider.EMPTY );
    // panelProps
    String propModelId = sgwGetClassPropModelId( getClassPropKind() );
    IM5Model<IDtoClassPropInfoBase> modelProps = m5.getModel( propModelId, IDtoClassPropInfoBase.class );
    panelProps = modelProps.panelCreator().createCollViewerPanel( ctx, IM5ItemsProvider.EMPTY );

  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericEntityEditPanel
  //

  @Override
  protected ValidationResult doCanGetEntity() {
    ISkClassInfo selClass = panelClasses.selectedItem();
    if( selClass == null ) {
      return ValidationResult.error( MSG_NO_SEL_CLASS );
    }
    ISkObject selObj = panelObjects.selectedItem();
    if( selObj == null ) {
      return ValidationResult.error( MSG_NO_SEL_OBJ );
    }
    IDtoClassPropInfoBase selProp = panelProps.selectedItem();
    if( selProp == null ) {
      return ValidationResult.error( MSG_NO_SEL_PROP );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected Ugwi doGetEntity() {
    return selectedItem();
  }

  @Override
  protected void doProcessSetEntity() {
    if( specifiedEntity() != null ) {
      setSelectedItem( getEntity() );
    }
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Composite backPanel = new Composite( aParent, SWT.NONE );
    BorderLayout borderLayout = new BorderLayout();
    backPanel.setLayout( borderLayout );

    Composite rriSectionPanel = new Composite( backPanel, SWT.NONE );
    // place combo to the north of parent
    rriSectionPanel.setLayoutData( BorderLayout.NORTH );
    createSectionComboPanel( rriSectionPanel );

    Composite selectionPanel = new Composite( backPanel, SWT.NONE );
    selectionPanel.setLayoutData( BorderLayout.CENTER );
    createSelectionPanel( selectionPanel );
    return backPanel;
  }

  private void createSelectionPanel( Composite aParent ) {
    BorderLayout borderLayout = new BorderLayout();
    aParent.setLayout( borderLayout );

    TsComposite board = new TsComposite( aParent );
    FillLayout fillLayout = new FillLayout();
    fillLayout.marginHeight = 5;
    fillLayout.marginWidth = 5;
    board.setLayout( fillLayout );

    SashForm verticalSashForm = new SashForm( board, SWT.VERTICAL );
    SashForm horizontalSashForm = new SashForm( verticalSashForm, SWT.HORIZONTAL );

    // panels
    panelClasses.createControl( horizontalSashForm );
    panelObjects.createControl( horizontalSashForm );
    panelProps.createControl( verticalSashForm );
    // setup
    horizontalSashForm.setWeights( 1, 1 );
    horizontalSashForm.setSashWidth( 5 );
    verticalSashForm.setSashWidth( 5 );
    panelClasses.addTsSelectionListener( ( src, sel ) -> whenClassSelectionChanges() );
    panelObjects.addTsSelectionListener( ( src, sel ) -> whenObjectSelectionChanges() );
    panelProps.addTsSelectionListener( ( src, sel ) -> genericChangeEventer().fireChangeEvent() );
    panelProps.addTsDoubleClickListener( ( src, sel ) -> whenPropDoubleClicked( sel ) );
  }

  /**
   * Create panel to select Ugwi kind through combo
   *
   * @param aBkPanel - back panel
   */
  private void createSectionComboPanel( Composite aBkPanel ) {
    GridLayout gl = new GridLayout( 2, false );
    aBkPanel.setLayout( gl );
    CLabel l = new CLabel( aBkPanel, SWT.LEFT );
    l.setText( STR_RRI_SECTION );

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)coreApi.services().getByKey( ISkRegRefInfoService.SERVICE_ID );
    IList<ISkRriSection> sectionList = rriService.listSections();
    ITsVisualsProvider<ISkRriSection> visualsProvider = ISkRriSection::id;

    rriSectionCombo = new ValedComboSelector<>( tsContext(), sectionList, visualsProvider );
    rriSectionCombo.createControl( aBkPanel )
        .setLayoutData( new GridData( GridData.FILL, GridData.FILL, true, false ) );

    rriSectionCombo.eventer().addListener( ( aSource, aEditFinished ) -> {
      currRriSection = rriSectionCombo.selectedItem();
      if( currRriSection != null ) {
        setRriSection( currRriSection );
      }
    } );
    currRriSection = sectionList.first();
    rriSectionCombo.setSelectedItem( currRriSection );
    setRriSection( currRriSection );
  }

  /**
   * Sets RRI section for editing values of its objects.
   *
   * @param aRriSection ISkRriSection - RRI section for editing values of its objects.
   */
  public void setRriSection( ISkRriSection aRriSection ) {
    String sectionId = aRriSection != null ? aRriSection.id() : TsLibUtils.EMPTY_STRING;
    lmClasses.setSectionId( sectionId );

    // objLm.setClassIds( IStringList.EMPTY );

    panelClasses.refresh();
    panelObjects.refresh();
    panelProps.refresh();

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Invokes dialog with {@link IPanelSingleCondInfo} for {@link Ugwi} editing.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - the dialog window parameters
   * @param aInitVal {@link Ugwi} - initial value or <code>null</code>
   * @return {@link Ugwi} - edited value or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static Ugwi selectUgwi( ITsDialogInfo aDialogInfo, Ugwi aInitVal ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo );
    IDialogPanelCreator<Ugwi, Object> creator = ( par, od ) //
    -> new TsDialogGenericEntityEditPanel<>( par, od, ( aContext, aViewer ) -> {
      SingleRriPropUgwiSelectPanel panel = new SingleRriPropUgwiSelectPanel( aContext, aViewer );
      return panel;
    } );
    TsDialog<Ugwi, Object> d = new TsDialog<>( aDialogInfo, aInitVal, null, creator );
    return d.execData();
  }

  @Override
  public Ugwi selectedItem() {
    TsValidationFailedRtException.checkError( canGetEntity() );
    ISkObject selObj = panelObjects.selectedItem();
    IDtoClassPropInfoBase selProp = panelProps.selectedItem();
    ESkClassPropKind kind = getClassPropKind();
    String namespace = currRriSection.id();
    Gwid gwid = kind.createConcreteGwid( selObj.skid(), selProp.id() );
    String ugwiKindId = tsContext().params().getStr( OPDEF_RRI_UGWI_KIND_ID );
    Ugwi retVal = Ugwi.of( ugwiKindId, namespace, gwid.canonicalString() );
    return retVal;
  }

  @Override
  public void setSelectedItem( Ugwi aItem ) {
    panelProps.setSelectedItem( null );
    panelObjects.setSelectedItem( null );
    panelClasses.setSelectedItem( null );
    if( aItem != null ) {
      Gwid gwid = Gwid.of( aItem.essence() );
      ISkClassInfo cinf = coreApi.sysdescr().findClassInfo( gwid.classId() );
      if( cinf != null ) {
        panelClasses.setSelectedItem( cinf );
        if( !gwid.isAbstract() && !gwid.isMulti() ) {
          ISkObject obj = coreApi.objService().find( gwid.skid() );
          if( obj != null && gwid.isProp() ) {
            panelObjects.setSelectedItem( obj );
            IDtoClassPropInfoBase prop = cinf.props( getClassPropKind() ).list().findByKey( gwid.propId() );
            panelProps.setSelectedItem( prop );
          }
        }
      }
    }
  }

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<Ugwi> aListener ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<Ugwi> aListener ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void refresh() {
    // nop
  }

  private ESkClassPropKind getClassPropKind() {
    return skClassPropKind;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //
  private void whenClassSelectionChanges() {
    panelProps.setSelectedItem( null );
    panelObjects.setSelectedItem( null );
    ISkClassInfo cinf = panelClasses.selectedItem();
    if( cinf != null ) {
      M5DefaultItemsProvider<ISkObject> itemsProvider = new M5DefaultItemsProvider<>();
      itemsProvider.items().setAll( coreApi.objService().listObjs( cinf.id(), true ) );
      panelObjects.setItemsProvider( itemsProvider );
      panelObjects.refresh();
    }
  }

  private void whenObjectSelectionChanges() {
    panelProps.setSelectedItem( null );
    ISkObject sel = panelObjects.selectedItem();
    if( sel != null ) {
      M5DefaultItemsProvider<IDtoClassPropInfoBase> itemsProvider = new M5DefaultItemsProvider<>();
      ISkClassInfo cinf = panelClasses.selectedItem();
      IStridablesList<IDtoRriParamInfo> rriParamInfoes = currRriSection.listParamInfoes( cinf.id() );
      IListEdit<IDtoClassPropInfoBase> params = new ElemArrayList<>();
      for( IDtoRriParamInfo rriParamInfo : rriParamInfoes ) {
        params.add( getPropInfo( rriParamInfo ) );
      }
      itemsProvider.items().setAll( params );
      panelProps.setItemsProvider( itemsProvider );
      panelProps.refresh();
    }
  }

  private IDtoClassPropInfoBase getPropInfo( IDtoRriParamInfo aRriParamInfo ) {
    switch( skClassPropKind ) {
      case ATTR:
        return aRriParamInfo.attrInfo();
      case CLOB:
        break;
      case CMD:
        break;
      case EVENT:
        break;
      case LINK:
        return aRriParamInfo.linkInfo();
      case RIVET:
        break;
      case RTDATA:
        break;
      default:
        break;
    }
    throw new TsIllegalStateRtException();
  }

  void whenPropDoubleClicked( IDtoClassPropInfoBase aSel ) {
    if( aSel != null && !canGetEntity().isError() ) {
      // TODO
      // fireTsDoubleClickEvent( getEntity() );
    }
  }

}
