package org.toxsoft.skf.rri.values.gui.ugwi;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.gui.ugwi.gui.*;

/**
 * {@link IUgwiKindGuiHelper} implementation for {@link UgwiKindRriLink}.
 *
 * @author dima
 */
public class UgwiGuiHelperRriLink
    extends UgwiKindGuiHelperBase<ISkidList> {

  /**
   * The registrator singleton.
   */
  // public static final SkUgwiGuiUtils.IRegistrator<ISkidList> REGISTRATOR =
  // aKind -> aKind.registerHelper( IUgwiKindGuiHelper.class, new UgwiGuiHelperRriLink( aKind ) );

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

    // TODO UgwiGuiHelperSkAttr.doCreateEntityPanel()

    return super.doCreateEntityPanel( aTsContext, aViewer );
  }

  @Override
  protected IGenericSelectorPanel<Ugwi> doCreateSelectorPanel( ITsGuiContext aTsContext, boolean aViewer ) {

    // TODO UgwiGuiHelperSkAttr.doCreateSelectorPanel()

    return super.doCreateSelectorPanel( aTsContext, aViewer );
  }

}
