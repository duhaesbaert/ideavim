/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package com.maddyhome.idea.vim.vimscript.services

import com.maddyhome.idea.vim.api.VimEditor
import com.maddyhome.idea.vim.api.injector
import com.maddyhome.idea.vim.ex.ExException
import com.maddyhome.idea.vim.ex.exExceptionMessage
import com.maddyhome.idea.vim.options.OptionAccessScope
import com.maddyhome.idea.vim.options.OptionScope
import com.maddyhome.idea.vim.vimscript.model.datatypes.VimDataType

/**
 * COMPATIBILITY-LAYER: Moved to a different package
 * Please see: https://jb.gg/zo8n0r
 */
// Used by:
// * which-key 0.8.0 - getOptionValue$default(OptionScope, String)
// * IdeaVimExtension 1.6.8 - setOption$default(Scope, String)
@Deprecated("Use VimOptionGroup")
public interface OptionService {

  /**
   * Gets option value.
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param token used in exception messages
   * @throws ExException("E518: Unknown option: $token")
   */
  // Used by which-key 0.8.0 - "which-key", "timeout" and "timeoutlen"
  @Deprecated("Use injector.optionGroup functions")
  public fun getOptionValue(scope: OptionScope, optionName: String, token: String = optionName): VimDataType

  /**
   * COMPATIBILITY-LAYER: Added this class
   * Please see: https://jb.gg/zo8n0r
   */
  // Used by IdeaVimExtension 1.6.5 + 1.6.8, passed to setOption
  public sealed class Scope {
    public object GLOBAL : Scope()
    public class LOCAL(public val editor: VimEditor) : Scope()
  }
}

@Suppress("DEPRECATION")
internal class OptionServiceImpl : OptionService {
  override fun getOptionValue(scope: OptionScope, optionName: String, token: String): VimDataType {
    val option = injector.optionGroup.getOption(optionName) ?: throw exExceptionMessage("E518", token)
    return injector.optionGroup.getOptionValue(option, OptionAccessScope.GLOBAL(null))
  }
}
