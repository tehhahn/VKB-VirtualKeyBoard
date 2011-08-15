/**
 * <tt>LatinKeyboardView.java</tt>
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
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * The view that renders the keyboard for VKB (Virtual KeyBoard).
 *
 * It handles rendering of keys and detecting key presses and touch movements.
 *
 * @author  Tobias Groch <tgroch@stud.hs-bremen.de>
 * @author  Florian Wolters <flwolters@stud.hs-bremen.de>
 * @version Release: @package_version@
 * @since   Class available since Release 1.0.0
 */
public class LatinKeyboardView extends KeyboardView {

    /**
     * Constructs a newly allocated <tt>LatinKeyboardView</tt>.
     *
     * @param context the context of the view.
     * @param attrs   a collection of attributes, as found associated with a tag
     *                in an XML document.
     */
    public LatinKeyboardView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a newly allocated <tt>LatinKeyboardView</tt>.
     *
     * @param context  the context of the view.
     * @param attrs    a collection of attributes, as found associated with a
     *                 tag in an XML document.
     * @param defStyle the default style of the view.
     */
    public LatinKeyboardView(
        final Context context, final AttributeSet attrs, final int defStyle
    ) {
        super(context, attrs, defStyle);
    }

    /**
     * Sets the state of the PC <i>Alt</i> key of the keyboard, if any.
     *
     * @param bSTATE <tt>true</tt> if the state of the PC <i>Alt</i> key to
     *               enable, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if the PC <i>Alt</i> key state changed,
     *         <tt>false</tt> if there was no change.
     * @see LatinKeyboardView#isPcAltActive()
     */
    public final boolean setPcAltState(final boolean bSTATE) {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        boolean bReturn = false;

        if (null != oKEYBOARD) {
            if (oKEYBOARD.setPcAltState(bSTATE)) {
                // The whole keyboard probably needs to be redrawn.
                this.invalidateAllKeys();
                bReturn = true;
            }
        }

        return bReturn;
    }

    /**
     * Returns the state of the PC <i>Alt</i> key of the keyboard, if any.
     *
     * @return <tt>true</tt> if the key is in a pressed state, <tt>false</tt>
     *         otherwise. If there is no PC <i>Alt</i> key on the keyboard or
     *         there is no keyboard attached, it returns <tt>false</tt>.
     *
     * @see LatinKeyboardView#setPcAltState(boolean)
     */
    public final boolean isPcAltActive() {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        if (null != oKEYBOARD) {
            return oKEYBOARD.isPcAltActive();
        }
        return false;
    }

    /**
     * Sets the state of the PC <i>Ctrl</i> key of the keyboard, if any.
     *
     * @param bSTATE <tt>true</tt> if the state of the PC <i>Ctrl</i> key to
     *               enable, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if the PC <i>Ctrl</i> key state changed,
     *         <tt>false</tt> if there was no change.
     * @see LatinKeyboardView#isPcCtrlActive()
     */
    public final boolean setPcCtrlState(final boolean bSTATE) {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        boolean bReturn = false;

        if (null != oKEYBOARD) {
            if (oKEYBOARD.setPcCtrlState(bSTATE)) {
                // The whole keyboard probably needs to be redrawn.
                this.invalidateAllKeys();
                bReturn = true;
            }
        }

        return bReturn;
    }

    /**
     * Returns the state of the PC <i>Ctrl</i> key of the keyboard, if any.
     *
     * @return <tt>true</tt> if the key is in a pressed state, <tt>false</tt>
     *         otherwise. If there is no PC <i>Ctrl</i> key on the keyboard or
     *         there is no keyboard attached, it returns <tt>false</tt>.
     *
     * @see LatinKeyboardView#setPcCtrlState(boolean)
     */
    public final boolean isPcCtrlActive() {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        if (null != oKEYBOARD) {
            return oKEYBOARD.isPcCtrlActive();
        }
        return false;
    }

