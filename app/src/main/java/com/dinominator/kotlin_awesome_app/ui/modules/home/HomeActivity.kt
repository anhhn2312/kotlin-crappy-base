package com.dinominator.kotlin_awesome_app.ui.modules.home

import android.os.Bundle
import com.dinominator.extensions.add
import com.dinominator.kotlin_awesome_app.R
import com.dinominator.kotlin_awesome_app.base.BaseActivity

/**
 * Created by Andy Ha on 5/3/2019.
 */

class HomeActivity : BaseActivity() {
    override fun layoutRes(): Int = R.layout.activity_home

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        add(R.id.container, HomeFragment())
    }
}