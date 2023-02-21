/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package com.maddyhome.idea.vim.api

import com.maddyhome.idea.vim.ex.ExException
import com.maddyhome.idea.vim.options.Option
import com.maddyhome.idea.vim.options.OptionChangeListener
import com.maddyhome.idea.vim.options.OptionScope
import com.maddyhome.idea.vim.options.OptionValueAccessor
import com.maddyhome.idea.vim.options.StringOption
import com.maddyhome.idea.vim.options.ToggleOption
import com.maddyhome.idea.vim.vimscript.model.datatypes.VimDataType

interface VimOptionGroup {

  /**
   * Gets option value.
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token")
   */
  fun getOptionValue(scope: OptionScope, optionName: String, commandArgumentText: String): VimDataType

  /**
   * Sets option value.
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param value option value
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token")
   */
  fun setOptionValue(scope: OptionScope, optionName: String, value: VimDataType, commandArgumentText: String)

  /**
   * Checks if the [value] is contained in string option.
   *
   * Returns false if there is no option with the given optionName, or it's type is different from string.
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param value option value
   */
  fun contains(scope: OptionScope, optionName: String, value: String): Boolean

  /**
   * Splits a string option into flags
   *
   * e.g. the `fileencodings` option with value "ucs-bom,utf-8,default,latin1" will result listOf("ucs-bom", "utf-8", "default", "latin1")
   *
   * returns null if there is no option with the given optionName, or its type is different from string.
   * @param scope global/local option scope
   * @param optionName option name or alias
   */
  fun getValues(scope: OptionScope, optionName: String): List<String>?

  /**
   * Same as [setOptionValue], but automatically casts [value] to the required [VimDataType]
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param value option value
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token") in case the option is not found
   * @throws ExException("E474: Invalid argument: $token") in case the cast to VimDataType is impossible
   */
  fun setOptionValue(scope: OptionScope, optionName: String, value: String, commandArgumentText: String)

  /**
   * Same as `set {option}+={value}` in Vim documentation.
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param value option value
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token") in case the option is not found
   * @throws ExException("E474: Invalid argument: $token") in case the method was called for the [ToggleOption]
   * @throws ExException("E474: Invalid argument: $token") in case the method was called for the [StringOption] and the argument is invalid (does not satisfy the option bounded values)
   * @throws ExException("E521: Number required after =: $token") in case the cast to VimInt is impossible
   */
  fun appendValue(scope: OptionScope, optionName: String, value: String, commandArgumentText: String)

  /**
   * Same as `set {option}^={value}` in Vim documentation.
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param value option value
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token") in case the option is not found
   * @throws ExException("E474: Invalid argument: $token") in case the method was called for the [ToggleOption]
   * @throws ExException("E474: Invalid argument: $token") in case the method was called for the [StringOption] and the argument is invalid (does not satisfy the option bounded values)
   * @throws ExException("E521: Number required after =: $token") in case the cast to VimInt is impossible
   */
  fun prependValue(scope: OptionScope, optionName: String, value: String, commandArgumentText: String)

  /**
   * Same as `set {option}-={value}` in Vim documentation.
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param value option value
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token") in case the option is not found
   * @throws ExException("E474: Invalid argument: $token") in case the method was called for the [ToggleOption]
   * @throws ExException("E474: Invalid argument: $token") in case the method was called for the [StringOption] and the argument is invalid (does not satisfy the option bounded values)
   * @throws ExException("E521: Number required after =: $token") in case the cast to VimInt is impossible
   */
  fun removeValue(scope: OptionScope, optionName: String, value: String, commandArgumentText: String)

  /**
   * Checks if the option's value set to default.
   *
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @throws ExException("E518: Unknown option: $optionName") in case the option is not found
   */
  fun isDefault(scope: OptionScope, optionName: String): Boolean

  /**
   * Resets option's value to default.
   *
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token") in case the option is not found
   */
  fun resetDefault(scope: OptionScope, optionName: String, commandArgumentText: String)

  /**
   * Resets all options back to default values.
   */
  fun resetAllOptions()

  /**
   * Checks if the option with given optionName is a toggleOption.
   * @param optionName option name or alias
   */
  fun isToggleOption(optionName: String): Boolean

  /**
   * Sets the option on (true).
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param commandArgumentText used in exception messages
   * @throws ExException("E518: Unknown option: $token") in case the option is not found
   * @throws ExException("E474: Invalid argument: $token") in case the option is not a [ToggleOption]
   */
  fun setOption(scope: OptionScope, optionName: String, commandArgumentText: String)

  /**
   * Unsets the option (false).
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token") in case the option is not found
   * @throws ExException("E474: Invalid argument: $token") in case the option is not a [ToggleOption]
   */
  fun unsetOption(scope: OptionScope, optionName: String, commandArgumentText: String)

  /**
   * Inverts boolean option value true -> false / false -> true.
   * @param scope global/local option scope
   * @param optionName option name or alias
   * @param commandArgumentText the text of the command argument typed by the user. Used in exception messages
   * @throws ExException("E518: Unknown option: $token") in case the option is not found
   * @throws ExException("E474: Invalid argument: $token") in case the option is not a [ToggleOption]
   */
  fun toggleOption(scope: OptionScope, optionName: String, commandArgumentText: String)

  /**
   * @return list of all option names
   */
  fun getOptions(): Set<String>

  /**
   * Adds the option.
   * @param option option
   */
  fun addOption(option: Option<out VimDataType>)

  /**
   * Removes the option.
   * @param optionName option name or alias
   */
  fun removeOption(optionName: String)

  /**
   * Adds a listener to the option.
   * @param optionName option name or alias
   * @param listener option listener
   * @param executeOnAdd whether execute listener after the method call or not
   */
  fun addListener(optionName: String, listener: OptionChangeListener<VimDataType>, executeOnAdd: Boolean = false)

  /**
   * Remove the listener from the option.
   * @param optionName option name or alias
   * @param listener option listener
   */
  fun removeListener(optionName: String, listener: OptionChangeListener<VimDataType>)

  /**
   * Get the [Option] by its name or abbreviation
   */
  fun getOption(key: String): Option<out VimDataType>?

  /**
   * Return an accessor class to easily retrieve options values
   *
   * Note that passing `null` as an editor means that you're only interested in global options - NOT global values of
   * local to buffer or local to window or global-local options! For that, use [getOptionValue].
   *
   * @param editor The editor to use to retrieve local option values. If `null`, then only global values are available
   * @return An instance of [OptionValueAccessor] to provide easy API to get option values
   */
  fun getValueAccessor(editor: VimEditor?): OptionValueAccessor
}