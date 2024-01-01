package org.toxsoft.skf.rri.struct.gui.panels;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
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
 * Панель редактирования структуры (набора) параметров НСИ.
 *
 * @author goga
 */
public class PanelRriSectionStructEditor
    extends TsPanel {

  private TextControlContribution textContr1;

  final ISkConnection                      conn;
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
    // if( this.skObjectPanel != null ) {
    // this.skObjectPanel.setClass( this.selectedClass );
    // }
    // if( this.skObjectCheckedListPanel != null ) {
    // this.skObjectCheckedListPanel.setClass( this.selectedClass );
    // }
    // if( this.propPanel != null ) {
    // this.propPanel.setClass( this.selectedClass );
    // }
    // if( this.rtDataCheckedListPanel != null ) {
    // this.rtDataCheckedListPanel.setClass( this.selectedClass );
    // }
  };

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelRriSectionStructEditor( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.params().addAll( aContext.params() );

    this.setLayout( new BorderLayout() );

    TsComposite frame = new TsComposite( this );
    frame.setLayout( new BorderLayout() );

    TsToolbar toolBar = new TsToolbar( ctx );
    toolBar.setIconSize( EIconSize.IS_24X24 );

    // toolBar.addActionDef( ACDEF_EDIT );
    toolBar.addActionDef( ACDEF_RUN_TEST );

    toolBar.addSeparator();

    Control toolbarCtrl = toolBar.createControl( frame );
    toolbarCtrl.setLayoutData( BorderLayout.NORTH );

    textContr1 = new TextControlContribution( "Label", 300, "Раздел НСИ:", SWT.NONE ); //$NON-NLS-1$
    toolBar.addContributionItem( textContr1 );

    toolBar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_EDIT.id() ) ) {
        editRriSectionList( ctx );
        return;
      }
      if( aActionId.equals( ACDEF_RUN_TEST.id() ) ) {
        selectRriSection( ctx );
        return;
      }
    } );

    SashForm sfMain = new SashForm( frame, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );

    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    final IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkClassInfo> model = m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );

    // IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    // IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
    // avValobj( EBorderLayoutPlacement.SOUTH ) );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AvUtils.AV_TRUE );

    clm = new RriClassInfoLifeCycleManager( model, conn.coreApi() );

    // classesPanel = model.panelCreator().createCollEditPanel( ctx, clm.itemsProvider(), clm );
    // setup
    // classesPanel.addTsSelectionListener( classChangeListener );
    // classesPanel.createControl( sfMain );
    // classesPanel.refresh();

    AttributeModel attrModel = (AttributeModel)m5.getModel( AttributeModel.MODEL_ID, IDtoAttrInfo.class );
    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );

    // ISkRriSection rriSection = rriService.findSection( "test.section" );
    // if( rriSection == null ) {
    // rriSection = rriService.createSection( "test.section", "Test Section", "Test Section", IOptionSet.NULL );
    // }

    alm = new AttributeLifeCycleManager( ctx, attrModel, rriService );
    // -----------------------------------

    MultiPaneComponentModown<ISkClassInfo> classComponentModown =
        new MultiPaneComponentModown<>( ctx, model, clm.itemsProvider(), clm ) {

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
                TsDialogInfo di = new TsDialogInfo( aContext, "Выбор класса для НСИ", "Выбор класса для НСИ" );
                // установим нормальный размер диалога
                di.setMinSize( new TsPoint( -30, -40 ) );
                ISkClassInfo selectClass = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), lm );
                if( selectClass == null ) {
                  return;
                }
                TsDialogInfo cdi = new TsDialogInfo( aContext, null, "Создание атрибут НСИ",
                    "Необходимо создать хотя бы один атрибут НСИ для нового класса", 0 );

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
        };
    classesPanel = new M5CollectionPanelMpcModownWrapper<>( classComponentModown, false );
    // ----------------------------------
    classesPanel.addTsSelectionListener( classChangeListener );
    classesPanel.createControl( sfMain );
    classComponentModown.toolbar().setNameLabelText( "Классы НСИ: " );
    classesPanel.refresh();

    SashForm rightPane = new SashForm( sfMain, SWT.VERTICAL );

    MultiPaneComponentModown<IDtoAttrInfo> attrComponentModown =
        new MultiPaneComponentModown<>( ctx, attrModel, alm.itemsProvider(), alm );

    attrPanel = new M5CollectionPanelMpcModownWrapper<>( attrComponentModown, false );
    attrPanel.createControl( rightPane );
    attrComponentModown.toolbar().setNameLabelText( "Атрибуты НСИ: " );
    attrPanel.refresh();

    ITsGuiContext lCtx = new TsGuiContext( aContext );

    LinkModel linkModel = (LinkModel)m5.getModel( LinkModel.MODEL_ID, IDtoLinkInfo.class );
    llm = new LinkLifeCycleManager( lCtx, linkModel, rriService );

    MultiPaneComponentModown<IDtoLinkInfo> linkComponentModown =
        new MultiPaneComponentModown<>( ctx, linkModel, llm.itemsProvider(), llm );

    linkPanel = new M5CollectionPanelMpcModownWrapper<>( linkComponentModown, false );
    linkPanel.createControl( rightPane );
    linkComponentModown.toolbar().setNameLabelText( "Связи НСИ: " );
    linkPanel.refresh();

    sfMain.setWeights( 4000, 6000 );
    setRriSection( null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericEntityPanel
  //

  private void setRriSection( ISkRriSection aEntity ) {
    String sectionId = aEntity != null ? aEntity.id() : null;
    clm.setSectionId( sectionId );
    alm.setSectionId( sectionId );
    alm.setClassId( null );
    llm.setSectionId( sectionId );
    llm.setClassId( null );

    classesPanel.refresh();
    attrPanel.refresh();
    linkPanel.refresh();

    textContr1.setText( "Раздел НСИ: " + (aEntity != null ? aEntity.id() : "не выбран") );
  }

  private void selectRriSection( ITsGuiContext aContext ) {
    // select the section

    IM5Domain m5 = conn.scope().get( IM5Domain.class );

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );

    IM5Model<ISkRriSection> model = m5.getModel( RriSectionModel.MODEL_ID, ISkRriSection.class );
    IM5LifecycleManager<ISkRriSection> lm = model.getLifecycleManager( rriService );
    TsDialogInfo di = new TsDialogInfo( aContext, "Выбор раздела НСИ", "Выбор раздела НСИ" );
    // установим нормальный размер диалога
    di.setMinSize( new TsPoint( -30, -40 ) );
    ISkRriSection section = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), lm );
    if( section != null ) {
      setRriSection( section );
    }
    else {
      if( clm.getSectionId() != null ) {
        if( !rriService.listSections().hasKey( clm.getSectionId() ) ) {
          setRriSection( null );
        }
      }
    }
  }

  private void editRriSectionList( ITsGuiContext aContext ) {
    // edit the sections

    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );

    IM5Model<ISkRriSection> model = m5.getModel( RriSectionModel.MODEL_ID, ISkRriSection.class );
    ITsDialogInfo di =
        new TsDialogInfo( aContext, "Редактирование списка разделов НСИ", "Редактирование списка разделов НСИ" );
    IM5LifecycleManager<ISkRriSection> lm = model.getLifecycleManager( rriService );
    M5GuiUtils.editModownColl( aContext, model, di, lm );
  }

  static class TextControlContribution
      extends ControlContribution {

    private final int width;
    private final int swtStyle;
    private String    text;
    CLabel            label;

    /**
     * Конструктор.
     *
     * @param aId String - ИД элемента
     * @param aWidth int - ширина текстового поля
     * @param aText String - текст
     * @param aSwtStyle int - swt стиль
     */
    public TextControlContribution( String aId, int aWidth, String aText, int aSwtStyle ) {
      super( aId );
      width = aWidth;
      swtStyle = aSwtStyle;
      text = aText;
    }

    // ------------------------------------------------------------------------------------
    // ControlContribution
    //

    @Override
    protected Control createControl( Composite aParent ) {
      label = new CLabel( aParent, swtStyle );
      label.setText( text );
      label.setAlignment( SWT.LEFT );
      return label;
    }

    @Override
    protected int computeWidth( Control aControl ) {
      if( width == SWT.DEFAULT ) {
        return super.computeWidth( aControl );
      }
      return width;
    }

    // ------------------------------------------------------------------------------------
    // API
    //

    /**
     * Возвращает текстовое поле.
     *
     * @return CLabel - текстовое поле
     */
    public CLabel label() {
      return label;
    }

    void setText( String aText ) {
      label.setText( aText );
      label.redraw();
    }

  }

}
