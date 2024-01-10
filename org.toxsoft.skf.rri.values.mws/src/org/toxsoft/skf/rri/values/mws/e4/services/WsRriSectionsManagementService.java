package org.toxsoft.skf.rri.values.mws.e4.services;

import static org.toxsoft.skf.rri.values.mws.IWsRriValuesConstants.*;

import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.mws.e4.helpers.partman.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.values.mws.*;
import org.toxsoft.skf.rri.values.mws.e4.uiparts.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * {@link IWsRriSectionsManagementService} implementation.
 *
 * @author hazard157
 */
public class WsRriSectionsManagementService
    implements IWsRriSectionsManagementService, ITsGuiContextable, ISkConnected {

  private static final String UIPART_PID_PREFIX = WS_RRI_FULL_ID + ".rri_section_uipart"; //$NON-NLS-1$

  private final ISkRegRefInfoServiceListener rriServiceListener = ( aOp, aSectionId ) -> refreshOpenUiparts();

  private final ITsGuiContext         tsContext;
  private final ISkConnectionSupplier skConnSupplier;
  private final ITsPartStackManager   psMan;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public WsRriSectionsManagementService( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    skConnSupplier = tsContext.get( ISkConnectionSupplier.class );
    psMan = new TsPartStackManager( tsContext.eclipseContext(), PARTSTACKID_RRI_MAIN );
    if( skConn().state().isActive() ) {
      whenConnInit();
    }
    skConn().addConnectionListener( ( aSource, aOldState ) -> {
      if( aSource.state().isActive() && !aOldState.isActive() ) {
        whenConnInit();
        return;
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static String makeUipartIdFromRefbookId( String aRefbookId ) {
    return StridUtils.makeIdPath( UIPART_PID_PREFIX, aRefbookId );
  }

  private static String makeRefbookIdFromUipartId( String aUipartId ) {
    return StridUtils.removeStartingIdPath( aUipartId, UIPART_PID_PREFIX );
  }

  private void whenConnInit() {
    ISkRegRefInfoService rriServ = coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
    rriServ.eventer().addListener( rriServiceListener );
  }

  /**
   * refreshes open UIparts - close non-existing refbooks and refreshes tabs of open UIparts.
   */
  private void refreshOpenUiparts() {
    if( !skConn().state().isOpen() ) { // close all refbook's UIparts
      psMan.closeAll();
      return;
    }
    ISkRegRefInfoService rriServ = coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
    IStridablesList<ISkRriSection> refbooksList = rriServ.listSections();
    // close removed refbooks UIparts
    IStringListEdit idsToClose = new StringArrayList();
    for( String partId : psMan.listManagedParts().keys() ) {
      String refbookId = makeRefbookIdFromUipartId( partId );
      if( !refbooksList.hasKey( refbookId ) ) {
        idsToClose.add( partId );
      }
    }
    for( String partId : idsToClose ) {
      psMan.closePart( partId );
    }
    // update opened refbook UIparts headers, content is update somewhere else
    for( ISkRriSection rb : refbooksList ) {
      String refbookId = rb.id();
      String partId = makeUipartIdFromRefbookId( refbookId );
      MPart part = psMan.findPart( partId );
      if( part != null ) {
        part.setLabel( rb.nmName() );
        part.setTooltip( rb.description() );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConnSupplier.defConn();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IWsRefbooksManagementService
  //

  @Override
  public void showRriSectionUipart( ISkRriSection aRriSection ) {
    // check argument is existing section
    TsNullArgumentRtException.checkNull( aRriSection );
    // it is necessary to switch to destination perspective, because parts are always created in current perspective
    e4Helper().switchToPerspective( PERSPID_WS_RRI_MAIN, null );
    // activate part if already exists
    String uipartId = makeUipartIdFromRefbookId( aRriSection.id() );
    MPart foundPart = psMan.findPart( uipartId );
    if( foundPart != null ) {
      e4Helper().switchToPerspective( PERSPID_WS_RRI_MAIN, uipartId );
      return;
    }
    // create UIpart part
    UIpartInfo partInfo = new UIpartInfo( uipartId );
    partInfo.setCloseable( true );
    partInfo.setTooltip( aRriSection.description() );
    partInfo.setLabel( aRriSection.id() );
    partInfo.setContributionUri( Activator.PLUGIN_ID, UipartRriSectionValuesEditor.class );
    MPart newPart = psMan.createPart( partInfo );
    // set part to display specified refbook
    UipartRriSectionValuesEditor uipartBrowser = (UipartRriSectionValuesEditor)newPart.getObject();
    uipartBrowser.setRriSection( aRriSection );
  }

}
