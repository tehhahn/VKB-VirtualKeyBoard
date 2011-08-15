/**
 * <tt>LatinKeyboard.java</tt>
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

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;

/**
 * The keyboard for VKB (Virtual KeyBoard).
 *
 * @author  Tobias Groch <tgroch@stud.hs-bremen.de>
 * @author  Florian Wolters <flwolters@stud.hs-bremen.de>
 * @version Release: @package_version@
 * @since   Class available since Release 1.0.0
 */
public class LatinKeyboard extends Keyboard {

    /**
     * The <tt>android:codes</tt> value for the <i>Alt</i> key of a PC.
     *
     * The value of this constant has no special meaning.
     */
    public static final int KEYCODE_PC_ALT = -128;

    /**
     * The <tt>android:codes</tt> value for the <i>Ctrl</i> key of a PC.
     *
     * The value of this constant has no special meaning.
     */
    public static final int KEYCODE_PC_CTRL = -129;

    /**
     * The <tt>android:codes</tt> value for the <i>Shift</i> key of a PC.
     *
     * The value of this constant has no special meaning.
     */
    public static final int KEYCODE_PC_SHIFT = -130;

    /**
     * The <tt>android:codes</tt> value for the <i>Arrow Down</i> key of a PC.
     *
     * The value of this constant has no special meaning.
     */
    public static final int KEYCODE_ARROW_DOWN = -256;

    /**
     * The <tt>android:codes</tt> value for the <i>Arrow Left</i> key of a PC.
     *
     * The value of this constant has no special meaning.
     */
    public static final int KEYCODE_ARROW_LEFT = -257;

    /**
     * The <tt>android:codes</tt> value for the <i>Arrow Right</i> key of a PC.
     *
     * The value of this constant has no special meaning.
     */
    public static final int KEYCODE_ARROW_RIGHT = -258;

    /**
     * The <tt>android:codes</tt> value for the <i>Arrow Up</i> key of a PC.
     *
     * The value of this constant has no special meaning.
     */
    public static final int KEYCODE_ARROW_UP = -259;

    /**
     * The <tt>android:codes</tt> value for the <i>language</i> key.
     *
     * The value of this constant has no special meaning.
     */
    public static final int KEYCODE_LANGUAGE = -164;

    /**
     * <tt>Key</tt> instance for the PC <i>Alt</i> key, if present.
     */
    private Key _oPcAltKey = null;

    /**
     * <tt>Key</tt> instance for the PC <i>Ctrl</i> key, if present.
     */
    private Key _oPcCtrlKey = null;

    /**
     * <tt>Key</tt> instance for the PC <i>shift</i> key, if present.
     */
    private Key _oPcShiftKey = null;

    /**
     * <tt>Key</tt> instance for the PC <i>Fn</i> key, if present.
     */
    private Key _oPcFnKey = null;

    /**
     * <tt>Key</tt> instance for the PC <i>Alt Gr</i> key, if present.
     */
    private Key _oPcAltGrKey = null;

    /**
     * Signals whether the PC <i>Alt</i> key of this keyboard is active (
     * <tt>true</tt>) or not (<tt>false</tt>).
     */
    private boolean _bIsPcAltKeyOn;

    /**
     * Signals whether the PC <i>Ctrl</i> key of this keyboard is active (
     * <tt>true</tt>) or not (<tt>false</tt>).
     */
    private boolean _bIsPcCtrlKeyOn;

    /**
     * Signals whether the PC <i>Shift</i> key of this keyboard is active (
     * <tt>true</tt>) or not (<tt>false</tt>).
     */
    private boolean _bIsPcShiftKeyOn;

    /**
     * Signals whether the PC <i>Fn</i> key of this keyboard is active (
     * <tt>true</tt>) or not (<tt>false</tt>).
     */
    private boolean _bIsPcFnKeyOn;

    /**
     * Signals whether the PC <i>Alt Gr</i> key of this keyboard is active (
     * <tt>true</tt>) or not (<tt>false</tt>).
     */
    private boolean _bIsPcAltGrKeyOn;

