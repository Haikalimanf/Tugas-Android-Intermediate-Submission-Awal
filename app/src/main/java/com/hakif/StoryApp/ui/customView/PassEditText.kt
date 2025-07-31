package com.hakif.StoryApp.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.hakif.StoryApp.R

class PassEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var passwordVisibleIcon: Drawable
    private var passwordInvisibleIcon: Drawable
    private var isPasswordVisible = false
    private val poppinsTypeface: Typeface? = ResourcesCompat.getFont(context, R.font.poppins)

    init {
        passwordVisibleIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_open) as Drawable
        passwordInvisibleIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_off) as Drawable

        setOnTouchListener(this)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) showPasswordToggleIcon() else hidePasswordToggleIcon()
                if (s.toString().length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                } else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onDraw(canvas: Canvas) {
        typeface = poppinsTypeface
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        super.onDraw(canvas)
    }

    private fun showPasswordToggleIcon() {
        val icon = if (isPasswordVisible) passwordVisibleIcon else passwordInvisibleIcon
        setButtonDrawables(endOfTheText = icon)
    }

    private fun hidePasswordToggleIcon() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val iconStart: Float
            val iconEnd: Float
            var isIconClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                iconEnd = (passwordInvisibleIcon.intrinsicWidth + paddingStart).toFloat()
                if (event.x < iconEnd) isIconClicked = true
            } else {
                iconStart = (width - paddingEnd - passwordInvisibleIcon.intrinsicWidth).toFloat()
                if (event.x > iconStart) isIconClicked = true
            }

            if (isIconClicked && event.action == MotionEvent.ACTION_UP) {
                isPasswordVisible = !isPasswordVisible
                val selection = selectionEnd
                inputType = if (isPasswordVisible)
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                typeface = poppinsTypeface
                setSelection(selection)
                showPasswordToggleIcon()
                return true
            }
        }
        return false
    }
}