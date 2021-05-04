package com.beestudio.beecore.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VM : AndroidViewModel,V : ViewBinding>(private val viewModelClass: Class<VM>) : Fragment() {
    private var _binding: V? = null
    val binding: V
        get() = _binding
            ?: throw RuntimeException("Should only use binding after onCreateView and before onDestroyView")

    val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    open fun onStarted(savedInstanceState: Bundle?){}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getBinding(inflater, container)
        onStarted(savedInstanceState)
        setupBinding(binding)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun setupBinding(binding: V){}
}