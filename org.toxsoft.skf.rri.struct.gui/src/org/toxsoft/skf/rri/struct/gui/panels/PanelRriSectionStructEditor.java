package org.toxsoft.skf.rri.struct.gui.panels;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.skf.rri.struct.gui.panels.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.km5.sgw.*;

/**
 * Panel to edit structure of selected RRI section
 *
 * @author max
 */
public class PanelRriSectionStructEditor
    extends TsPanel {

  private final ISkConnection              conn;
  private IM5CollectionPanel<ISkClassInfo> classesPanel;
  private ISkClassInfo                     selectedClass = null;

  private RriClassInfoLifeCycleManager clm;

  private IM5CollectionPanel<IDtoAttrInfo> attrPanel;

  private AttributeLifeCycleManager alm;

  private IM5CollectionPanel<IDtoLinkInfo> linkPanel;

  private LinkLifeCycleManager llm;

  private final ITsSelectionChangeListener<ISkClassInfo> classChangeListener = ( aSource, aSelectedItem ) -> {
    this.selectedClass = aSelectedItem;
    if( selectedClass == null ) {
      return;
    }
    if( linkPanel != null ) {
      llm.setClassId( selectedClass.id() );
      linkPanel.refresh();
    }
    if( attrPanel != null ) {
      alm.setClassId( selectedClass.id() );
      attrPanel.refresh();
    }
  };

  /**
   * Конструктор панели.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelRriSectionStructEditor( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    final IM5Domain m5 = conn.scope().get( IM5Domain.class );

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );

    this.setLayout( new BorderLayout() );

    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );

    IM5Model<ISkClassInfo> clsModel = m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );

    ITsGuiContext clsCtx = new TsGuiContext( aContext );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( clsCtx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( clsCtx.params(), AvUtils.AV_TRUE );

    clm = new RriClassInfoLifeCycleManager( clsModel, conn.coreApi() );

    AttributeModel attrModel = (AttributeModel)m5.getModel( AttributeModel.MODEL_ID, IDtoAttrInfo.class );

    ITsGuiContext atrCtx = new TsGuiContext( aContext );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( atrCtx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( atrCtx.params(), AvUtils.AV_TRUE );

    alm = new AttributeLifeCycleManager( atrCtx, attrModel, rriService );
    // -----------------------------------

    final MultiPaneComponentModown<ISkClassInfo> classComponentModown =
        new MultiPaneComponentModown<>( clsCtx, clsModel, clm.itemsProvider(), clm ) {

          @SuppressWarnings( "unused" )
          @Override
          public void processAction( String aActionId ) {
            ISkClassInfo selClass = selectedItem();

            switch( aActionId ) {

              case ACTID_ADD:
                IM5Model<ISkClassInfo> classModel =
                    m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );
                // TODO
                ChoosableClassInfoLifeCycleManager lm =
                    new ChoosableClassInfoLifeCycleManager( classModel, conn.coreApi() );
                lm.setSectionId( alm.getSectionId() );
                TsDialogInfo di = new TsDialogInfo( tsContext(), STR_SELECTION_CLASS_TO_ADD_INTO_RRI,
                    STR_SELECTION_CLASS_TO_ADD_INTO_RRI );
                // установим нормальный размер диалога
                di.setMinSize( new TsPoint( -30, -40 ) );
                ISkClassInfo selectClass = M5GuiUtils.askSelectItem( di, model(), null, lm.itemsProvider(), lm );
                if( selectClass == null ) {
                  return;
                }
                TsDialogInfo cdi = new TsDialogInfo( tsContext(), null, STR_RRI_ATTR_CREATION,
                    IT_NEEDS_TO_CREATE_AT_LEAST_ONE_RRI_ATTR, 0 );

                IM5BunchEdit<IDtoAttrInfo> initVals = alm.createNewItemValues();
                String currClass = alm.getClassId();
                alm.setClassId( selectClass.id() );

                IDtoAttrInfo createdAttr = M5GuiUtils.askCreate( tsContext(), attrModel, initVals, cdi, alm );
                alm.setClassId( currClass );

                classesPanel.refresh();
                classesPanel.setSelectedItem( selectClass );
                break;

              case ACTID_REMOVE:
                super.processAction( aActionId );
                break;

              default:
                throw new TsNotAllEnumsUsedRtException( aActionId );
            }
          }

          protected boolean doGetIsAddAllowed( ISkClassInfo aSel ) {
            return clm.getSectionId() != null && clm.getSectionId().length() > 0;
          }
        };
    classesPanel = new M5CollectionPanelMpcModownWrapper<>( classComponentModown, false );
    // ----------------------------------
    classesPanel.addTsSelectionListener( classChangeListener );
    classesPanel.createControl( sfMain );
    classComponentModown.toolbar().setNameLabelText( STR_RRI_CLASSES );
    classesPanel.refresh();

    SashForm rightPane = new SashForm( sfMain, SWT.VERTICAL );

    MultiPaneComponentModown<IDtoAttrInfo> attrComponentModown =
        new MultiPaneComponentModown<>( atrCtx, attrModel, alm.itemsProvider(), alm ) {

          protected boolean doGetIsAddAllowed( IDtoAttrInfo aSel ) {
            return alm.getSectionId() != null && alm.getSectionId().length() > 0 && alm.getClassId() != null
                && alm.getClassId().length() > 0;
          }
        };

    attrPanel = new M5CollectionPanelMpcModownWrapper<>( attrComponentModown, false );
    attrPanel.createControl( rightPane );
    attrComponentModown.toolbar().setNameLabelText( STR_RRI_ATTRS );
    attrPanel.refresh();

    ITsGuiContext lnkCtx = new TsGuiContext( aContext );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( lnkCtx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( lnkCtx.params(), AvUtils.AV_TRUE );

    LinkModel linkModel = (LinkModel)m5.getModel( LinkModel.MODEL_ID, IDtoLinkInfo.class );
    llm = new LinkLifeCycleManager( lnkCtx, linkModel, rriService );

    MultiPaneComponentModown<IDtoLinkInfo> linkComponentModown =
        new MultiPaneComponentModown<>( lnkCtx, linkModel, llm.itemsProvider(), llm ) {

          protected boolean doGetIsAddAllowed( IDtoLinkInfo aSel ) {
            return llm.getSectionId() != null && llm.getSectionId().length() > 0 && llm.getClassId() != null
                && llm.getClassId().length() > 0;
          }
        };

    linkPanel = new M5CollectionPanelMpcModownWrapper<>( linkComponentModown, false );
    linkPanel.createControl( rightPane );
    linkComponentModown.toolbar().setNameLabelText( STR_RRI_LINKS );
    linkPanel.refresh();

    sfMain.setWeights( 4000, 6000 );
  }

  /**
   * Sets RRI section for ediing its structure.
   *
   * @param aRriSection - RRI section, can be null.
   */
  public void setRriSection( ISkRriSection aRriSection ) {
    String sectionId = aRriSection != null ? aRriSection.id() : null;
    if( clm == null ) {
      return;
    }
    clm.setSectionId( sectionId );
    alm.setSectionId( sectionId );
    alm.setClassId( null );
    llm.setSectionId( sectionId );
    llm.setClassId( null );

    classesPanel.refresh();
    attrPanel.refresh();
    linkPanel.refresh();
  }
}
