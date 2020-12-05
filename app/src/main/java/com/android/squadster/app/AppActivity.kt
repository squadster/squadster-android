package com.android.squadster.app

import android.os.Bundle
import com.android.squadster.R
import com.android.squadster.app.presentation.AppPresenter
import com.android.squadster.app.presentation.AppView
import com.android.squadster.core.FlowFragment
import com.android.squadster.databinding.ActivityAppBinding
import com.android.squadster.di.DI
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.Toothpick
import javax.inject.Inject

class AppActivity : MvpAppCompatActivity(), AppView {

    @InjectPresenter
    lateinit var presenter: AppPresenter

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private lateinit var binding: ActivityAppBinding

    private val navigator: Navigator =
        SupportAppNavigator(this, supportFragmentManager, R.id.appContainer_fl)

    private val currentFlowFragment: FlowFragment?
        get() = supportFragmentManager.findFragmentById(R.id.appContainer_fl) as? FlowFragment

    @ProvidePresenter
    fun providePresenter(): AppPresenter =
        Toothpick.openScope(DI.APP_SCOPE).getInstance(AppPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.SERVER_SCOPE))
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        presenter.onAppStarted()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()

        super.onPause()
    }

    override fun onBackPressed() {
        currentFlowFragment?.onBackPressed() ?: presenter.onBackPressed()
    }
}
