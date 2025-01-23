package com.interview.cisco.thousandeyes.di

import com.interview.cisco.thousandeyes.data.remote.HostRepositoryImpl
import com.interview.cisco.thousandeyes.data.service.ApiService
import com.interview.cisco.thousandeyes.domain.repo.HostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @Provides
    fun provideDataRepository(apiService: ApiService): HostRepository {
        return HostRepositoryImpl(apiService)
    }
}