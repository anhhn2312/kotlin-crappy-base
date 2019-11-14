package com.dinominator.kotlin_awesome_app.ui.modules.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dinominator.data.persistence.models.UserModel
import com.dinominator.extensions.observeNotNull
import com.dinominator.kotlin_awesome_app.R
import com.dinominator.kotlin_awesome_app.base.BaseFragment
import com.dinominator.kotlin_awesome_app.databinding.FragmentHomeBinding
import javax.inject.Inject

/**
 * Created by Andy Ha on 6/21/2019.
 */

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mUserModel: UserModel

    private val mViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun layoutRes() = R.layout.fragment_home

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(titleResId = R.string.app_name, hideNavigation = true)
        viewBinding.lifecycleOwner = this

        mViewModel.getUser()

        mViewModel.userLiveData.observeNotNull(this) {
            mUserModel = it
            viewBinding.viewModel = mUserModel
        }
    }

    override fun viewModel() = mViewModel
}