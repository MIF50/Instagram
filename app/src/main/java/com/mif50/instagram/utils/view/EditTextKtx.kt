package com.mif50.instagram.utils.view

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


@SuppressLint("CheckResult")
fun EditText.setupTextWatch(block:(text: String) -> Unit) {

    val subject = PublishSubject.create<String>()
    subject.debounce(300, TimeUnit.MILLISECONDS)
        .subscribe {
            block(it)
        }

    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(charSequence: Editable?) {}

        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            charSequence?.toString()?.let {
                if (it.contains(' ')){
                    val text = it.replace(" ","_")
                    val length = text.length
                    this@setupTextWatch.setSelection(length)
                    subject.onNext(text)
                } else {
                    subject.onNext(it)
                }

            }
        }

    })
}



fun EditText.getString() = this.text.toString().trim()