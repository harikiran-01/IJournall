package com.bliss.auth.hiltmodule

import com.bliss.auth.repo.AccessRepository
import com.bliss.auth.repo.AccessRepositoryImpl
import com.bliss.auth.repo.UserLocalDataSource
import com.bliss.auth.ui.views.LoginFragment
import com.bliss.auth.ui.views.RegisterFragment
import com.hk.ijournal.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object AccessModule {

    @Provides
    fun provideLoginFragment() = LoginFragment()

    @Provides
    fun provideRegisterFragment() = RegisterFragment()
}

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalUserDataSource

    @Singleton
    @LocalUserDataSource
    @Provides
    fun provideUserLocalDataSource(
        userDao: UserDao,
        ioDispatcher: CoroutineDispatcher
    ): UserLocalDataSource {
        return UserLocalDataSource(userDao, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        @LocalUserDataSource userLocalDataSource: UserLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): AccessRepository {
        return AccessRepositoryImpl(userLocalDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}