    /**
     * Creates a keyboard from the given xml key layout file.
     *
     * @param context        the application or service context.
     * @param xmlLayoutResId the resource file that contains the keyboard layout
     *                       and keys.
     */
    public LatinKeyboard(final Context context, final int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    /**
     * Creates a blank keyboard from the given resource file and populates it
     * with the specified characters in left-to-right, top-to-bottom fashion,
     * using the specified number of columns.
     *
     * @param context             the application or service context.
     * @param layoutTemplateResId the layout template file, containing no keys.
     * @param characters          the list of characters to display on the
     *                            keyboard. One key will be created for each
     *                            character.
     * @param columns             the number of columns of keys to display. If
     *                            this number is greater than the number of keys
     *                            that can fit in a row, it will be ignored. If
     *                            this number is -1, the keyboard will fit as
     *                            many keys as possible in each row.
     * @param horizontalPadding   the horizontal padding between two keys.
     */
    public LatinKeyboard(
        final Context context, final int layoutTemplateResId,
        final CharSequence characters, final int columns,
        final int horizontalPadding
    ) {
        super(
            context, layoutTemplateResId, characters, columns,
            horizontalPadding
        );
    }

    @Override
    protected final Key createKeyFromXml(
        final Resources res, final Row parent, final int x, final int y,
        final XmlResourceParser parser
    ) {
        final Key oKey = new Key(res, parent, x, y, parser);

        switch (oKey.codes[0]) {
        case KEYCODE_PC_ALT:
            this._oPcAltKey = oKey;
            this.getModifierKeys().add(oKey);
            break;
        case KEYCODE_PC_CTRL:
            this._oPcCtrlKey = oKey;
            this.getModifierKeys().add(oKey);
            break;
        case KEYCODE_PC_SHIFT:
            this._oPcShiftKey = oKey;
            this.getModifierKeys().add(oKey);
            break;
        case KEYCODE_ALT:
            this._oPcAltGrKey = oKey;
            this.getModifierKeys().add(oKey);
            break;
        case KEYCODE_MODE_CHANGE:
            this._oPcFnKey = oKey;
            this.getModifierKeys().add(oKey);
            break;
        default:
        }
        return oKey;
    }

    /**
     * Returns whether the PC <i>shift</i> key of this keyboard is pressed.
     *
     * @return <tt>true</tt> if the key is pressed, <tt>false</tt> otherwise.
     */
    public final boolean isPcShiftActive() {
        return this._bIsPcShiftKeyOn;
    }

    /**
     * Sets the state of the PC <i>shift</i> key of this keyboard.
     *
     * @param bSTATE <tt>true</tt> if the key is pressed, <tt>false</tt>
     *               otherwise.
     *
     * @return <tt>true</tt> if the state has been changed, <tt>false</tt>
     *         otherwise.
     */
    public final boolean setPcShiftState(final boolean bSTATE) {
        if (this._oPcShiftKey != null) {
            this._oPcShiftKey.on = bSTATE;
        }

        if (this._bIsPcShiftKeyOn != bSTATE) {
            this._bIsPcShiftKeyOn = bSTATE;
            return true;
        }

        return false;
    }

    /**
     * Returns whether the PC <i>ctrl</i> key of this keyboard is pressed.
     *
     * @return <tt>true</tt> if the key is pressed, <tt>false</tt>
     *         otherwise.
     */
    public final boolean isPcCtrlActive() {
        return this._bIsPcCtrlKeyOn;
    }

    /**
     * Sets the state of the PC <i>ctrl</i> key of this keyboard.
     *
     * @param bSTATE <tt>true</tt> if the key is pressed, <tt>false</tt>
     *               otherwise.
     *
     * @return <tt>true</tt> if the state has been changed, <tt>false</tt>
     *         otherwise.
     */
    public final boolean setPcCtrlState(final boolean bSTATE) {
        if (this._oPcCtrlKey != null) {
            this._oPcCtrlKey.on = bSTATE;
        }

        if (this._bIsPcCtrlKeyOn != bSTATE) {
            this._bIsPcCtrlKeyOn = bSTATE;
            return true;
        }

        return false;
    }

    /**
     * Returns whether the PC <i>Fn</i> key of this keyboard is pressed.
     *
     * @return <tt>true</tt> if the key is pressed, <tt>false</tt> otherwise.
     */
    public final boolean isPcFnActive() {
        return this._bIsPcFnKeyOn;
    }

    /**
     * Sets the state of the PC <i>Fn</i> key of this keyboard.
     *
     * @param bSTATE <tt>true</tt> if the key is pressed, <tt>false</tt>
     *               otherwise.
     *
     * @return <tt>true</tt> if the state has been changed, <tt>false</tt>
     *         otherwise.
     */
    public final boolean setPcFnState(final boolean bSTATE) {
        if (this._oPcFnKey != null) {
            this._oPcFnKey.on = bSTATE;
        }

        if (this._bIsPcFnKeyOn != bSTATE) {
            this._bIsPcFnKeyOn = bSTATE;
            return true;
        }

        return false;
    }

    /**
     * Returns whether the PC <i>Alt</i> key of this keyboard is pressed.
     *
     * @return <tt>true</tt> if the key is pressed, <tt>false</tt> otherwise.
     */
    public final boolean isPcAltActive() {
        return this._bIsPcAltKeyOn;
    }

    /**
     * Sets the state of the PC <i>Alt</i> key of this keyboard.
     *
     * @param bSTATE <tt>true</tt> if the key is pressed, <tt>false</tt>
     *               otherwise.
     *
     * @return <tt>true</tt> if the state has been changed, <tt>false</tt>
     *         otherwise.
     */
    public final boolean setPcAltState(final boolean bSTATE) {
        if (this._oPcAltKey != null) {
            this._oPcAltKey.on = bSTATE;
        }

        if (this._bIsPcAltKeyOn != bSTATE) {
            this._bIsPcAltKeyOn = bSTATE;
            return true;
        }

        return false;
    }

    /**
     * Returns whether the PC <i>Alt Gr</i> key of this keyboard is pressed.
     *
     * @return <tt>true</tt> if the key is pressed, <tt>false</tt> otherwise.
     */
    public final boolean isPcAltGrActive() {
        return this._bIsPcAltGrKeyOn;
    }

    /**
     * Sets the state of the PC <i>Alt Gr</i> key of this keyboard.
     *
     * @param bSTATE <tt>true</tt> if the key is pressed, <tt>false</tt>
     *               otherwise.
     *
     * @return <tt>true</tt> if the state has been changed, <tt>false</tt>
     *         otherwise.
     */
    public final boolean setPcAltGrState(final boolean bSTATE) {
        if (this._oPcAltGrKey != null) {
            this._oPcAltGrKey.on = bSTATE;
        }

        if (this._bIsPcAltGrKeyOn != bSTATE) {
            this._bIsPcAltGrKeyOn = bSTATE;
            return true;
        }

        return false;
    }
}
