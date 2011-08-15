/**
 * <tt>VirtualKeyBoard.java</tt>
 *
 * VKB (Virtual KeyBoard) is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * VKB (Virtual KeyBoard) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * You should have received a copy of the GNU General Public License along with
 * VKB (Virtual KeyBoard).  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author  Tobias Groch <tgroch@stud.hs-bremen.de>
 * @author  Florian Wolters <flwolters@stud.hs-bremen.de>
 * @license http://gnu.org/licenses/gpl.txt GNU General Public License
 * @version SVN: Id:$
 * @since   File available since Release 1.0.0
 */

package de.hs_bremen.vkb;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.util.Log;

/**
 * The input method editor (IME) for VKB (Virtual KeyBoard).
 *
 * @author  Tobias Groch <tgroch@stud.hs-bremen.de>
 * @author  Florian Wolters <flwolters@stud.hs-bremen.de>
 * @version Release: @package_version@
 * @see     <a href=
 * "http://developer.android.com/resources/articles/creating-input-method.html"
 *          >Creating an Input Method</a>
 * @see     <a href=
 * "http://developer.android.com/resources/articles/on-screen-inputs.html"
 *          >Onscreen Input Methods</a>
 * @since   Class available since Release 1.0.0
 */
public class VirtualKeyBoard extends InputMethodService implements
    OnKeyboardActionListener {

    /**
     * Enable debug mode if <tt>true</tt>, <tt>false</tt> otherwise.
     */
    public static final boolean DEBUG_MODE = false;

    /**
     * The possible languages for the Virtual KeyBoard.
     */
    private static enum KeyboardLanguage {
        /**
         * Germany (German).
         */
        DE,
        /**
         * United Kingdom (English).
         */
        UK,
        /**
         * France (French).
         */
        FR
    }

    /**
     * The current language of the Virtual KeyBoard.
     */
    private KeyboardLanguage _eCurrentLanguage;

    /**
     * The currently used <tt>LatinKeyboard</tt>.
     *
     * An example for a keyboard is a <i>QWERTY</i> based layout for Latin
     * script.
     */
    private LatinKeyboard _oKeyboardCurrent = null;

    /**
     * The <tt>LatinKeyboard</tt> which was used before the currently used
     * <tt>LatinKeyboard</tt>.
     */
    private LatinKeyboard _oKeyboardLast = null;

    /**
     * The currently used <tt>LatinKeyboardView</tt>.
     */
    private LatinKeyboardView _oKeyboardView;

    /**
     * The <tt>LatinKeyboard</tt> with the lowercase layout.
     */
    private LatinKeyboard _oKeyboardLowercase;

    /**
     * The <tt>LatinKeyboard</tt> with the uppercase layout.
     */
    private LatinKeyboard _oKeyboardUppercase;

    /**
     * The <tt>LatinKeyboard</tt> with the lowercase and Alt Gr layout.
     */
    private LatinKeyboard _oKeyboardLowercaseAltGr;

    /**
     * The <tt>LatinKeyboard</tt> with the uppercase and Alt Gr layout.
     */
    private LatinKeyboard _oKeyboardUppercaseAltGr;

    /**
     * The <tt>LatinKeyboard</tt> with the Math layout.
     */
    private LatinKeyboard _oKeyboardMath;

    /**
     * The mask that includes the modifier key meta state bits <tt>{@link
     * KeyEvent#META_ALT_ON}</tt>, <tt>{@link KeyEvent#META_CTRL_ON}</tt> and
     * <tt>{@link KeyEvent#META_SHIFT_ON}</tt>.
     */
    private int _iCurrentMetaKeyMask = 0;

    /**
     * The maximum width, in pixels, of this input method (IME).
     */
    private int _iLastDisplayWidth;

    /**
     * Called by the system when the service is first created.
     */
    @Override
    public final void onCreate() {
        // Do not forget to call the onCreate method of the super class.
        super.onCreate();
    }

    /**
     * This is a hook that subclasses can use to perform initialization of their
     * interface. It is called for you prior to any of your UI objects being
     * created, both after the service is first created and after a
     * configuration change happens.
     */
    @Override
    public final void onInitializeInterface() {
        // Configuration changes can happen after the keyboard gets recreated,
        // so we need to be able to re-build the keyboards if the available
        // space has changed.
        if (null != this._oKeyboardCurrent) {
            // Return the maximum width (in pixels) available for the input
            // method.
            int iDisplayWidth = this.getMaxWidth();
            if (iDisplayWidth == this._iLastDisplayWidth) {
                // The width has not changed.
                return;
            }

            this._iLastDisplayWidth = iDisplayWidth;
        }

        // Creates the mathematical keyboard from the given xml key layout file.
        this._oKeyboardMath = new LatinKeyboard(this, R.xml.math);
        // Sets the language German as the default language.
        this._eCurrentLanguage = KeyboardLanguage.DE;
        // Creates the keyboards for the given language and sets the current
        // keyboard to lowercase.
        this._createKeyboardsForCurrentLanguage();
    }

    /**
     * Creates the keyboards for the given language and sets the current
     * keyboard to lowercase.
     */
    private void _createKeyboardsForCurrentLanguage() {
        int lcXml, lcAltGrXml, ucXml, ucAltGrXml;

        switch (this._eCurrentLanguage) {
        case UK:
            lcXml = R.xml.en_uk_lc;
            lcAltGrXml = R.xml.en_uk_lc_alt_gr;
            ucXml = R.xml.en_uk_uc;
            ucAltGrXml = R.xml.en_uk_uc_alt_gr;
            break;
        case FR:
            lcXml = R.xml.fr_fr_lc;
            lcAltGrXml = R.xml.fr_fr_lc_alt_gr;
            ucXml = R.xml.fr_fr_uc;
            ucAltGrXml = R.xml.fr_fr_uc_alt_gr;
            break;
        default:
            lcXml = R.xml.de_de_lc;
            lcAltGrXml = R.xml.de_de_lc_alt_gr;
            ucXml = R.xml.de_de_uc;
            ucAltGrXml = R.xml.de_de_uc_alt_gr;
        }

        this._oKeyboardLowercase = new LatinKeyboard(this, lcXml);
        this._oKeyboardLowercaseAltGr = new LatinKeyboard(this, lcAltGrXml);
        this._oKeyboardUppercase = new LatinKeyboard(this, ucXml);
        this._oKeyboardUppercaseAltGr = new LatinKeyboard(this, ucAltGrXml);
        this._oKeyboardCurrent = this._oKeyboardLowercase;
    }

    /**
     * Create and return the view hierarchy used to show candidates.
     *
     * This will be called once, when the candidates are first displayed. You
     * can return <tt>null</tt> to have no candidates view; the default
     * implementation returns <tt>null</tt>.
     *
     * To control when the candidates view is displayed, use
     * <tt>setCandidatesViewShown(boolean)</tt>. To change the candidates view
     * after the first one is created by this function, use
     * <tt>setCandidatesView(View)</tt>.
     *
     * @return <tt>null</tt>
     */
    @Override
    public final View onCreateCandidatesView() {
        return null;
    }

    /**
     * Create and return the view hierarchy used for the input area (such as a
     * soft keyboard).
     *
     * This will be called once, when the input area is first displayed. You can
     * return <tt>null</tt> to have no input area; the default implementation
     * returns <tt>null</tt>.
     *
     * To control when the input view is displayed, implement
     * <tt>onEvaluateInputViewShown()</tt>. To change the input view after the
     * first one is created by this function, use <tt>setInputView(View)</tt>.
     *
     * @return the view hierarchy used for the input area
     */
    @Override
    public final View onCreateInputView() {
        this._oKeyboardView = (LatinKeyboardView) getLayoutInflater().inflate(
            R.layout.input, null
        );

        this._oKeyboardView.setOnKeyboardActionListener(this);

        // Apply the selected keyboard to the input view.
        this._oKeyboardView.setKeyboard(this._oKeyboardCurrent);

        // Return the input view.
        return this._oKeyboardView;
    }

    /**
     * Called when the input view is being shown and input has started on a new
     * editor.
     *
     * This will always be called after <tt>{@link #onStartInput(EditorInfo,
     * boolean)}</tt>, allowing you to do your general setup there and just
     * view-specific setup here. You are guaranteed that
     * <tt>onCreateInputView()</tt> will have been called some time before this
     * function is called.
     *
     * @param info       the description of the type of text being edited.
     * @param restarting <tt>true</tt> if we are restarting input on the same
     *                   text field as before.
     */
    @Override
    public final void onStartInputView(
        final EditorInfo info, final boolean restarting
    ) {
        super.onStartInputView(info, restarting);
        // Apply the selected keyboard to the input view.
        this._oKeyboardView.setKeyboard(this._oKeyboardCurrent);
    }

    /**
     * Called to inform the input method that text input has started in an
     * editor.
     *
     * You should use this callback to initialize the state of your input to
     * match the state of the editor given to it.
     *
     * @param attribute  the attributes of the editor that input is starting in.
     * @param restarting set to <tt>true</tt> if input is restarting in the same
     *                   editor such as because the application has changed the
     *                   text in the editor. Otherwise will be <tt>false</tt>,
     *                   indicating this is a new session with the editor.
     */
    @Override
    public final void onStartInput(
        final EditorInfo attribute, final boolean restarting
    ) {
        super.onStartInput(attribute, restarting);
    }

    /**
     * Called to inform the input method that text input has finished in the
     * last editor.
     *
     * At this point there may be a call to
     * <tt>onStartInput(EditorInfo, boolean)</tt> to perform input in a new
     * editor, or the input method may be left idle. This method is not called
     * when input restarts in the same editor.
     *
     * The default implementation uses the <tt>InputConnection</tt> to clear any
     * active composing text; you can override this (not calling the base class
     * implementation) to perform whatever behavior you would like.
     */
    @Override
    public final void onFinishInput() {
        super.onFinishInput();
    }

    /**
     * Called when the application has reported a new selection region of the
     * text.
     *
     * This is called whether or not the input method has requested extracted
     * text updates, although if so it will not receive this call if the
     * extracted text has changed as well.
     *
     * The default implementation takes care of updating the cursor in the
     * extract text, if it is being shown.
     *
     * @param oldSelStart     the start position of the old selection.
     * @param oldSelEnd       the end position of the old selection.
     * @param newSelStart     the start position of the new selection.
     * @param newSelEnd       the end position of the new selection.
     * @param candidatesStart the start position of the candidates.
     * @param candidatesEnd   the end position of the candidates.
     */
    @Override
    public final void onUpdateSelection(
        final int oldSelStart, final int oldSelEnd, final int newSelStart,
        final int newSelEnd, final int candidatesStart, final int candidatesEnd
    ) {
        super.onUpdateSelection(
            oldSelStart, oldSelEnd, newSelStart, newSelEnd,
            candidatesStart, candidatesEnd
        );
    }

    /**
     * Called when the application has reported auto-completion candidates that
     * it would like to have the input method displayed.
     *
     * Typically these are only used when an input method is running in
     * full-screen mode, since otherwise the user can see and interact with the
     * pop-up window of completions shown by the application.
     *
     * The default implementation here does nothing.
     *
     * @param completions the auto-completion candidates. Each contains
     *                    information about a single text completion that an
     *                    editor has reported to an input method.
     */
    @Override
    public final void onDisplayCompletions(final CompletionInfo[] completions) {
        super.onDisplayCompletions(completions);
    }

    // Implementation of KeyboardViewListener.

    /**
     * Helper method to send an alternate key (<i>Alt</i>) to the current
     * editor.
     */
    private void _handleAlt() {
        this._iCurrentMetaKeyMask ^= KeyEvent.META_ALT_ON;
    }

    /**
     * Helper method to send a control key (<i>Ctrl</i>) to the current editor.
     */
    private void _handleCtrl() {
        this._iCurrentMetaKeyMask ^= KeyEvent.META_CTRL_ON;
    }

    /**
     * Helper method to send <i>Shift</i> to the current editor.
     */
    private void _handleShift() {
        this._iCurrentMetaKeyMask ^= KeyEvent.META_SHIFT_ON;
    }

    /**
     * Helper method to manage the current state of the keyboard.
     */
    private void _switchCapitalization() {
        if (DEBUG_MODE) {
            Log.w(
                "Keyboard::isShifted", //$NON-NLS-1$
                String.valueOf(this._oKeyboardCurrent.isShifted())
            );
            Log.w(
                "KeyboardView::isShifted", //$NON-NLS-1$
                String.valueOf(this._oKeyboardView.isShifted())
            );
        }

        if (this._oKeyboardCurrent == this._oKeyboardLowercase) {
            // this._oKeyboardLowercase.setShifted(true);
            // this._oKeyboardLowercaseAltGr.setShifted(true);
            this._oKeyboardCurrent = this._oKeyboardUppercase;
            // this._oKeyboardUppercase.setShifted(true);
            // this._oKeyboardUppercaseAltGr.setShifted(true);

        } else if (this._oKeyboardCurrent == this._oKeyboardUppercase) {
            // this._oKeyboardUppercase.setShifted(false);
            // this._oKeyboardUppercaseAltGr.setShifted(false);
            this._oKeyboardCurrent = this._oKeyboardLowercase;
            // this._oKeyboardLowercase.setShifted(false);
            // this._oKeyboardLowercaseAltGr.setShifted(false);

        } else if (this._oKeyboardCurrent == this._oKeyboardLowercaseAltGr) {
            // this._oKeyboardLowercase.setShifted(true);
            // this._oKeyboardLowercaseAltGr.setShifted(true);
            this._oKeyboardCurrent = this._oKeyboardUppercaseAltGr;
            // this._oKeyboardUppercase.setShifted(true);
            // this._oKeyboardUppercaseAltGr.setShifted(true);

        } else if (this._oKeyboardCurrent == this._oKeyboardUppercaseAltGr) {
            // this._oKeyboardUppercase.setShifted(false);
            // this._oKeyboardUppercaseAltGr.setShifted(false);
            this._oKeyboardCurrent = this._oKeyboardLowercaseAltGr;
            // this._oKeyboardLowercase.setShifted(false);
            // this._oKeyboardLowercaseAltGr.setShifted(false);
        }
        // Apply the selected keyboard to the input view.
        this._oKeyboardView.setKeyboard(this._oKeyboardCurrent);

        if (DEBUG_MODE) {
            Log.w(
                "Keyboard::isShifted", //$NON-NLS-1$
                String.valueOf(this._oKeyboardCurrent.isShifted())
            );
            Log.w(
                "KeyboardView::isShifted", //$NON-NLS-1$
                String.valueOf(this._oKeyboardView.isShifted())
            );
        }
    }

    /**
     * Helper method to manage the current state of the keyboard.
     */
    private void _switchAltGr() {
        if (DEBUG_MODE) {
            Log.w(
                "LatinKeyboard.isPcAltGrActive", //$NON-NLS-1$
                String.valueOf(this._oKeyboardCurrent.isPcAltGrActive())
            );
            Log.w(
                "LatinKeyboardView.isPcAltGrActive", //$NON-NLS-1$
                String.valueOf(this._oKeyboardView.isPcAltGrActive())
            );
        }

        if (this._oKeyboardCurrent == this._oKeyboardLowercase
                || this._oKeyboardCurrent == this._oKeyboardMath) {
            // this._oKeyboardLowercase.setPcAltGrState(true);
            // this._oKeyboardUppercase.setPcAltGrState(true);
            this._oKeyboardCurrent = this._oKeyboardLowercaseAltGr;
            // this._oKeyboardLowercaseAltGr.setPcAltGrState(true);
            // this._oKeyboardUppercaseAltGr.setPcAltGrState(true);

        } else if (this._oKeyboardCurrent == this._oKeyboardLowercaseAltGr) {
            // this._oKeyboardLowercase.setPcAltGrState(false);
            // this._oKeyboardUppercase.setPcAltGrState(false);
            this._oKeyboardCurrent = this._oKeyboardLowercase;
            // this._oKeyboardLowercaseAltGr.setPcAltGrState(false);
            // this._oKeyboardUppercaseAltGr.setPcAltGrState(false);

        } else if (this._oKeyboardCurrent == this._oKeyboardUppercase) {
            // this._oKeyboardLowercase.setPcAltGrState(true);
            // this._oKeyboardUppercase.setPcAltGrState(true);
            this._oKeyboardCurrent = this._oKeyboardUppercaseAltGr;
            // this._oKeyboardLowercaseAltGr.setPcAltGrState(true);
            // this._oKeyboardUppercaseAltGr.setPcAltGrState(true);

        } else if (this._oKeyboardCurrent == this._oKeyboardUppercaseAltGr) {
            // this._oKeyboardLowercase.setPcAltGrState(false);
            // this._oKeyboardUppercase.setPcAltGrState(false);
            this._oKeyboardCurrent = this._oKeyboardUppercase;
            // this._oKeyboardLowercaseAltGr.setPcAltGrState(false);
            // this._oKeyboardUppercaseAltGr.setPcAltGrState(false);
        }
        // Apply the selected keyboard to the input view.
        this._oKeyboardView.setKeyboard(this._oKeyboardCurrent);

        if (DEBUG_MODE) {
            Log.w(
                "LatinKeyboard.isPcAltGrActive", //$NON-NLS-1$
                String.valueOf(this._oKeyboardCurrent.isPcAltGrActive())
            );
            Log.w(
                "LatinKeyboardView.isPcAltGrActive", //$NON-NLS-1$
                String.valueOf(this._oKeyboardView.isPcAltGrActive())
            );
        }
    }

    /**
     * Helper method to manage the current state of the keyboard.
     */
    private void _switchFn() {
        if (this._oKeyboardCurrent != this._oKeyboardMath) {
            this._oKeyboardLast = this._oKeyboardCurrent;
            this._oKeyboardCurrent = this._oKeyboardMath;
            this._oKeyboardView.setKeyboard(this._oKeyboardCurrent);
            this._oKeyboardView.setPcFnState(true);
        } else {
            this._oKeyboardCurrent = this._oKeyboardLast;
            this._oKeyboardView.setKeyboard(this._oKeyboardCurrent);
        }
        // Apply the selected keyboard to the input view.
        this._oKeyboardView.setKeyboard(this._oKeyboardCurrent);
    }

    /**
     * Switched from the current language of the Virtual KeyBoard to the next
     * language (round-robin).
     */
    private void _switchLanguage() {
        switch (this._eCurrentLanguage) {
        case DE:
            this._eCurrentLanguage = KeyboardLanguage.UK;
            break;
        case UK:
            this._eCurrentLanguage = KeyboardLanguage.FR;
            break;
        case FR:
            this._eCurrentLanguage = KeyboardLanguage.DE;
            break;
        default:
            break;
        }
        // Create the Keyboard instances for the current language.
        this._createKeyboardsForCurrentLanguage();
        // Apply the selected keyboard to the input view.
        this._oKeyboardView.setKeyboard(this._oKeyboardCurrent);
    }

    /**
     * Helper method to send a character key to the current editor.
     *
     * @param primaryCode the unicode code of the key that was pressed.
     */
    private void _handleCharacter(final int primaryCode) {
        this.getCurrentInputConnection().commitText(
            String.valueOf((char) primaryCode), 1
        );
    }

    /**
     * Helper method to send a <tt>{@link KeyEvent}</tt> to the current editor.
     *
     * @param primaryCode the unicode code of the key that was pressed.
     */
    private void _handleKeyEvent(final int primaryCode) {
        InputConnection ic = this.getCurrentInputConnection();
        long eventTime = SystemClock.uptimeMillis();

        try {
            ic.sendKeyEvent(
                new KeyEvent(
                    eventTime, // The time (in uptimeMillis()) at which this key code originally went down.
                    eventTime, // The time (in uptimeMillis()) at which this event happened.
                    KeyEvent.ACTION_DOWN, // Action code: either ACTION_DOWN, ACTION_UP, or ACTION_MULTIPLE.
                    ASCIICodeToKeyEventConstantTranslator.translate(primaryCode),  // The key code.
                    0,  // A repeat count for down events (> 0 if this is after the initial down) or event count for multiple events.
                    this._iCurrentMetaKeyMask, // Flags indicating which meta keys are currently pressed.
                    KeyCharacterMap.FULL, // The device ID that generated the key event.
                    0, // Raw device scan code of the event.
                    KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, // The flags for this key event.
                    InputDevice.SOURCE_KEYBOARD // The input source such as SOURCE_KEYBOARD.
                )
            );
        } catch (NullPointerException ex) {
            ic.commitText(String.valueOf((char) primaryCode), 1);
        }
    }

    /**
     * Sends one of the four cursor movement keys (arrow keys) to the current
     * editor.
     *
     * Nothing is done if <tt>primaryCode</tt> doesn't specify one of the cursor
     * movement keys.
     *
     * @param primaryCode the unicode code of the key that was pressed.
     */
    private void _handleArrowKeys(final int primaryCode) {
        switch (primaryCode) {
        case LatinKeyboard.KEYCODE_ARROW_DOWN:
            // Down cursor movement key or arrow key.
            // Send the key event code for the Directional Pad Down key to the
            // current input connection as a key down + key up event pair.
            this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_DOWN);
            break;
        case LatinKeyboard.KEYCODE_ARROW_LEFT:
            // Left cursor movement key or arrow key.
            // Send the key event code for the Directional Pad Left key to the
            // current input connection as a key down + key up event pair.
            this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT);
            break;
        case LatinKeyboard.KEYCODE_ARROW_RIGHT:
            // Right cursor movement key or arrow key.
            // Send the key event code for the Directional Pad Right key to the
            // current input connection as a key down + key up event pair.
            this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT);
            break;
        case LatinKeyboard.KEYCODE_ARROW_UP:
            // Up cursor movement key or arrow key.
            // Send the key event code for the Directional Pad Up key to the
            // current input connection as a key down + key up event pair.
            this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_UP);
            break;
        default:
            // primaryCode doesn't specify a cursor movement key or arrow key.
            break;
        }
    }

    /**
     * Helper method to send a backspace key (<i>BS</i>) to the current editor.
     */
    private void _handleBackspaceKey() {
        this.sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL);
    }

    // Start: Implementation of the abstracts methods of class
    // android.inputmethodservice.KeyboardView.OnKeyboardActionListener.

    /**
     * Called when the user presses a key. This is sent before the
     * {@link #onKey(int, int[])} is called.
     *
     * For keys that repeat, this is only called once.
     *
     * @param primaryCode the unicode code of the key that was pressed. If the
     *                    touch is not on a valid key, the value will be zero.
     */
    @Override
    public void onPress(final int primaryCode) {
        // Empty block.
    }

    /**
     * Sends a key press to the listener.
     *
     * @param primaryCode the unicode code of the key that was released.
     * @param keyCodes    the codes for all the possible alternative keys with
     *                    the primary code being the first. If the primary key
     *                    code is a single character such as an alphabet o
     *                    number or symbol, the alternatives will include other
     *                    characters that may be on the same key or adjacent
     *                    keys. These codes are useful to correct for accidental
     *                    presses of a key adjacent to the intended key.
     */
    @Override
    public final void onKey(final int primaryCode, final int[] keyCodes) {
        if (DEBUG_MODE) {
            Log.w(
                "CURRENT_META_MASK", //$NON-NLS-1$
                String.valueOf(this._iCurrentMetaKeyMask)
            );
        }
        switch (primaryCode) {
        case LatinKeyboard.KEYCODE_ARROW_DOWN:
            // Right cursor movement key or arrow key.
        case LatinKeyboard.KEYCODE_ARROW_LEFT:
            // Left cursor movement key or arrow key.
        case LatinKeyboard.KEYCODE_ARROW_RIGHT:
            // Up cursor movement key or arrow key.
        case LatinKeyboard.KEYCODE_ARROW_UP:
            // Down cursor movement key or arrow key.
            this._handleArrowKeys(primaryCode);
            break;
        case Keyboard.KEYCODE_CANCEL:
        case Keyboard.KEYCODE_DONE:
            this._handleClose();
            break;
        case Keyboard.KEYCODE_DELETE:
            this._handleBackspaceKey();
            break;
        case Keyboard.KEYCODE_SHIFT:
            this._switchCapitalization();
            break;
        case Keyboard.KEYCODE_ALT:
            this._switchAltGr();
            break;
        case Keyboard.KEYCODE_MODE_CHANGE:
            this._switchFn();
            break;
        case LatinKeyboard.KEYCODE_PC_ALT:
            this._handleAlt();
            break;
        case LatinKeyboard.KEYCODE_PC_CTRL:
            this._handleCtrl();
            break;
        case LatinKeyboard.KEYCODE_PC_SHIFT:
            this._handleShift();
            break;
        case LatinKeyboard.KEYCODE_LANGUAGE:
            this._switchLanguage();
            break;
        default:
            // Check whether the current bit mask of the meta keys is set.
            if (0 != this._iCurrentMetaKeyMask) {
                this._handleKeyEvent(primaryCode);
            } else {
                this._handleCharacter(primaryCode);
            }
        }
    }

    /**
     * Called when the user releases a key. This is sent after the
     * {@link #onKey(int, int[])} is called.
     *
     * For keys that repeat, this is only called once.
     *
     * @param primaryCode the unicode code of the key that was released.
     */
    @Override
    public void onRelease(final int primaryCode) {
        // Empty block.
    }

    /**
     * Sends a sequence of characters to the listener.
     *
     * @param text the sequence of characters to be displayed.
     */
    @Override
    public void onText(final CharSequence text) {
        // Empty block.
    }

    /**
     * Called when the user quickly moves the finger from up to down.
     */
    @Override
    public void swipeDown() {
        // Empty block.
    }

    /**
     * Called when the user quickly moves the finger from right to left.
     */
    @Override
    public void swipeLeft() {
        // Empty block.
    }

    /**
     * Called when the user quickly moves the finger from left to right.
     */
    @Override
    public void swipeRight() {
        // Empty block.
    }

    /**
     * Called when the user quickly moves the finger from down to up.
     */
    @Override
    public void swipeUp() {
        // Empty block.
    }

    /**
     * Close (hides) the keyboard and the view that renders the key.
     */
    private void _handleClose() {
        // Close this input method's soft input area, removing it from the
        // display. The input method will continue running, but the user can no
        // longer use it to generate input by touching the screen.
        this.requestHideSelf(0);
        // Close the view that renders the keyboard.
        this._oKeyboardView.closing();
    }

    // End: Implementation of the abstracts methods of class
    // android.inputmethodservice.KeyboardView.OnKeyboardActionListener.

    // Start: Generic helper methods that simplify usage.

    /**
     * Send the given key event code (as defined by <tt>{@link KeyEvent}</tt>)
     * to the current input connection as a key down event.
     *
     * The sent event has <tt>{@link KeyEvent#FLAG_SOFT_KEYBOARD}</tt> set, so
     * that the recipient can identify it as coming from a software input
     * method, and <tt>{@link KeyEvent#FLAG_KEEP_TOUCH_MODE}</tt>, so that it
     * doesn't impact the current touch mode of the UI.
     *
     * @param keyEventCode the raw key code to send, as defined by <tt>{@link
     *                     KeyEvent}</tt>.
     */
    public final void sendDownKeyEvent(final int keyEventCode) {
        InputConnection ic = this.getCurrentInputConnection();
        if (ic != null) {
            long eventTime = SystemClock.uptimeMillis();
            ic.sendKeyEvent(
                new KeyEvent(
                    eventTime, eventTime,
                    KeyEvent.ACTION_DOWN, keyEventCode, 0, 0, 0, 0,
                    KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE
                )
            );
        }
    }

    /**
     * Send the given key event code (as defined by <tt>{@link KeyEvent}</tt>)
     * to the current input connection as a key up event.
     *
     * The sent event has <tt>{@link KeyEvent#FLAG_SOFT_KEYBOARD}</tt> set, so
     * that the recipient can identify it as coming from a software input
     * method, and <tt>{@link KeyEvent#FLAG_KEEP_TOUCH_MODE}</tt>, so that it
     * doesn't impact the current touch mode of the UI.
     *
     * @param keyEventCode the raw key code to send, as defined by <tt>{@link
     *                     KeyEvent}</tt>.
     */
    public final void sendUpKeyEvent(final int keyEventCode) {
        InputConnection ic = this.getCurrentInputConnection();
        if (ic != null) {
            long eventTime = SystemClock.uptimeMillis();
            ic.sendKeyEvent(
                new KeyEvent(
                    eventTime, eventTime,
                    KeyEvent.ACTION_UP, keyEventCode, 0, 0, 0, 0,
                    KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE
                )
            );
        }
    }

    // End: Generic helper methods that simplify usage.

}
