/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package org.jetbrains.plugins.ideavim.ex.implementation.commands

import com.maddyhome.idea.vim.api.injector
import com.maddyhome.idea.vim.vimscript.model.commands.WriteNextFileCommand
import org.jetbrains.plugins.ideavim.VimTestCase
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WriteNextCommandTest : VimTestCase() {
  @Test
  fun `command parsing`() {
    val command = injector.vimscriptParser.parseCommand("wnext 42")
    assertTrue(command is WriteNextFileCommand)
    assertEquals("42", command.argument)
  }
}