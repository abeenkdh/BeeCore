package com.beestudio.beecore

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View.inflate
import androidx.viewbinding.ViewBinding
import com.beestudio.beecore.base.BaseActivity
import com.beestudio.beecore.base.findClass
import com.beestudio.beecore.base.getBinding

@Suppress("UNCHECKED_CAST")
class BeeDialog<VB : ViewBinding>(private val context: Context, val layoutInflater: LayoutInflater) {
    lateinit var dialog: AlertDialog.Builder
    private var _binding: ViewBinding? = null
    lateinit var bindingInflater: (LayoutInflater) -> VB

     val binding: VB
        get() = _binding as VB

    fun create(block : (VB) -> Unit){

     //   _binding = ViewBinding.inflate(LayoutInflater.from(act))
        block.invoke(_binding!! as VB)

        dialog = AlertDialog.Builder(context)
        dialog.setView(_binding!!.root)
        dialog.setCancelable(true)
    }

}