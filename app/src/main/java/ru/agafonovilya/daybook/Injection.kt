package ru.agafonovilya.daybook

import android.content.res.Resources
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.agafonovilya.daybook.model.repository.IRepository
import ru.agafonovilya.daybook.model.repository.RepositoryStub
import ru.agafonovilya.daybook.utils.GlideImageLoader
import ru.agafonovilya.daybook.utils.IImageLoader
import ru.agafonovilya.daybook.view.HomeFragment
import ru.agafonovilya.daybook.view.ScheduleFragment
import ru.agafonovilya.daybook.viewmodel.HomeViewModelFactory
import ru.agafonovilya.daybook.viewmodel.ScheduleViewModelFactory

object Injection {
    /**
     * Creates an instance of [Repository]
     */
    private fun provideRepository(): IRepository {
        return RepositoryStub()
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(fragment: Fragment): ViewModelProvider.Factory =
        when(fragment::class.java) {
            HomeFragment::class.java -> HomeViewModelFactory(provideRepository())
            ScheduleFragment::class.java -> ScheduleViewModelFactory(provideRepository())
            else -> ViewModelProvider.NewInstanceFactory()
        }


    /**
     * Provides the image loader.
     */
    fun provideImageLoader(): IImageLoader<ImageView> = GlideImageLoader()

    /**
     * Provide application resources
     */
    fun provideAppResources(): Resources = App.instance.resources
}