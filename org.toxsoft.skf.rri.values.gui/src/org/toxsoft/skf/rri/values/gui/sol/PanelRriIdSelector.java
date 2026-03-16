package org.toxsoft.skf.rri.values.gui.sol;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель выбора идентификатора sk-сущности в разделе НСИ.
 * <p>
 *
 * @author vs
 */
public class PanelRriIdSelector
    extends AbstractTsDialogPanel<RriId, ITsGuiContext> {

  ISkRegRefInfoService rriServ    = null;
  ISkCoreApi           coreApi    = null;
  ISkRriSection        rriSection = null;

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelRriIdSelector( Composite aParent, TsDialog<RriId, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );

    coreApi = environ().get( ISkConnectionSupplier.class ).defConn().coreApi();
    rriServ = coreApi.getService( ISkRegRefInfoService.SERVICE_ID );

    Composite bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayoutData( BorderLayout.CENTER );
    init( bkPanel );
  }

  private Text   fldSectionId;
  private Button btnBrowseSection;

  void init( Composite aParent ) {
    GridLayout gl = new GridLayout( 3, false );
    aParent.setLayout( gl );

    CLabel l = new CLabel( aParent, SWT.CENTER );
    l.setText( "Секция НСИ: " );

    fldSectionId = new Text( aParent, SWT.BORDER );
    fldSectionId.setEditable( false );
    fldSectionId.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    btnBrowseSection = new Button( aParent, SWT.PUSH );
    btnBrowseSection.setText( "..." );
    btnBrowseSection.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        String sectionId = PanelRriSectionIdSelector.selectRriSectionId( fldSectionId.getText(), tsContext() );
        if( sectionId != null ) {
          onSectionChanged( sectionId );
        }
      }
    } );

    Composite bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 3, 1 ) );
    bkPanel.setLayout( new BorderLayout() );
    createContent( bkPanel );
  }

  @Override
  protected void doSetDataRecord( RriId aRriId ) {
    if( aRriId != null ) {
      onSectionChanged( aRriId.sectionId() );
      StructuredSelection sel = new StructuredSelection( coreApi.sysdescr().getClassInfo( aRriId.gwid().classId() ) );
      classesViewer.viewer().setSelection( sel );

      ISkObject skObj = coreApi.objService().get( new Skid( aRriId.gwid().classId(), aRriId.gwid().strid() ) );
      objectsViewer.viewer().setSelection( new StructuredSelection( skObj ) );

      IDtoRriParamInfo attrInfo;
      attrInfo = rriSection.listParamInfoes( aRriId.gwid().classId() ).getByKey( aRriId.gwid().propId() );
      attrsViewer.viewer().setSelection( new StructuredSelection( attrInfo ) );
    }
  }

  @Override
  protected RriId doGetDataRecord() {
    String sectionId = fldSectionId.getText();
    ISkObject skObj;
    skObj = (ISkObject)((IStructuredSelection)objectsViewer.viewer().getSelection()).getFirstElement();

    IDtoRriParamInfo attrInfo;
    attrInfo = (IDtoRriParamInfo)((IStructuredSelection)attrsViewer.viewer().getSelection()).getFirstElement();

    Gwid gwid = Gwid.createAttr( skObj.classId(), skObj.strid(), attrInfo.id() );
    return new RriId( sectionId, gwid );
  }

  @Override
  protected ValidationResult doValidate() {
    // check selected class
    if( classesViewer.viewer().getSelection().isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать класс" );
    }
    // check selected attribute
    if( attrsViewer.viewer().getSelection().isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать атрибут" );
    }
    // check selected object
    if( objectsViewer.viewer().getSelection().isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать объект" );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  StridableTableViewer classesViewer;
  StridableTableViewer objectsViewer;
  StridableTableViewer attrsViewer;

  void createContent( Composite aParent ) {

    SashForm sashForm = new SashForm( aParent, SWT.HORIZONTAL );
    int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION;
    Composite classesPanel = new Composite( sashForm, SWT.NONE );
    classesPanel.setLayout( new BorderLayout() );
    CLabel l = new CLabel( classesPanel, SWT.NONE );
    l.setText( "Классы:" );
    l.setLayoutData( BorderLayout.NORTH );
    classesViewer = new StridableTableViewer( classesPanel, style, 120, 200, 0 );
    classesViewer.viewer().getTable().setLayoutData( BorderLayout.CENTER );
    classesViewer.viewer().addSelectionChangedListener( aEvent -> {
      IStructuredSelection selection = (IStructuredSelection)classesViewer.viewer().getSelection();
      if( !selection.isEmpty() ) {
        ISkClassInfo clsInfo = (ISkClassInfo)selection.getFirstElement();
        objectsViewer.viewer().setInput( coreApi.objService().listObjs( clsInfo.id(), true ).toArray() );
        attrsViewer.viewer().setInput( rriSection.listParamInfoes( clsInfo.id() ).toArray() );
      }
      fireContentChangeEvent();
    } );

    SashForm sashForm1 = new SashForm( sashForm, SWT.HORIZONTAL );
    Composite attrsPanel = new Composite( sashForm1, SWT.NONE );
    attrsPanel.setLayout( new BorderLayout() );
    l = new CLabel( attrsPanel, SWT.NONE );
    l.setText( "Атрибуты:" );
    l.setLayoutData( BorderLayout.NORTH );
    attrsViewer = new StridableTableViewer( attrsPanel, style, 120, 200, 0 );
    attrsViewer.viewer().getTable().setLayoutData( BorderLayout.CENTER );
    attrsViewer.viewer().addSelectionChangedListener( aEvent -> {
      fireContentChangeEvent();
    } );

    Composite objectsPanel = new Composite( sashForm1, SWT.NONE );
    objectsPanel.setLayout( new BorderLayout() );
    l = new CLabel( objectsPanel, SWT.NONE );
    l.setText( "Объекты:" );
    l.setLayoutData( BorderLayout.NORTH );
    objectsViewer = new StridableTableViewer( objectsPanel, style, 120, 200, 0 );
    objectsViewer.viewer().getTable().setLayoutData( BorderLayout.CENTER );
    objectsViewer.viewer().addSelectionChangedListener( aEvent -> {
      fireContentChangeEvent();
    } );

    sashForm.setWeights( 1, 2 );
  }

  void onSectionChanged( String aSectionId ) {
    fldSectionId.setText( aSectionId );
    rriSection = rriServ.findSection( aSectionId );
    if( rriSection != null ) {
      IStringList clsIds = rriSection.listClassIds();
      IStridablesList<ISkClassInfo> clsInfoes = coreApi.sysdescr().listClasses();
      StridablesList<ISkClassInfo> classes = new StridablesList<>();
      for( String id : clsIds ) {
        classes.add( clsInfoes.getByKey( id ) );
      }
      classesViewer.viewer().setInput( classes.toArray() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Static method to show dialog
  //

  /**
   * Выводит диалог выбора RriId.
   * <p>
   *
   * @param aRriId {@link RriId} для инициализации
   * @param aContext {@link ITsGuiContext} - контекст
   * @return {@link RriId} - выбранный параметр <b>null</b> в случает отказа от редактирования
   */
  public static final RriId selectRriId( RriId aRriId, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<RriId, ITsGuiContext> creator = PanelRriIdSelector::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "DLG_RriId_SELECTOR", "DLG_RriId_SELECTOR_D" );
    TsDialog<RriId, ITsGuiContext> d = new TsDialog<>( dlgInfo, aRriId, aContext, creator );
    return d.execData();
  }

}
