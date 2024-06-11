package org.toxsoft.skf.rri.struct.gui.ugwi;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.gui.ugwi.gui.*;
import org.toxsoft.uskat.core.gui.ugwi.kinds.*;

/**
 * {@link IUgwiKindGuiHelper} implementation for {@link UgwiKindRriLink}.
 *
 * @author dima
 */
public class UgwiGuiHelperRriLink
    extends UgwiKindGuiHelperBase<ISkidList> {

  /**
   * Constructor.
   *
   * @param aKind {@link AbstractSkUgwiKind}&lt;T&gt; - the UGWI kind
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException kind of the specified ID is not registered
   */
  public UgwiGuiHelperRriLink( AbstractSkUgwiKind<ISkidList> aKind ) {
    super( aKind );
  }

  // ------------------------------------------------------------------------------------
  // UgwiKindGuiHelper
  //

  @Override
  protected IGenericEntityEditPanel<Ugwi> doCreateEntityPanel( ITsGuiContext aTsContext, boolean aViewer ) {
    // set kind of RRI prop (link)
    SingleSkPropUgwiSelectPanel.OPDEF_CLASS_PROP_KIND.setValue( aTsContext.params(),
        avValobj( ESkClassPropKind.LINK ) );
    SingleRriPropUgwiSelectPanel.OPDEF_RRI_UGWI_KIND_ID.setValue( aTsContext.params(),
        avStr( UgwiKindRriLink.KIND_ID ) );

    return new SingleRriPropUgwiSelectPanel( aTsContext, aViewer );
  }

  @Override
  protected IGenericSelectorPanel<Ugwi> doCreateSelectorPanel( ITsGuiContext aTsContext, boolean aViewer ) {
    SingleSkPropUgwiSelectPanel.OPDEF_CLASS_PROP_KIND.setValue( aTsContext.params(),
        avValobj( ESkClassPropKind.LINK ) );
    SingleRriPropUgwiSelectPanel.OPDEF_RRI_UGWI_KIND_ID.setValue( aTsContext.params(),
        avStr( UgwiKindRriLink.KIND_ID ) );

    return new SingleRriPropUgwiSelectPanel( aTsContext, aViewer );
  }

}
