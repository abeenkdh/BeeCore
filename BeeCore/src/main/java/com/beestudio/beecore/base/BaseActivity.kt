package com.beestudio.beecore.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VM : AndroidViewModel, V : ViewBinding>(private val viewModelClass: Class<VM>) : AppCompatActivity() {
    lateinit var binding: V

    val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getBinding()
        setContentView(binding.root)
        setupBinding(binding)
    }

    open fun setupBinding(binding: V){}
}