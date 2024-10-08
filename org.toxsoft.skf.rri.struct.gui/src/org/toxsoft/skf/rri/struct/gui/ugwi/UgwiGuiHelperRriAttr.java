package org.toxsoft.skf.rri.struct.gui.ugwi;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.gui.glib.gwidsel.*;
import org.toxsoft.uskat.core.gui.ugwi.gui.*;

/**
 * {@link IUgwiKindGuiHelper} implementation for {@link UgwiKindRriAttr}.
 *
 * @author hazard157
 * @author dima
 */
public class UgwiGuiHelperRriAttr
    extends UgwiKindGuiHelperBase<IAtomicValue> {

  /**
   * Constructor.
   *
   * @param aKind {@link AbstractSkUgwiKind}&lt;T&gt; - the UGWI kind
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException kind of the specified ID is not registered
   */
  public UgwiGuiHelperRriAttr( AbstractSkUgwiKind<IAtomicValue> aKind ) {
    super( aKind );
  }

  // ------------------------------------------------------------------------------------
  // UgwiKindGuiHelper
  //

  @Override
  protected IGenericEntityEditPanel<Ugwi> doCreateEntityPanel( ITsGuiContext aTsContext, boolean aViewer ) {
    // set kind of RRI prop (attr)
    IGwidSelectorConstants.OPDEF_CLASS_PROP_KIND.setValue( aTsContext.params(), avValobj( ESkClassPropKind.ATTR ) );
    SingleRriPropUgwiSelectPanel.OPDEF_RRI_UGWI_KIND_ID.setValue( aTsContext.params(),
        avStr( UgwiKindRriAttr.KIND_ID ) );
    return new SingleRriPropUgwiSelectPanel( aTsContext, aViewer );
  }

  @Override
  protected IGenericSelectorPanel<Ugwi> doCreateSelectorPanel( ITsGuiContext aTsContext, boolean aViewer ) {
    // set kind of RRI prop (attr)
    IGwidSelectorConstants.OPDEF_CLASS_PROP_KIND.setValue( aTsContext.params(), avValobj( ESkClassPropKind.ATTR ) );
    SingleRriPropUgwiSelectPanel.OPDEF_RRI_UGWI_KIND_ID.setValue( aTsContext.params(),
        avStr( UgwiKindRriAttr.KIND_ID ) );
    return new SingleRriPropUgwiSelectPanel( aTsContext, aViewer );
  }

}