    /**
     * Sets the state of the PC <i>Shift</i> key of the keyboard, if any.
     *
     * @param bSTATE <tt>true</tt> if the state of the PC <i>Shift</i> key to
     *               enable, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if the PC <i>Shift</i> key state changed,
     *         <tt>false</tt> if there was no change.
     * @see LatinKeyboardView#isPcShiftActive()
     */
    public final boolean setPcShiftState(final boolean bSTATE) {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        boolean bReturn = false;

        if (null != oKEYBOARD) {
            if (oKEYBOARD.setPcShiftState(bSTATE)) {
                // The whole keyboard probably needs to be redrawn.
                this.invalidateAllKeys();
                bReturn = true;
            }
        }

        return bReturn;
    }

    /**
     * Returns the state of the PC <i>Shift</i> key of the keyboard, if any.
     *
     * @return <tt>true</tt> if the key is in a pressed state, <tt>false</tt>
     *         otherwise. If there is no PC <i>Shift</i> key on the keyboard or
     *         there is no keyboard attached, it returns <tt>false</tt>.
     *
     * @see LatinKeyboardView#setPcShiftState(boolean)
     */
    public final boolean isPcShiftActive() {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        if (null != oKEYBOARD) {
            return oKEYBOARD.isPcShiftActive();
        }
        return false;
    }

    /**
     * Sets the state of the PC <i>Fn</i> key of the keyboard, if any.
     *
     * @param bSTATE <tt>true</tt> if the state of the PC <i>Fn</i> key to
     *               enable, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if the PC <i>Fn</i> key state changed,
     *         <tt>false</tt> if there was no change.
     * @see LatinKeyboardView#isPcFnActive()
     */
    public final boolean setPcFnState(final boolean bSTATE) {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        boolean bReturn = false;

        if (null != oKEYBOARD) {
            if (oKEYBOARD.setPcFnState(bSTATE)) {
                // The whole keyboard probably needs to be redrawn.
                this.invalidateAllKeys();
                bReturn = true;
            }
        }

        return bReturn;
    }

    /**
     * Returns the state of the PC <i>Fn</i> key of the keyboard, if any.
     *
     * @return <tt>true</tt> if the key is in a pressed state, <tt>false</tt>
     *         otherwise. If there is no PC <i>Fn</i> key on the keyboard or
     *         there is no keyboard attached, it returns <tt>false</tt>.
     *
     * @see LatinKeyboardView#setPcFnState(boolean)
     */
    public final boolean isPcFnActive() {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        if (null != oKEYBOARD) {
            return oKEYBOARD.isPcFnActive();
        }
        return false;
    }

    /**
     * Sets the state of the PC <i>Alt Gr</i> key of the keyboard, if any.
     *
     * @param bSTATE <tt>true</tt> if the state of the PC <i>Alt Gr</i> key to
     *               enable, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if the PC <i>Alt Gr</i> key state changed,
     *         <tt>false</tt> if there was no change.
     * @see LatinKeyboardView#isPcAltGrActive()
     */
    public final boolean setPcAltGrState(final boolean bSTATE) {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        boolean bReturn = false;

        if (null != oKEYBOARD) {
            if (oKEYBOARD.setPcAltGrState(bSTATE)) {
                // The whole keyboard probably needs to be redrawn.
                this.invalidateAllKeys();
                bReturn = true;
            }
        }

        return bReturn;
    }

    /**
     * Returns the state of the PC <i>Alt Gr</i> key of the keyboard, if any.
     *
     * @return <tt>true</tt> if the key is in a pressed state, <tt>false</tt>
     *         otherwise. If there is no PC <i>Alt Gr</i> key on the keyboard or
     *         there is no keyboard attached, it returns <tt>false</tt>.
     *
     * @see LatinKeyboardView#setPcAltGrState(boolean)
     */
    public final boolean isPcAltGrActive() {
        final LatinKeyboard oKEYBOARD = (LatinKeyboard) this.getKeyboard();
        if (null != oKEYBOARD) {
            return oKEYBOARD.isPcAltGrActive();
        }
        return false;
    }
}
